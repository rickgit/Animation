package edu.ptu.androidanimation.viewpager.impl;

import android.support.v4.view.ViewCompat;
import android.view.View;

import static android.support.v4.view.ViewPager.PageTransformer;

/**
 * Created by WangAnshu on 16/4/9.
 */
public class CustomTransformer implements PageTransformer {
    @Override
    public void transformPage(View view, float v) {
//        v ∈{-1,1} v范围在-1到1之间
        if (v > 1 || v < -1)
            System.out.println("===> v " + v);
        if (v >= 0) {
            int color = (((byte) (0xff * v) )|(0xff<<24));
            ViewCompat.animate(view).scaleX(1 - v/3).scaleY(1 - v/3).start();
        } else {
            v = Math.abs(v);
            byte color = (byte) ((0xff<<24) |((byte)(0xff - (byte) (0xff * v))<<8));
            ViewCompat.animate(view).scaleX(1 - v/3).scaleY(1 - v/3).start();
        }
    }
}
