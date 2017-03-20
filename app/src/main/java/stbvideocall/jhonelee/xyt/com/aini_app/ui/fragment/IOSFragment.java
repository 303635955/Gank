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
import stbvideocall.jhonelee.xyt.com.aini_app.actions.IOSGankActionCreator;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.RxError;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.component.DaggerIOSFragmentComonent;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.component.IOSFragmentComonent;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankNormalItem;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;
import stbvideocall.jhonelee.xyt.com.aini_app.sotres.IOSStore;
import stbvideocall.jhonelee.xyt.com.aini_app.sotres.RxStoreChange;
import stbvideocall.jhonelee.xyt.com.aini_app.stat.StatName;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.activity.WebviewActivity;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.adapter.CategoryGankAdapter;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.widget.LoadMoreView;

public class IOSFragment extends CategoryGankFragment {

    public static final String TAG = IOSFragment.class.getSimpleName();

    @Inject IOSStore mStore;
    @Inject IOSGankActionCreator mActionCreator;
    @Inject Dispatcher mDispatcher;

    private IOSFragmentComonent component;
    public static IOSFragment newInstance() {
        return new IOSFragment();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDispatcher.unsubscribeRxStore(mStore);
        mDispatcher.unsubscribeRxView(this);
    }

    private void initInjector() {
        component= DaggerIOSFragmentComonent.builder()
                .appComponent(AiniAppliction.getAppComponent())
                .build();
        component.inject(this);
    }

    @Override
    protected void refreshList() {
        mActionCreator.getIOSList(1);
    }

    @Override
    protected void loadMore() {
        vLoadMore.setStatus(LoadMoreView.STATUS_LOADING);
        mActionCreator.getIOSList(mAdapter.getmCurPage() + 1);
    }

    @Override
    protected String getStatPageName() {
        return StatName.PAGE_IOS;
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()){
            case IOSStore.ID:
                if(1 == mStore.getPage()){
                    refreshLayout.setRefreshing(false);
                }
                mAdapter.updateData(mStore.getPage(),mStore.getmGankList());
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
            case ActionType.GET_ANDROID_LIST:
                refreshLayout.setRefreshing(false);
                mLoadingMore = false;
                vLoadMore.setStatus(LoadMoreView.STATUS_FAIL);
                break;
            default:
                break;
        }
    }
}
