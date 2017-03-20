package stbvideocall.jhonelee.xyt.com.aini_app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import stbvideocall.jhonelee.xyt.com.aini_app.AiniAppliction;
import stbvideocall.jhonelee.xyt.com.aini_app.R;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.ActionType;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.RxError;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.TodayGankActionCreator;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.component.DaggerTodayGankFragmentComponent;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.component.TodayGankFragmentComponent;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankGirlImageItem;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankNormalItem;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.RxViewDispatch;
import stbvideocall.jhonelee.xyt.com.aini_app.sotres.RxStoreChange;
import stbvideocall.jhonelee.xyt.com.aini_app.sotres.TodayGankStore;
import stbvideocall.jhonelee.xyt.com.aini_app.stat.StatName;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.activity.PictureActivity;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.activity.SearchActivity;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.activity.WebviewActivity;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.adapter.ToadyAdapter;

public class TodayGankFragment extends BaseFragment implements RxViewDispatch, SwipeRefreshLayout.OnRefreshListener, ToadyAdapter.OnItemClickListener {


    public static final String TAG = TodayGankFragment.class.getSimpleName();
    @BindView(R.id.recycle_today)
    RecyclerView recycleToday;
    @BindView(R.id.refresh_today)
    SwipeRefreshLayout refreshToday;


    @Inject TodayGankStore mStore;
    @Inject TodayGankActionCreator mActionCreator;
    @Inject Dispatcher mDispatcher;

    private ToadyAdapter toadyAdapter;

    public static TodayGankFragment newInstance() {
        return new TodayGankFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_today, container, false);
        ButterKnife.bind(this, contentView);
        initInjector();

        initView(contentView);

        mDispatcher.subscribeRxStore(mStore);
        mDispatcher.subscribeRxView(this);
        return contentView;
    }

    private void initInjector() {
        TodayGankFragmentComponent mComponent= DaggerTodayGankFragmentComponent.builder()
                .appComponent(AiniAppliction.getAppComponent())
                .build();
        mComponent.inject(this);
    }

    private void initView(View contentView){
        recycleToday = (RecyclerView) contentView.findViewById(R.id.recycle_today);
        refreshToday = (SwipeRefreshLayout) contentView.findViewById(R.id.refresh_today);
        refreshToday.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        refreshToday.setOnRefreshListener(this);
        recycleToday.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleToday.setHasFixedSize(true);
        toadyAdapter = new ToadyAdapter(this);
        toadyAdapter.setOnItemClickListener(this);
        recycleToday.setAdapter(toadyAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshToday.post(new Runnable() {
            @Override
            public void run() {
                refreshToday.setRefreshing(true);
                refreshData();
            }
        });
    }

    @Override
    public void onDestroyView() {
        mDispatcher.unsubscribeRxStore(mStore);
        mDispatcher.unsubscribeRxView(this);
        super.onDestroyView();
    }

    private void refreshData() {
        mActionCreator.getTodayGank();
    }


    @Override
    public void onRefresh() {
        refreshData();
    }

    @Override
    public void onClickNormalItem(View view, GankNormalItem normalItem) {
        WebviewActivity.openUrl(getActivity(), normalItem.url, normalItem.desc);
    }

    @Override
    public void onClickGirlItem(View view, GankGirlImageItem girlImageItem) {
        if(null != girlImageItem && !TextUtils.isEmpty(girlImageItem.imgUrl)){
            startActivity(PictureActivity.newIntent(getActivity(),girlImageItem.imgUrl,girlImageItem.publishedAt));
        }
    }

    @Override
    public void onClickSearchItem() {
        startActivity(SearchActivity.newIntent(getActivity()));
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case TodayGankStore.ID:
                refreshToday.setRefreshing(false);
                toadyAdapter.swapData(mStore.getItems());
                break;
            default:
                break;
        }
    }

    @Override
    public void onRxError(@NonNull RxError error) {
        switch (error.getAction().getType()) {
            case ActionType.GET_TODAY_GANK:
                refreshToday.setRefreshing(false);
                break;
            default:
                break;
        }
    }

    @Override
    protected String getStatPageName() {
        return StatName.PAGE_TODAY;
    }
}
