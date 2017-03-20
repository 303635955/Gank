package stbvideocall.jhonelee.xyt.com.aini_app.ui.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import stbvideocall.jhonelee.xyt.com.aini_app.R;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankNormalItem;
import stbvideocall.jhonelee.xyt.com.aini_app.util.DateUtil;

/**
 * Created by Administrator on 2017/3/14.
 */

public class SearchGankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<GankNormalItem> mItems;

    private int mCurPage = 0;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClickNormalItem(View view, GankNormalItem normalItem);
    }

    @Inject
    public SearchGankAdapter(){}

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

    public void clearData() {
        mItems = null;
        notifyDataSetChanged();
    }

    public int getmCurPage() {
        return mCurPage;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHodler(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof NormalViewHodler){
            NormalViewHodler normalViewHodler = (NormalViewHodler) holder;
            final GankNormalItem gankNormalItem = mItems.get(position);
            normalViewHodler.tvItemTitleSearch.setText(gankNormalItem.desc == null ? "unkonwn" : gankNormalItem.desc);
            normalViewHodler.tvItemTypeSearch.setText(gankNormalItem.type == null ? "unkonwn" : gankNormalItem.type);
            normalViewHodler.tvItemPublisherSearch.setText(gankNormalItem.who == null ? "unkonwn" : gankNormalItem.who);
            normalViewHodler.tvItemTimeSearch.setText(DateUtil.dateFormat(gankNormalItem.publishedAt));

            normalViewHodler.llItemSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClickNormalItem(v,gankNormalItem);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public static class NormalViewHodler extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_title_search)
        AppCompatTextView tvItemTitleSearch;
        @BindView(R.id.tv_item_type_search)
        AppCompatTextView tvItemTypeSearch;
        @BindView(R.id.tv_item_publisher_search)
        AppCompatTextView tvItemPublisherSearch;
        @BindView(R.id.tv_item_time_search)
        AppCompatTextView tvItemTimeSearch;
        @BindView(R.id.ll_item_search)
        LinearLayout llItemSearch;

        public NormalViewHodler(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_search, parent, false));
            ButterKnife.bind(this,itemView);

        }
    }
}
