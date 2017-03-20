package stbvideocall.jhonelee.xyt.com.aini_app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import stbvideocall.jhonelee.xyt.com.aini_app.R;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.ActionType;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.QueryActionCreator;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.RxError;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.Module.ActivityModule;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.component.DaggerSearchActivityComonent;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankNormalItem;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.RxViewDispatch;
import stbvideocall.jhonelee.xyt.com.aini_app.sotres.RxStoreChange;
import stbvideocall.jhonelee.xyt.com.aini_app.sotres.SearchStore;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.adapter.SearchGankAdapter;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.widget.HeaderViewRecyclerAdapter;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.widget.LoadMoreView;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.widget.RecycleViewDivider;

import static stbvideocall.jhonelee.xyt.com.aini_app.AiniAppliction.getAppComponent;

/**
 * Created by Administrator on 2017/3/17.
 */

public class SearchActivity extends BaseActivity implements SearchGankAdapter.OnItemClickListener,RxViewDispatch,TextView.OnEditorActionListener,TextWatcher{
    @BindView(R.id.ed_search)
    AppCompatEditText edSearch;
    @BindView(R.id.iv_edit_clear)
    AppCompatImageView ivEditClear;
    @BindView(R.id.iv_search)
    AppCompatImageView ivSearch;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    protected LoadMoreView vLoadMore;

    protected LinearLayoutManager mLayoutManager;

    protected boolean mLoadingMore = false;

    protected HeaderViewRecyclerAdapter adapter;

    @Inject SearchGankAdapter mAdapter;
    @Inject SearchStore mStore;
    @Inject QueryActionCreator mActionCreator;
    @Inject Dispatcher mDispatcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initInjector();

        initView();

        edSearch.addTextChangedListener(this);
        mDispatcher.subscribeRxStore(mStore);
        mDispatcher.subscribeRxView(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mDispatcher.subscribeRxStore(mStore);
        mDispatcher.subscribeRxView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDispatcher.unsubscribeRxStore(mStore);
        mDispatcher.unsubscribeRxView(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initInjector() {
        DaggerSearchActivityComonent.builder()
                .appComponent(getAppComponent())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
    }

    public void initView(){
        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        refreshLayout.setRefreshing(false);
        refreshLayout.setEnabled(false);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(onScrollListener);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
        mAdapter = new SearchGankAdapter();
        mAdapter.setOnItemClickListener(this);

        vLoadMore = (LoadMoreView) LayoutInflater.from(this).inflate(R.layout.load_more,null);
        adapter = new HeaderViewRecyclerAdapter(mAdapter);
        adapter.setLoadingView(vLoadMore);
        recyclerView.setAdapter(mAdapter);
    }

    public static Intent newIntent(Activity context) {
        Intent intent = new Intent(context, SearchActivity.class);
        return intent;
    }

    @OnClick({R.id.iv_edit_clear, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_edit_clear:
                mAdapter.clearData();
                mLoadingMore = true;
                vLoadMore.setStatus(LoadMoreView.STATUS_INIT);
                edSearch.setText("");
                ivEditClear.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_search:
                queryGank(edSearch.getText().toString().trim(),1);
                refreshLayout.setRefreshing(true);
                break;
        }
    }

    public void queryGank(String querytxt,int page){
        if(!TextUtils.isEmpty(querytxt)){
            mActionCreator.query(querytxt,page);
        }else {
            Toast.makeText(this,"搜索内容不能为空！",Toast.LENGTH_SHORT).show();
        }
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener(){

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            boolean reachBottom = mLayoutManager.findLastCompletelyVisibleItemPosition()
                    >= mLayoutManager.getItemCount() - 1;
            if(!mLoadingMore && reachBottom) {
                mLoadingMore = true;
                vLoadMore.setStatus(LoadMoreView.STATUS_LOADING);
                queryGank(edSearch.getText().toString().trim(),mAdapter.getmCurPage() + 1);
            }
        }
    };

    @Override
    public void onClickNormalItem(View view, GankNormalItem normalItem) {
        if("福利".equals(normalItem.type)){
            startActivity(PictureActivity.newIntent(this,normalItem.url,normalItem.publishedAt));
        }else{
            WebviewActivity.openUrl(this, normalItem.url, normalItem.desc);
        }
    }
    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {

        switch (change.getStoreId()){
            case SearchStore.ID:
                refreshLayout.setRefreshing(false);
                if(mStore.getGankList() != null && mStore.getGankList().size() > 0){
                    mAdapter.updateData(mStore.getPage(),mStore.getGankList());
                    mLoadingMore = false;
                    vLoadMore.setStatus(LoadMoreView.STATUS_INIT);
                }else{
                    vLoadMore.setStatus(LoadMoreView.STATUS_NO_MORE);
                }
                break;
            default:
                break;

        }
    }
    @Override
    public void onRxError(@NonNull RxError error) {
        switch (error.getAction().getType()) {
            case ActionType.QUERY_GANK:
                mAdapter.clearData();
                mLoadingMore = false;
                vLoadMore.setStatus(LoadMoreView.STATUS_INIT);
                refreshLayout.setRefreshing(false);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            queryGank(edSearch.getText().toString().trim(),1);
            refreshLayout.setRefreshing(true);
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
    @Override
    public void afterTextChanged(Editable s) {
        if(s.length() > 0){
            ivEditClear.setVisibility(View.VISIBLE);
        }else{
            ivEditClear.setVisibility(View.INVISIBLE);
            mAdapter.clearData();
        }
    }
}
