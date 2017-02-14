package com.cobeisfresh.animateallthethings;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Marijan on 06/02/2017.
 */
public class TalksHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_image)
    ImageView imageView;

    @BindString(R.string.transition_name)
    String trans;

    private TalksModel talk;
    private ItemClickListener listener;

    public TalksHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(TalksModel talk) {
        this.talk = talk;
        ImageLoader.loadImage(imageView, talk.getImage());
    }

    @OnClick(R.id.item_image)
    protected void itemClicked() {
        imageView.setTransitionName(trans);
        if (listener != null) {
            listener.itemClicked(imageView, talk);
        }
    }
}
