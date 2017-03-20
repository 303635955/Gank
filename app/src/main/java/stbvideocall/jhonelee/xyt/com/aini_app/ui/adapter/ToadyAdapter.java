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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import stbvideocall.jhonelee.xyt.com.aini_app.R;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankGirlImageItem;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankHeaderItem;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankItem;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankNormalItem;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.widget.RatioImageView;
import stbvideocall.jhonelee.xyt.com.aini_app.util.AppUtil;

/**
 * Created by Administrator on 2017/3/3.
 */

public class ToadyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_HEADER = 2;
    private static final int VIEW_TYPE_GIRL_IMAGE = 3;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClickNormalItem(View view, GankNormalItem normalItem);

        void onClickGirlItem(View view, GankGirlImageItem girlImageItem);

        void onClickSearchItem();
    }

    private Fragment fragment;
    private List<GankItem> list;

    public ToadyAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void swapData(List<GankItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new CategoryHeaderViewHolder(parent);
            case VIEW_TYPE_NORMAL:
                return new NormalViewHolder(parent);
            case VIEW_TYPE_GIRL_IMAGE:
                return new GirlImageViewHolder(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof CategoryHeaderViewHolder) {
            CategoryHeaderViewHolder headerHolder = (CategoryHeaderViewHolder) holder;
            headerHolder.categoryTitle.setText(((GankHeaderItem) list.get(position)).name);
            return;
        }
        if (holder instanceof NormalViewHolder) {
            NormalViewHolder normalHolder = (NormalViewHolder) holder;
            final GankNormalItem normalItem = (GankNormalItem) list.get(position);
            normalHolder.title.setText(getGankTitleStr(normalItem.desc, normalItem.who));
            normalHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClickNormalItem(v, normalItem);
                }
            });
            return;
        }
        if (holder instanceof GirlImageViewHolder) {
            GirlImageViewHolder girlHolder = (GirlImageViewHolder) holder;
            final GankGirlImageItem girlItem = (GankGirlImageItem) list.get(position);
            Glide.with(fragment)
                    .load(girlItem.imgUrl)
                    .placeholder(R.color.imageColorPlaceholder)
                    .centerCrop()
                    .into(girlHolder.girl_image);
            girlHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClickGirlItem(v, girlItem);
                }
            });

            girlHolder.llHomeSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClickSearchItem();
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        GankItem gankItem = list.get(position);
        if (gankItem instanceof GankHeaderItem) {
            return VIEW_TYPE_HEADER;
        }
        if (gankItem instanceof GankGirlImageItem) {
            return VIEW_TYPE_GIRL_IMAGE;
        }
        return VIEW_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    private CharSequence getGankTitleStr(String desc, String who) {
        if (TextUtils.isEmpty(who)) {
            return desc;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(desc);
        SpannableString spannableString = new SpannableString(" (" + who + ")");
        spannableString.setSpan(new TextAppearanceSpan(AppUtil.getAppContext(), R.style.SummaryTextAppearance), 0, spannableString.length(), 0);
        builder.append(spannableString);
        return builder;
    }

    public static class CategoryHeaderViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.category_title)
        TextView categoryTitle;

        public CategoryHeaderViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_category_title, parent, false));
            ButterKnife.bind(this, itemView);
        }
    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        public NormalViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_todaygank, parent, false));
            ButterKnife.bind(this, itemView);
        }
    }

    public static class GirlImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.girl_image)
        RatioImageView girl_image;
        @BindView(R.id.ll_home_search)
        LinearLayout llHomeSearch;

        public GirlImageViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_girl_imge, parent, false));
            ButterKnife.bind(this, itemView);
            girl_image.setRatio(1.718f);
        }
    }


}
