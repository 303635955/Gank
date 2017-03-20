package stbvideocall.jhonelee.xyt.com.aini_app.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import stbvideocall.jhonelee.xyt.com.aini_app.AiniAppliction;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.ActionType;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.RxError;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.VideoGankActionCreator;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.component.DaggerVideoFragmentComponent;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.component.VideoFragmentComponent;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankNormalItem;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;
import stbvideocall.jhonelee.xyt.com.aini_app.sotres.RxStoreChange;
import stbvideocall.jhonelee.xyt.com.aini_app.sotres.VideoStore;
import stbvideocall.jhonelee.xyt.com.aini_app.stat.StatName;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.activity.WebviewActivity;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.adapter.CategoryGankAdapter;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.widget.LoadMoreView;

public class VideoFragment extends CategoryGankFragment {

    public static final String TAG = VideoFragment.class.getSimpleName();
    @Inject
    VideoStore mStore;
    @Inject
    VideoGankActionCreator mActionCreator;
    @Inject
    Dispatcher mDispatcher;

    private VideoFragmentComponent component;
    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = createView(inflater,container);
        mAdapter.setOnItemClickListener(new CategoryGankAdapter.OnItemClickListener() {
            @Override
            public void onClickNormalItem(View view, GankNormalItem normalItem) {
                WebviewActivity.openUrl(getActivity(), normalItem.url, normalItem.desc);
            }
        });

        initInjector();
        mDispatcher.subscribeRxStore(mStore);
        mDispatcher.subscribeRxView(this);
        return contentView;
    }



    private void initInjector() {
        component= DaggerVideoFragmentComponent.builder()
                .appComponent(AiniAppliction.getAppComponent())
                .build();
        component.inject(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDispatcher.unsubscribeRxStore(mStore);
        mDispatcher.unsubscribeRxView(this);
    }

    @Override
    protected void refreshList() {
        mActionCreator.getVideoList(1);
    }

    @Override
    protected void loadMore() {
        vLoadMore.setStatus(LoadMoreView.STATUS_LOADING);
        mActionCreator.getVideoList(mAdapter.getmCurPage() + 1);

    }

    @Override
    protected String getStatPageName() {
        return StatName.PAGE_VIDEO;
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()){
            case VideoStore.ID:
                if(1 == mStore.getPage()) {
                    refreshLayout.setRefreshing(false);
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
            case ActionType.GET_VIDEO_LIST:
                refreshLayout.setRefreshing(false);
                mLoadingMore = false;
                vLoadMore.setStatus(LoadMoreView.STATUS_FAIL);
                break;
            default:
                break;
        }
    }
}
