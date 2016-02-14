package com.midsummer.mynews.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.midsummer.mynews.R;
import com.midsummer.mynews.activities.TopicListActivity;
import com.midsummer.mynews.model.topic.Result;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by nienb on 13/2/16.
 */
public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ItemViewHolder>{
    public Context mContext;
    public List<Result> mModel;

    public List<Result> tmps = new ArrayList<>();

    public TopicAdapter(Context mContext, List<Result> mModel) {
        this.mContext = mContext;
        this.mModel = mModel;
        tmps.addAll(mModel);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_topic_item, null);
        ItemViewHolder rcv = new ItemViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.mTopicName.setText(mModel.get(position).getWebTitle());
        Random r = new Random();
        int w = r.nextInt(200) + 400;
        int h = r.nextInt(200) + 400;
        Glide.with(mContext).load("http://lorempixel.com/"+w+"/"+h+"/")
                .error(R.drawable.ic_broken_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.mTopicPhoto);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.midsummer.mynews.model.topic.Result model = mModel.get(position);
                Intent i = new Intent(mContext, TopicListActivity.class);
                i.putExtra("topic", Parcels.wrap(model));
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mModel.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView mTopicName;
        public ImageView mTopicPhoto;
        public View itemView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTopicName = (TextView)itemView.findViewById(R.id.country_name);
            mTopicPhoto = (ImageView)itemView.findViewById(R.id.country_photo);
            this.itemView = itemView;

        }


    }

    public void filter(String query){
        mModel.clear();
        if (query == ""){
            mModel.addAll(tmps);
        }else{
            for (Result result: tmps
                 ) {
                if (result.getWebTitle().toLowerCase().contains(query.toLowerCase()))
                    mModel.add(result);
            }
        }
        notifyDataSetChanged();
    }
}
