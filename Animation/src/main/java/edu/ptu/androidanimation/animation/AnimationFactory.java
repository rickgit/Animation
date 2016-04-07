package edu.ptu.androidanimation.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 抽象工厂
 * Created by WangAnshu on 16/2/27.
 */
public class AnimationFactory {
    //视图动画
    public Animation createViewAnimation(){
//        AnimationUtils.loadAnimation()
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(provideAngleAnimation());
        animationSet.addAnimation(provideTransparencyAnimation());
        animationSet.addAnimation(providePositionAnimation());
        animationSet.addAnimation(provideSizeAnimation());

        animationSet.setDuration(5000);
        return animationSet;
    };
    //Drawable动画
    public AnimationDrawable createAnimationDrawable(Context context){
        AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.setOneShot(false);
        animationDrawable.addFrame(context.getResources().getDrawable(android.R.drawable.ic_delete), 250);
        animationDrawable.addFrame(context.getResources().getDrawable(android.R.drawable.ic_media_rew),250);
//        animationDrawable.addFrame(new ColorDrawable(0xccccad),250);
//        animationDrawable.addFrame(new ColorDrawable(0xccccae), 250);
//        animationDrawable.addFrame(new ColorDrawable(0xccccaf), 250);
        return animationDrawable;
    }
    /**
     * android 3.0,api 11
     */
    public Animator createPropertyAnimation(Object obj,String property){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(5000);
        animatorSet.play(provideObjectAnimator(obj,property));
        return animatorSet;
    }
    ///视图动画
    ///position, size, rotation, and transparency
    private Animation providePositionAnimation(){
        return new TranslateAnimation(0,100,0,100);
    }
    private Animation provideSizeAnimation(){
        return new ScaleAnimation(0,10f,0,10f);
    }
    private Animation provideAngleAnimation(){
        return new RotateAnimation(0,10f,0,10f);
    }
    private Animation provideTransparencyAnimation(){
        return new AlphaAnimation(100f,10f);
    }

    //属性动画
    ///对象动画
    public static class Dog{
        int step;//步数
    }
    private Animator provideObjectAnimator(Object obj,String property){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(obj, property, 0.5f, 1.f);
        objectAnimator.setDuration(5000);
        return objectAnimator;
    }
    //值动画
    private Animator provideIntValueAnimator(){
        return ValueAnimator.ofInt(1, 100);
    }
    private Animator provideFloatValueAnimator(){
        return ValueAnimator.ofFloat(1, 100);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Animator provideArgbValueAnimator(){
        return ValueAnimator.ofArgb(0xffffffff, 0x00000000);
    }
}
