package com.cobeisfresh.animateallthethings;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marijan on 06/02/2017.
 */
public class TalksAdapter extends RecyclerView.Adapter {

    private List<TalksModel> data = new ArrayList<>();
    private ItemClickListener clickListener;

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setData(List<TalksModel> data) {
        this.data.clear();
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new TalksHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TalksHolder talksHolder = (TalksHolder) holder;
        talksHolder.setListener(clickListener);
        talksHolder.setData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
