package stbvideocall.jhonelee.xyt.com.aini_app.ui.adapter;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import stbvideocall.jhonelee.xyt.com.aini_app.R;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankNormalItem;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.widget.RatioImageView;
import stbvideocall.jhonelee.xyt.com.aini_app.util.AppUtil;

/**
 * Created by Administrator on 2017/3/14.
 */

public class WelfareGankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Fragment mFragment;
    private List<GankNormalItem> mItems;

    private int mCurPage = 0;

    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onClickItem(View view, GankNormalItem item);
    }

    public WelfareGankAdapter(Fragment fragment) {
        mFragment = fragment;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
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

    public int getCurPage() {
        return mCurPage;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_welfare, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if(null != tag && tag instanceof Integer) {
                    int position = (Integer) tag;
                    if(null != mItemClickListener) {
                        mItemClickListener.onClickItem(v, mItems.get(position));
                    }
                }
            }
        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        holder.itemView.setTag(position);
        GankNormalItem welfare = mItems.get(position);
        Glide.with(mFragment)
                .load(welfare.url)
                .centerCrop()
                .placeholder(R.color.imageColorPlaceholder)
                .into(vh.girlImage);


    }

    @Override
    public int getItemCount() {
        return null == mItems ? 0 : mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.girl_image)
        RatioImageView girlImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            girlImage.setRatio(0.618f);

        }
    }
}
