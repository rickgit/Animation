package edu.ptu.customview.element.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;

import edu.ptu.customview.element.IDrawElement;

/**<pre>
 *      类里面有path作为总要对象。
 * </pre>
 * Created by WangAnshu on 2016/4/7.
 */
public class PathInfo implements IDrawElement {
    private final Path path;
    private final PathMeasure pathMeasure;
    private final float length;
    private int distance;
    private float[] pos={0,0};
    private float[] tang={0,0};
    public static final int posIndexX=0;
    public static final int posIndexY=1;

    public PathInfo(){
        this.path=new Path();

//        this.path.lineTo(290, 290);
        int screenWidth=600;
        int[] controlPos1={0, 0};
        int[] controlPos2={0, (int) (screenWidth/3f*2)};
        int[] startPos={(int) (screenWidth/2.f), (int) (screenWidth/3f)};
        int[] endPos={(int) (screenWidth/2.f), screenWidth-20};

        this.path.moveTo(startPos[posIndexX], startPos[posIndexY]);
        this.path.cubicTo(controlPos1[posIndexX], controlPos1[posIndexY], controlPos2[posIndexX], controlPos2[posIndexY], endPos[posIndexX], endPos[posIndexY]);
        this.path.cubicTo(screenWidth - controlPos2[posIndexX], controlPos2[posIndexY], screenWidth - controlPos1[posIndexX], controlPos1[posIndexY], startPos[posIndexX], startPos[posIndexY]);
//        this.path.cubicTo(150, 75,150,200,290, 290);
        pathMeasure = new PathMeasure(path, false);
        length = pathMeasure.getLength();
        this.distance=0;
    }

    /**
     * 获取下一帧的数据
     */
    public void getNext(){
        if (distance>=length){
            distance=0;
        }
        pathMeasure.getPosTan(distance,pos,tang);
        distance+=10;
    }

    public float[] getPos() {
        return pos;
    }

    public Path getPath() {
        return path;
    }

    /**
     *
     * @param canvas
     * @param paint
     */
    //XXX 可以返回布尔类型，作为是否要重绘标示符。
    public void onDrawElement(Canvas canvas,Paint paint) {
        getNext();
        float[] pos = getPos();
        paint.setColor(0xffff0000);
        canvas.drawPath(getPath(), paint);
        paint.setColor(0xffffffff);
        canvas.drawCircle(pos[0], pos[1], 5, paint);

    }
}
