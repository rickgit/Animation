package edu.ptu.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by WangAnshu on 2016/3/28.
 */
public class CustomAbstractView extends ListView{
    public CustomAbstractView(Context context) {
        super(context);
    }

    public CustomAbstractView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomAbstractView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomAbstractView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
