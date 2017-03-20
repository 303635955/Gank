package stbvideocall.jhonelee.xyt.com.aini_app.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import stbvideocall.jhonelee.xyt.com.aini_app.R;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.RxViewDispatch;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.adapter.CategoryGankAdapter;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.widget.HeaderViewRecyclerAdapter;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.widget.LoadMoreView;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.widget.RecycleViewDivider;

/**
 * Created by Administrator on 2017/3/14.
 */

public abstract class CategoryGankFragment extends BaseFragment implements RxViewDispatch,SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    protected LoadMoreView vLoadMore;

    protected LinearLayoutManager mLayoutManager;

    protected CategoryGankAdapter mAdapter;

    protected boolean mLoadingMore = false;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                refreshList();
            }
        });
    }

    protected View createView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_refresh_recycler, container, false);
        ButterKnife.bind(this, view);

        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        refreshLayout.setOnRefreshListener(this);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(onScrollListener);
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
        mAdapter = new CategoryGankAdapter(this);

        vLoadMore = (LoadMoreView) inflater.inflate(R.layout.load_more, recyclerView, false);
        HeaderViewRecyclerAdapter adapter = new HeaderViewRecyclerAdapter(mAdapter);
        adapter.setLoadingView(vLoadMore);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    protected String getStatPageName() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    protected abstract void refreshList();

    protected abstract void loadMore();

    @Override
    public void onRefresh() {
        refreshList();
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener(){

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            boolean reachBottom = mLayoutManager.findLastCompletelyVisibleItemPosition()
                    >= mLayoutManager.getItemCount() - 1;
            if(!mLoadingMore && reachBottom) {
                mLoadingMore = true;
                loadMore();
            }
        }
    };
}
