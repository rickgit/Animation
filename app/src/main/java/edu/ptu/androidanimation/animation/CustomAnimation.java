package edu.ptu.androidanimation.animation;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by WangAnshu on 2016/3/15.
 */
public class CustomAnimation extends Animation {
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
    }
    /**
     * Gets the transformation to apply at a specified point in time. Implementations of this
     * method should always replace the specified Transformation or document they are doing
     * otherwise.
     *
     * @param currentTime Where we are in the animation. This is wall clock time.
     * @param outTransformation A transformation object that is provided by the
     *        caller and will be filled in by the animation.
     * @return True if the animation is still running
     */
    @Override
    public boolean getTransformation(long currentTime, Transformation outTransformation) {
        Log.w("CustomAnimation","getTransformation");
        return super.getTransformation(currentTime, outTransformation);
    }

    @Override
    public boolean getTransformation(long currentTime, Transformation outTransformation, float scale) {
        return super.getTransformation(currentTime, outTransformation, scale);
    }
    /**
     * Convenience method to start the animation the first time
     * {@link #getTransformation(long, Transformation)} is invoked.
     */
    @Override
    public void start() {
        Log.w("CustomAnimation","start");
        super.start();
    }
}
