package edu.ptu.androidanimation;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import edu.ptu.androidanimation.animation.AnimationFactory;
import edu.ptu.androidanimation.graphics.PathUtils;
import edu.ptu.androidanimation.viewpager.ViewPagerFactory;

public class AnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        new PathUtils().getPosTan();
        ViewPager vp = (ViewPager) findViewById(R.id.anim_vp);
        ViewPagerFactory.createViewpager(this,vp);
    }
    public void playAnim(View tvLabel){
        AnimationFactory animationFactory = new AnimationFactory();
//        Animation viewAnimation = animationFactory.createViewAnimation();
//        tvLabel.setAnimation(viewAnimation);
//        viewAnimation.setStartOffset(5000);
//        AnimationDrawable animationDrawable = animationFactory.createAnimationDrawable(this);
//        tvLabel.setBackgroundDrawable(animationDrawable);
//        animationDrawable.start();
        Animator propertyAnimation = animationFactory.createPropertyAnimation(tvLabel, "alpha");
        propertyAnimation.start();
    }
}
