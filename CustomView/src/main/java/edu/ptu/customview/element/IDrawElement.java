package edu.ptu.customview.element;

import android.graphics.Canvas;
import android.graphics.Paint;

/**子类采用模板方法
 * Created by WangAnshu on 2016/4/7.
 */
public interface IDrawElement {
    public void onDrawElement(Canvas canvas,Paint paint);
}
