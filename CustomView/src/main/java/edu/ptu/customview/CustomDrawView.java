package edu.ptu.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import edu.ptu.customview.element.IDrawElement;
import edu.ptu.customview.element.impl.PathInfo;

/**该类主要作为容器，进行绘制图形。
 * Created by WangAnshu on 2016/3/28.
 */
public class CustomDrawView extends View {

    private Paint paint;
    private RectF oval;
    private List<IDrawElement> pathInfo=new ArrayList<>(2);

    public CustomDrawView(Context context) {
        super(context);
        initParams();
    }

    public CustomDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParams();
    }

    public CustomDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomDrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initParams();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
       setMeasuredDimension(widthMode==MeasureSpec.EXACTLY?widthSize:600,heightMode==MeasureSpec.EXACTLY?heightSize:600);
    }
    public void initParams(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oval = new RectF(0, 0, 600, 600);
        pathInfo.add( new PathInfo());
//        pathInfo.add( new LineChart());
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(0xff000000);
        canvas.drawRect(oval, paint);
        for (IDrawElement item: pathInfo) {
            item.onDrawElement(canvas,paint);
        }
        postDelayed(action, 25);
    }



    Runnable action = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };
}
