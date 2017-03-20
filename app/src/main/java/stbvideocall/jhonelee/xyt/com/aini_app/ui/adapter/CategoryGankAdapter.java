package stbvideocall.jhonelee.xyt.com.aini_app.ui.adapter;

import android.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import stbvideocall.jhonelee.xyt.com.aini_app.R;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankNormalItem;
import stbvideocall.jhonelee.xyt.com.aini_app.util.AppUtil;
import stbvideocall.jhonelee.xyt.com.aini_app.util.DateUtil;

/**
 * Created by Administrator on 2017/3/14.
 */

public class CategoryGankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GankNormalItem> mItems;
    private int mCurPage = 0;
    private OnItemClickListener onItemClickListener;
    private Fragment fragment;

    public CategoryGankAdapter(Fragment fragment){
        this.fragment = fragment;
    }

    public interface OnItemClickListener {
        void onClickNormalItem(View view, GankNormalItem normalItem);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void updateData(int page, List<GankNormalItem> list) {
        if (null == list || list.size() == 0) {
            return;
        }
        if (page - mCurPage > 1) {
            return;
        }
        if (page <= 1) {
            if (mItems == null || !mItems.containsAll(list)) {
                mItems = list;
                notifyDataSetChanged();
                mCurPage = page;
            }
        } else if (1 == page - mCurPage && null != mItems) {
            int size = mItems.size();
            mItems.addAll(size, list);
            notifyItemRangeInserted(size, list.size());
            mCurPage = page;
        }
    }

    public int getmCurPage() {
        return mCurPage;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
            final GankNormalItem normalItem = mItems.get(position);

            if (normalItem.images != null && normalItem.images.size() > 0) {
                Glide.with(fragment)
                        .load(normalItem.images.get(0)+"?imageView2/0/w/190")
                        .centerCrop()
                        .placeholder(R.color.imageColorPlaceholder)
                        .into(normalViewHolder.ivItemImg);
            }

            normalViewHolder.tvItemTitle.setText(normalItem.desc == null ? "unknown" : normalItem.desc);
            normalViewHolder.tvItemPublisher.setText(normalItem.who == null ? "unknown" : normalItem.who);
            normalViewHolder.tvItemTime.setText(DateUtil.dateFormat(normalItem.publishedAt));
            normalViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClickNormalItem(v, normalItem);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null == mItems ? 0 : mItems.size();
    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.iv_item_img)
        ImageView ivItemImg;
        @BindView(R.id.tv_item_title)
        AppCompatTextView tvItemTitle;
        @BindView(R.id.tv_item_publisher)
        AppCompatTextView tvItemPublisher;
        @BindView(R.id.tv_item_time)
        AppCompatTextView tvItemTime;
        @BindView(R.id.ll_item)
        LinearLayout llItem;

        public NormalViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_gank, parent, false));
            ButterKnife.bind(this, itemView);
        }
    }
}
