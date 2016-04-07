package edu.ptu.androidanimation.graphics;

import android.graphics.Path;
import android.graphics.PathMeasure;

/**
 * Created by WangAnshu on 2016/4/6.
 */
public class PathUtils {
    public void getPosTan(){
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(100, 100);
        PathMeasure pm = new PathMeasure(path, false);
        float[] pos1 = {0.0f, 0.0f};//拉直后，所要长度的坐标
        float[] zhengqie = {0.0f, 0.0f};//正弦值

        for (int i = 0; i < 10; i++) {
            pm.getPosTan(10*i,pos1,zhengqie);
            System.out.println("===>"+pos1[0]+" "+pos1[1]+" ;"+zhengqie[0]+" "+zhengqie[1]);
        }
    }
}
