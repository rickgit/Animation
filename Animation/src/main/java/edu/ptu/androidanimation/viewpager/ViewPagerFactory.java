package edu.ptu.androidanimation.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import edu.ptu.androidanimation.viewpager.impl.CustomTransformer;

/**
 * Created by WangAnshu on 16/4/9.
 */
public class ViewPagerFactory {
    public static ViewPager createViewpager(FragmentActivity context, ViewPager viewpager) {

        viewpager.setAdapter(new FragmentPagerAdapter(context.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {

                return new ColorFragment();
            }

            @Override
            public int getCount() {
                return 10;
            }
        });
        viewpager.setPageTransformer(true, new CustomTransformer());
        return  viewpager;
    }
}
