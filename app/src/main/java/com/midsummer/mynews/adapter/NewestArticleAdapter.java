package com.midsummer.mynews.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.midsummer.mynews.R;
import com.midsummer.mynews.activities.ArticleDetailActivity;
import com.midsummer.mynews.model.Result;
import com.midsummer.mynews.model.SavedModel;

import org.ocpsoft.prettytime.PrettyTime;
import org.parceler.Parcels;

import java.util.List;

/**
 * Created by nienb on 12/2/16.
 */
public class NewestArticleAdapter extends UltimateViewAdapter<NewestArticleAdapter.ItemViewViewHolder> {
    private List<Result> mModel;
    private static final PrettyTime PT = new PrettyTime();
    private Context mContext;

    public NewestArticleAdapter(List<Result> mModel, Context mContext) {
        this.mModel = mModel;
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(final ItemViewViewHolder holder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= mModel.size() : position < mModel.size()) && (customHeaderView != null ? position > 0 : true)) {
            holder.mTitle.setText(mModel.get(customHeaderView != null ? position - 1 : position).webTitle);
            holder.mSubtitle.setText(mModel.get(customHeaderView != null ? position - 1 : position).fields.trailText.replaceAll("s/<(.*?)>//g",""));
            holder.mSection.setText(mModel.get(customHeaderView != null ? position - 1 : position).sectionName);
            holder.mPostDate.setText(PT.format(mModel.get(customHeaderView != null ? position - 1 : position).getPostDate()));
            Glide.with(mContext).load(SavedModel.extractImagelinkFromRawString(mModel.get(customHeaderView != null ? position - 1 : position).fields.main))
                    .placeholder(R.drawable.ic_broken_image)
                    .error(R.drawable.ic_broken_image)
                    .into(holder.mThumbnail);
            /*if (mDragStartListener != null) {
                holder.item_view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
            }*/

            holder.item_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Result model = getItem(position);
                    Intent i = new Intent(mContext, ArticleDetailActivity.class);
                    i.putExtra("article", Parcels.wrap(model));
                    mContext.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getAdapterItemCount() {
        return mModel.size();
    }

    @Override
    public ItemViewViewHolder getViewHolder(View view) {
        return new ItemViewViewHolder(view, false);
    }

    @Override
    public ItemViewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_newestarticle_item, parent, false);
        ItemViewViewHolder vh = new ItemViewViewHolder(v, true);
        return vh;
    }


    public void insert(List<Result> result, int position) {
        for (Result r: result
             ) {
            insert(mModel, r, position);
        }

    }

    public void remove(int position) {
        remove(mModel, position);
    }

    public void clear() {
        clear(mModel);
    }

    @Override
    public void toggleSelection(int pos) {
        super.toggleSelection(pos);
    }

    @Override
    public void setSelected(int pos) {
        super.setSelected(pos);
    }

    @Override
    public void clearSelection(int pos) {
        super.clearSelection(pos);
    }


    public void swapPositions(int from, int to) {
        swapPositions(mModel, from, to);
    }


    @Override
    public long generateHeaderId(int position) {
        /*if (getItem(position).length() > 0)
            return getItem(position).charAt(0);
        else return -1;*/
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.stick_header_item, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        swapPositions(fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
        super.onItemMove(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        remove(position);
        super.onItemDismiss(position);
    }

    public void setOnDragStartListener(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;

    }

    public class ItemViewViewHolder extends UltimateRecyclerviewViewHolder {
        TextView mTitle, mSubtitle, mSection, mPostDate;
        ImageView mThumbnail;
        View item_view;
        public  ItemViewViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                mTitle = (TextView) itemView.findViewById(
                        R.id.item_title);
                mSubtitle = (TextView) itemView.findViewById(
                        R.id.item_subtitle);
                mSection = (TextView) itemView.findViewById(
                        R.id.item_section);
                mPostDate = (TextView) itemView.findViewById(
                        R.id.item_postdate);
                mThumbnail = (ImageView) itemView.findViewById(R.id.item_thumbnail);
                item_view = itemView.findViewById(R.id.item_main);
            }

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public Result getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mModel.size())
            return mModel.get(position);
        else return null;
    }

}