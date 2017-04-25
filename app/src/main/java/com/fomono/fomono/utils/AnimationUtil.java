package com.fomono.fomono.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by David on 4/25/2017.
 */

public class AnimationUtil {

    public static void playInteractionAnimation(View view) {
        ObjectAnimator expandX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.2f);
        ObjectAnimator expandY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.2f);
        AnimatorSet expand = new AnimatorSet();
        expand.setDuration(100);
        expand.setInterpolator(new AccelerateDecelerateInterpolator());
        expand.playTogether(expandX, expandY);

        ObjectAnimator revertX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f);
        ObjectAnimator revertY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f);
        AnimatorSet revert = new AnimatorSet();
        revert.setDuration(100);
        expand.setInterpolator(new AccelerateDecelerateInterpolator());
        revert.playTogether(revertX, revertY);

        AnimatorSet sequence = new AnimatorSet();
        sequence.playSequentially(expand, revert);
        sequence.start();
    }
}
