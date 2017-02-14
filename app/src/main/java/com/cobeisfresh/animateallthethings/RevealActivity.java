package com.cobeisfresh.animateallthethings;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RevealActivity extends AppCompatActivity {

    @BindView(R.id.card_view)
    CardView cardView;

    @BindView(R.id.fab)
    ImageView fab;

    public static Intent getLaunchIntent(Context from) {
        return new Intent(from, RevealActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reveal_activity);
        ButterKnife.bind(this);

        setupTransitions();
    }

    private void setupTransitions() {

        TransitionSet enterSet = new TransitionSet();
        TransitionInflater inflater = TransitionInflater.from(this);

        Transition arc = inflater.inflateTransition(R.transition.arc_transition);

        enterSet.addTransition(arc);
        enterSet.setDuration(500);

        enterSet.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                revealView(cardView);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });

        TransitionSet exitSet = new TransitionSet();

        exitSet.addTransition(arc);
        exitSet.setDuration(500);

        getWindow().setSharedElementEnterTransition(enterSet);
        getWindow().setSharedElementReenterTransition(enterSet);
        getWindow().setSharedElementReturnTransition(exitSet);
        getWindow().setSharedElementExitTransition(exitSet);

        Slide slide = new Slide(Gravity.TOP);

        slide.excludeTarget(android.R.id.statusBarBackground, true);
        slide.excludeTarget(android.R.id.statusBarBackground, true);
        slide.excludeTarget(android.R.id.navigationBarBackground, true);
        slide.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow().setEnterTransition(slide);
        getWindow().setExitTransition(slide);
        getWindow().setReenterTransition(slide);
        getWindow().setReturnTransition(slide);

        getWindow().setAllowReturnTransitionOverlap(true);

    }

    public void revealView(View view) {

        int centerY = view.getMeasuredHeight() / 2;
        int centerX = view.getMeasuredWidth() / 2;

        Animator animator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, 0, view.getWidth());

        animator.setDuration(300);

        view.setVisibility(View.VISIBLE);
        animator.start();

    }

    public void hideView(View view) {

        int centerY = view.getMeasuredHeight() / 2;
        int centerX = view.getMeasuredWidth() / 2;

        Animator animator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, view.getHeight(), 0);

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                cardView.setVisibility(View.INVISIBLE);
                supportFinishAfterTransition();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(400);
        animator.start();

    }

    @Override
    public void onBackPressed() {
        hideView(cardView);
    }
}
