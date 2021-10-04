package com.i9930.croptrails.Utility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Build;
import android.transition.Transition;
import android.view.View;
import android.view.ViewAnimationUtils;

import androidx.annotation.RequiresApi;

public class AnimationConstants {

    Activity activity;
     Transition.TransitionListener mEnterTransitionListener;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public AnimationConstants(Activity activity) {
        this.activity = activity;
        mEnterTransitionListener = new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {

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
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public    void exitReveal(View get_otp_tv) {
        int cx = get_otp_tv.getMeasuredWidth() / 2;
        int cy = get_otp_tv.getMeasuredHeight() / 2;
        // get the initial radius for the clipping circle
        int initialRadius = get_otp_tv.getWidth() / 2;

        // create the animation (the final radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(get_otp_tv, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                get_otp_tv.setVisibility(View.INVISIBLE);
            }
        });

        // start the animation
        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public  void enterReveal(View get_otp_tv) {
        int cx = get_otp_tv.getMeasuredWidth() / 2;
        int cy = get_otp_tv.getMeasuredHeight() / 2;
        int finalRadius = Math.max(get_otp_tv.getWidth(), get_otp_tv.getHeight()) / 2;
        Animator anim = ViewAnimationUtils.createCircularReveal(get_otp_tv, cx, cy, 0, finalRadius);
        get_otp_tv.setVisibility(View.VISIBLE);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                activity.getWindow().getEnterTransition().removeListener(mEnterTransitionListener);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

}
