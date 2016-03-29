package edu.ptu.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by WangAnshu on 2016/3/28.
 */
public class CustomDrawView extends View {

    private Paint paint;
    private RectF oval;

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
        initParams();
    }
    public void initParams(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oval = new RectF(0, 0, 100, 100);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(0xff000000);
        canvas.drawRect(oval, paint);

    }
}
