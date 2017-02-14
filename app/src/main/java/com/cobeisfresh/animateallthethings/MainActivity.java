package com.cobeisfresh.animateallthethings;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Pair;
import android.view.View;
import android.view.animation.AnticipateInterpolator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ItemClickListener {

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupTransitions();

        ButterKnife.bind(this);

        TalksAdapter adapter = new TalksAdapter();
        adapter.setClickListener(this);
        recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recycler.setAdapter(adapter);

        List<TalksModel> list = getTalksList();
        adapter.setData(list);
    }

    private void setupTransitions() {

        Explode explode = new Explode();
        explode.setDuration(300);
        explode.setInterpolator(new AnticipateInterpolator());
        getWindow().setExitTransition(explode);

        Transition slide = TransitionInflater.from(this).inflateTransition(R.transition.slide);
        slide.setDuration(300);
        getWindow().setReenterTransition(slide);
    }

    @Override
    public void itemClicked(View view, TalksModel model) {
        Intent intent = TalksDetailsActivity.getLaunchIntent(this, model);

        String transName = getString(R.string.transition_name);
        Pair<View, String> myPair = Pair.create(view, transName);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, myPair);

        // in case of only one shared view, this works as well
//        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, view, transName);

        startActivity(intent, options.toBundle());
    }

    private List<TalksModel> getTalksList() {
        List<TalksModel> list = new ArrayList<>();
        list.add(new TalksModel("http://www.drodd.com/images15/1-12.jpg", "Android Talks", getString(R.string.description_one), "20.01.2016."));
        list.add(new TalksModel("http://image.tsn.ua/media/images2/original/Aug2011/383466237.jpg", "Data Binding", getString(R.string.description_two), "19.02.2016."));
        list.add(new TalksModel("http://frontierpsychiatrist.co.uk/wp-content/uploads/2009/09/3.jpg", "Prvi spoj s Kotlinom", getString(R.string.description_three), "18.03.2016."));
        list.add(new TalksModel("http://mysticalnumbers.com/wp-content/uploads/2012/06/Number-4-on-fire.png", "Let me explain you... Fragments", getString(R.string.description_four), "19.05.2016."));
        list.add(new TalksModel("http://gradcontent.com/lib/600x350/number_5.jpg", "Google Firebase 2.0", getString(R.string.description_five), "12.07.2016."));
        list.add(new TalksModel("http://media.gettyimages.com/videos/digital-count-up-110-video-id472610073?s=640x640", "ButterKnife", getString(R.string.description_six), "22.09.2016."));
        list.add(new TalksModel("https://pbs.twimg.com/profile_images/633211635822268416/LUbjbFsH.png", "Dagger 2 + OkHttp", getString(R.string.description_seven), "06.12.2016."));
        list.add(new TalksModel("https://cdn.pixabay.com/photo/2013/08/06/22/55/number-8-170382_960_720.jpg", "Animations and adapters", getString(R.string.description_eight), "09.02.2016."));
        return list;
    }
}
