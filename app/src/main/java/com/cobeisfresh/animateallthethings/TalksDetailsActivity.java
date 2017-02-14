package com.cobeisfresh.animateallthethings;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TalksDetailsActivity extends AppCompatActivity {

    public static final String BUNDLE_ITEM = "bundle_item";
    public static final String BUNDLE_NAME = "bundle_name";

    @BindView(R.id.root_view)
    ViewGroup root;

    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.title_container)
    ViewGroup titleContainer;

    @BindView(R.id.date_container)
    ViewGroup dateContainer;

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.fab)
    ImageView fab;

    private boolean isCollapsedScene;
    private boolean isCollapsedView;

    private Scene originalScene;
    private Scene collapsedScene;
    private TransitionManager transitionManager;
    private int defDateHeight;
    private int defTitleHeight;

    public static Intent getLaunchIntent(Context from, TalksModel model) {
        Intent intent = new Intent(from, TalksDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_ITEM, model);
        intent.putExtra(BUNDLE_NAME, bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupTransitions();

        setContentView(R.layout.details_activity);
        ButterKnife.bind(this);

        setupScenes();

        loadData();
    }

    private void loadData() {
        TalksModel model = (TalksModel) getIntent().getBundleExtra(BUNDLE_NAME).getSerializable(BUNDLE_ITEM);

        if (model != null) {
            ImageLoader.loadImage(image, model.getImage());

            title.setText(model.getTitle());
            description.setText(model.getDescription());
            date.setText(model.getDate());
        }
    }

    @OnClick(R.id.image)
    protected void imageClicked() {
        if (isCollapsedView) {
            expandViews();
        } else {
            collapseViews();
        }
    }

    @OnClick(R.id.description)
    protected void descriptionClicked() {
        if (isCollapsedScene) {
            transitionManager.transitionTo(originalScene);
        } else {
            transitionManager.transitionTo(collapsedScene);
        }
    }

    @OnClick(R.id.fab)
    protected void fabClicked() {
        startRevealActivity();
    }

    @OnClick({R.id.title_container, R.id.date_container})
    protected void titleClicked() {
        niceAnimateAll();
    }

    private void setupTransitions() {

        Fade slide = (new Fade());
        slide.setDuration(300);

        getWindow().setEnterTransition(slide);
        getWindow().setExitTransition(slide);

        getWindow().setSharedElementsUseOverlay(false);

    }

    private void niceAnimateAll() {
        //fab animator

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(fab, "scaleX", 0, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(fab, "scaleY", 0, 1);
        AnimatorSet fabAnimator = new AnimatorSet();
        fabAnimator.playTogether(scaleX, scaleY);

        //title date animator

        int titleStart = titleContainer.getTop();
        int titleEnd = titleContainer.getBottom();
        ObjectAnimator titleAnimator = ObjectAnimator.ofInt(titleContainer, "bottom", titleStart, titleEnd);

        titleAnimator.setInterpolator(new AccelerateInterpolator());

        int dateTop = dateContainer.getTop();
        int dateBottom = dateContainer.getBottom();
        ObjectAnimator dateAnimator = ObjectAnimator.ofInt(dateContainer, "bottom", dateTop, dateBottom);

        dateAnimator.setInterpolator(new DecelerateInterpolator());

        titleContainer.setBottom(titleStart);
        dateContainer.setBottom(titleStart);

        AnimatorSet textAnimator = new AnimatorSet();
        textAnimator.playSequentially(titleAnimator, dateAnimator);

        AnimatorSet allAnimations = new AnimatorSet();
        allAnimations.playTogether(textAnimator, fabAnimator);
        allAnimations.start();
    }

    private void setupScenes() {

        originalScene = Scene.getSceneForLayout(root, R.layout.details_activity, this);
        collapsedScene = Scene.getSceneForLayout(root, R.layout.details_activity_short, this);

        originalScene.setEnterAction(new Runnable() {
            @Override
            public void run() {
                ButterKnife.bind(TalksDetailsActivity.this);
                loadData();
                isCollapsedScene = false;
            }
        });

        collapsedScene.setEnterAction(new Runnable() {
            @Override
            public void run() {
                ButterKnife.bind(TalksDetailsActivity.this);
                loadData();
                isCollapsedScene = true;
            }
        });

        Transition sceneTransition = TransitionInflater.from(this).inflateTransition(R.transition.changebounds);

        transitionManager = new TransitionManager();

        transitionManager.setTransition(originalScene, collapsedScene, sceneTransition);
        transitionManager.setTransition(collapsedScene, originalScene, sceneTransition);

        originalScene.enter();
    }

    private void expandViews() {
        ValueAnimator titleAnim = ValueAnimator.ofInt(0, defTitleHeight);
        titleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                titleContainer.getLayoutParams().height = (int) animation.getAnimatedValue();
                titleContainer.requestLayout();
            }
        });
        titleAnim.setDuration(500);

        ValueAnimator dataAnim = ValueAnimator.ofInt(0, defDateHeight);
        dataAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dateContainer.getLayoutParams().height = (int) animation.getAnimatedValue();
                dateContainer.requestLayout();
            }
        });
        dataAnim.setDuration(500);

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(titleAnim, dataAnim);
        set.start();

        isCollapsedView = false;
    }

    private void collapseViews() {
        defDateHeight = dateContainer.getMeasuredHeight();
        defTitleHeight = titleContainer.getMeasuredHeight();

        ValueAnimator dateAnimator = ValueAnimator.ofInt(defDateHeight, 0);
        dateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dateContainer.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                dateContainer.requestLayout();
            }
        });
        dateAnimator.setDuration(500);

        ValueAnimator titleAnimator = ValueAnimator.ofInt(defTitleHeight, 0);
        titleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                titleContainer.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                titleContainer.requestLayout();
            }
        });
        titleAnimator.setDuration(500);

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(dateAnimator, titleAnimator);
        set.start();

        isCollapsedView = true;
    }

    private void startRevealActivity() {
        Intent intent = RevealActivity.getLaunchIntent(this);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fab, getString(R.string.element_transition));

        startActivity(intent, options.toBundle());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAfterTransition();
    }
}