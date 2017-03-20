package stbvideocall.jhonelee.xyt.com.aini_app.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import stbvideocall.jhonelee.xyt.com.aini_app.AiniAppliction;
import stbvideocall.jhonelee.xyt.com.aini_app.R;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.ActionType;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.RxError;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.WelfareGankActionCreator;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.component.DaggerFrontEndFragmentComponent;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.component.DaggerWelfareFragmentComponent;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.component.WelfareFragmentComponent;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankNormalItem;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.RxViewDispatch;
import stbvideocall.jhonelee.xyt.com.aini_app.sotres.RxStoreChange;
import stbvideocall.jhonelee.xyt.com.aini_app.sotres.WelfareStore;
import stbvideocall.jhonelee.xyt.com.aini_app.stat.StatName;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.activity.PictureActivity;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.adapter.WelfareGankAdapter;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.widget.HeaderViewRecyclerAdapter;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.widget.LoadMoreView;

public class WelfareFragment extends BaseFragment implements RxViewDispatch, SwipeRefreshLayout.OnRefreshListener{

    public static final String TAG = WelfareFragment.class.getSimpleName();

    @BindView(R.id.refresh_layout) SwipeRefreshLayout vRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView vWelfareRecycler;
    private LoadMoreView vLoadMore;

    private GridLayoutManager mLayoutManager;

    private WelfareFragmentComponent component;

    private WelfareGankAdapter mAdapter;

    @Inject WelfareStore mStore;
    @Inject WelfareGankActionCreator mActionCreator;
    @Inject Dispatcher mDispatcher;

    private boolean mLoadingMore = false;

    public static WelfareFragment newInstance() {
        return new WelfareFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initInjector() {
        component= DaggerWelfareFragmentComponent.builder()
                .appComponent(AiniAppliction.getAppComponent())
                .build();
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_refresh_recycler, container, false);
        ButterKnife.bind(this, contentView);
        initInjector();

        vRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        vRefreshLayout.setOnRefreshListener(this);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        vWelfareRecycler.setLayoutManager(mLayoutManager);
        vWelfareRecycler.setHasFixedSize(true);
        vWelfareRecycler.addOnScrollListener(mScrollListener);

        vLoadMore = (LoadMoreView) inflater.inflate(R.layout.load_more, vWelfareRecycler, false);
        mAdapter = new WelfareGankAdapter(this);
        mAdapter.setItemClickListener(mItemClickListener);
        HeaderViewRecyclerAdapter adapter = new HeaderViewRecyclerAdapter(mAdapter);
        adapter.setLoadingView(vLoadMore);
        vWelfareRecycler.setAdapter(adapter);

        mDispatcher.subscribeRxStore(mStore);
        mDispatcher.subscribeRxView(this);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                vRefreshLayout.setRefreshing(true);
                refreshList();
            }
        });
    }

    @Override
    public void onDestroyView() {
        mDispatcher.unsubscribeRxStore(mStore);
        mDispatcher.unsubscribeRxView(this);
        super.onDestroyView();
    }

    private void refreshList() {
        mActionCreator.getWelfareList(1);
    }

    private void loadMore() {
        mLoadingMore = true;
        vLoadMore.setStatus(LoadMoreView.STATUS_LOADING);
        mActionCreator.getWelfareList(mAdapter.getCurPage() + 1);
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case WelfareStore.ID:
                if(1 == mStore.getPage()) {
                    vRefreshLayout.setRefreshing(false);
                }
                mAdapter.updateData(mStore.getPage(), mStore.getGankList());
                mLoadingMore = false;
                vLoadMore.setStatus(LoadMoreView.STATUS_INIT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRxError(@NonNull RxError error) {
        switch (error.getAction().getType()) {
            case ActionType.GET_WELFARE_LIST:
                vRefreshLayout.setRefreshing(false);
                mLoadingMore = false;
                vLoadMore.setStatus(LoadMoreView.STATUS_FAIL);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        refreshList();
    }

    @Override
    protected String getStatPageName() {
        return StatName.PAGE_WELFARE;
    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            boolean reachBottom = mLayoutManager.findLastCompletelyVisibleItemPosition()
                    >= mLayoutManager.getItemCount() - 1;
            if(!mLoadingMore && reachBottom) {
                loadMore();
            }
        }
    };

    private WelfareGankAdapter.OnItemClickListener mItemClickListener = new WelfareGankAdapter.OnItemClickListener() {
        @Override
        public void onClickItem(View view, GankNormalItem item) {
            if(null != item) {
                getActivity().startActivity(PictureActivity.newIntent(getActivity(), item.page, item._id));
            }
        }
    };
}
