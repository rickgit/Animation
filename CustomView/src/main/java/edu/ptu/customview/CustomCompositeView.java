package edu.ptu.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**自定义标题
 * Created by WangAnshu on 2016/3/28.
 */
public class CustomCompositeView extends LinearLayout{
    public CustomCompositeView(Context context) {
        super(context);
    }

    public CustomCompositeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCompositeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomCompositeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
