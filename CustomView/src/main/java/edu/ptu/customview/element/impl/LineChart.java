package edu.ptu.customview.element.impl;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Shader;

import edu.ptu.customview.element.IDrawElement;

/**
 * Created by WangAnshu on 2016/4/7.
 */
public class LineChart implements IDrawElement {
    private final Path path;
    private final PathMeasure pathMeasure;
    /**
     * 线的长度
     */
    private final float length;
    /**
     * 当前的距离
     */
    private int distance;
    private float[] pos={0,0};
    private float[] tang={0,0};
    public static final int posIndexX=0;
    public static final int posIndexY=1;

    public LineChart(){
        this.path=new Path();

//        this.path.lineTo(290, 290);
        int screenWidth=600;
        int controlWidth=600/4;

        int[] startPos={0, 0};
        int[] centerPos={(int) (screenWidth/3.f), (int) (screenWidth/2f)};
        int[] endPos={(int) (screenWidth/3*2), 0};

        this.path.moveTo(startPos[posIndexX], startPos[posIndexY]);
        this.path.cubicTo((startPos[posIndexX]+centerPos[posIndexX])/2, startPos[posIndexY], (startPos[posIndexX]+centerPos[posIndexX])/2, centerPos[posIndexY], centerPos[posIndexX], centerPos[posIndexY]);
        this.path.cubicTo((endPos[posIndexX]+centerPos[posIndexX])/2, centerPos[posIndexY], (endPos[posIndexX]+centerPos[posIndexX])/2, endPos[posIndexY], endPos[posIndexX], endPos[posIndexY]);
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

        Shader mShader = new LinearGradient(0,0,40,60,new int[] {Color.RED,Color.GREEN,Color.BLUE},null,Shader.TileMode.REPEAT);
        paint.setShader(mShader);

        canvas.drawPath(getPath(), paint);
        paint.setColor(0xffffffff);
        canvas.drawCircle(pos[0], pos[1], 5, paint);

    }
}
