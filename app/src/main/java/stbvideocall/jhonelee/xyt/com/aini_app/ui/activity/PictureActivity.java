package stbvideocall.jhonelee.xyt.com.aini_app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import stbvideocall.jhonelee.xyt.com.aini_app.R;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.ActionType;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.PicTureGankActionCreator;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.RxError;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.Module.ActivityModule;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.component.DaggerPictureActivityComonent;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankNormalItem;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.RxViewDispatch;
import stbvideocall.jhonelee.xyt.com.aini_app.sotres.PictureStore;
import stbvideocall.jhonelee.xyt.com.aini_app.sotres.RxStoreChange;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.adapter.PicturePagerAdapter;

import static stbvideocall.jhonelee.xyt.com.aini_app.AiniAppliction.getAppComponent;

/**
 * Created by Administrator on 2017/3/15.
 */

public class PictureActivity extends BaseActivity implements RxViewDispatch{

    private static final String EXTRA_URL_SINGLE_PIC = "url_single_pic";
    private static final String EXTRA_PUBLISH_SINGLE_PIC = "publish_single_pic";
    private static final String EXTRA_PAGE_INDEX = "page_index";
    private static final String EXTRA_PIC_ID = "pic_id";

    private static final SimpleDateFormat sDateFormatter = new SimpleDateFormat("yyyy-MM-dd",
            Locale.getDefault());

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Inject PicturePagerAdapter picturePagerAdapter;
    @Inject PictureStore mStore;
    @Inject PicTureGankActionCreator mActionCreator;
    @Inject Dispatcher mDispatcher;

    private String mInitPicId;

    public static Intent newIntent(Context context, String url, Date publishAt){
        Intent intent = new Intent(context,PictureActivity.class);
        intent.putExtra(EXTRA_URL_SINGLE_PIC,url);
        intent.putExtra(EXTRA_PUBLISH_SINGLE_PIC,publishAt);
        return intent;
    }

    public static Intent newIntent(Context context, int pageIndex, String picId){
        Intent intent = new Intent(context,PictureActivity.class);
        intent.putExtra(EXTRA_PAGE_INDEX,pageIndex);
        intent.putExtra(EXTRA_PIC_ID,picId);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initInjector();
        initData();
        viewPager.setAdapter(picturePagerAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);

        mDispatcher.subscribeRxStore(mStore);
        mDispatcher.subscribeRxView(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        // if unsubscribe is in onDestroy, this activity's onDestroy may delay to new activity's onCreate
        // it will cause that storeChange event can't receive in new activity
        mDispatcher.unsubscribeRxStore(mStore);
        mDispatcher.unsubscribeRxView(this);
    }

    private void initInjector() {
        DaggerPictureActivityComonent.builder()
                .appComponent(getAppComponent())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
    }

    public void initData(){
        Intent intent = getIntent();
        if(null == intent) return;
        String singlePicUrl = intent.getStringExtra(EXTRA_URL_SINGLE_PIC);
        if(!TextUtils.isEmpty(singlePicUrl)){
            Date publishAt = (Date) intent.getSerializableExtra(EXTRA_PUBLISH_SINGLE_PIC);
            List<GankNormalItem> list = new ArrayList<>(1);
            GankNormalItem item = new GankNormalItem();
            item.url = singlePicUrl;
            item.publishedAt = publishAt;
            list.add(item);

            picturePagerAdapter.initList(list);
            setTitle(sDateFormatter.format(publishAt));
        } else {
            int pageIndex = intent.getIntExtra(EXTRA_PAGE_INDEX, -1);
            String picId = intent.getStringExtra(EXTRA_PIC_ID);
            if(-1 != pageIndex) {
                mInitPicId = picId;
                loadPictureList(pageIndex);
            }
        }
    }

    private void loadPictureList(int page) {
        mActionCreator.getPictureList(page);
    }

    private int getInitPicPos(List<GankNormalItem> list) {
        int size = list.size();
        for(int i = 0; i < size; i ++) {
            if(TextUtils.equals(list.get(i)._id, mInitPicId)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {

        switch (change.getStoreId()){
            case PictureStore.ID:
                if(picturePagerAdapter.getCount() == 0){
                    picturePagerAdapter.initList(mStore.getGankList());
                    picturePagerAdapter.notifyDataSetChanged();
                    int initPos = getInitPicPos(mStore.getGankList());
                    viewPager.setCurrentItem(initPos,false);
                    if(initPos == 0 ){
                        onPageChangeListener.onPageSelected(0);
                    }
                }else{
                    int addStatus = picturePagerAdapter.oppendList(mStore.getPage(),mStore.getGankList());
                    picturePagerAdapter.notifyDataSetChanged();
                    if(addStatus == PicturePagerAdapter.ADD_FRONT){
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + mStore.getGankList().size(), false);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRxError(@NonNull RxError error) {
        switch (error.getAction().getType()) {
            case ActionType.GET_PICTURE_LIST:
                // TODO
                break;
            default:
                break;
        }
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            GankNormalItem item = picturePagerAdapter.getItem(position);

            if(null != item.publishedAt){
                setTitle(sDateFormatter.format(item.publishedAt));
            }

            if(position == 0){
                if (item.page > 1){
                    loadPictureList(item.page -1);
                }
                return;
            }

            if (position == picturePagerAdapter.getCount() - 1 ){
                loadPictureList(item.page + 1);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
}
