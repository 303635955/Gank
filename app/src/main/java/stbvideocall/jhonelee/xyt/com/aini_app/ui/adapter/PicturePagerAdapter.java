package stbvideocall.jhonelee.xyt.com.aini_app.ui.adapter;

import android.support.annotation.IntDef;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import stbvideocall.jhonelee.xyt.com.aini_app.R;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankNormalItem;

/**
 * Created by Administrator on 2017/3/15.
 */

public class PicturePagerAdapter extends PagerAdapter{

    public static final int ADD_FRONT = -1;
    public static final int ADD_END = 1;
    public static final int ADD_NONE = 0;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ADD_FRONT, ADD_END, ADD_NONE})
    public @interface ADD_STATUS{}

    private List<GankNormalItem> mItems;

    @Inject
    public PicturePagerAdapter(){}

    public void initList(List<GankNormalItem> mItems){
        this.mItems = mItems;
    }

    public @ADD_STATUS int oppendList(int page,List<GankNormalItem> list){

        if(0 == getCount()){
            return ADD_NONE;
        }
        if(page == mItems.get(0).page -1){
            mItems.addAll(0,list);
            return ADD_FRONT;
        }
        if(page == mItems.get(mItems.size() - 1).page +1){
            mItems.addAll(mItems.size(),list);
            return ADD_END;
        }
        return ADD_NONE;
    }

    @Override
    public int getCount() {
        return (mItems == null? 0 : mItems.size());
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.pager_item_picture,container,false);
        ViewHodler viewHodler = new ViewHodler(view);
        GankNormalItem item = mItems.get(position);
        Glide.with(container.getContext())
                .load(item.url)
                .dontAnimate()
                .centerCrop()
                .into(viewHodler.vPic);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(object instanceof View){
            container.removeView((View) object);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public GankNormalItem getItem(int pos) {
        if(pos < 0 || pos >= getCount()) {
            return null;
        }
        return mItems.get(pos);
    }

    public static class ViewHodler{
        @BindView(R.id.pic)
        ImageView vPic;
        public ViewHodler(View viewItem){
            ButterKnife.bind(this,viewItem);
        }
    }
}
