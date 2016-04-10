package edu.ptu.androidanimation.viewpager.impl;

import android.view.View;

import static android.support.v4.view.ViewPager.PageTransformer;

/**
 * Created by WangAnshu on 16/4/9.
 */
public class CustomTransformer implements PageTransformer {
    @Override
    public void transformPage(View view, float v) {
//        v ∈{-1,1} v范围在-1到1之间
        if (v >= 0) {
            view.setBackgroundColor(0xff_ff_ff & ( ((byte)(0xff * v))<<24));
        }
        else {
            v=Math.abs(v);
            byte color = (byte) (0xff_ff_ff & ((0xff - (byte) (0xff * v)) & 0xff << 24));
            view.setBackgroundColor((byte)color);
        }
    }
}
