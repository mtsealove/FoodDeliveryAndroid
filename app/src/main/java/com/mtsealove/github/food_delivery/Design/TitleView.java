package com.mtsealove.github.food_delivery.Design;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.mtsealove.github.food_delivery.R;

public class TitleView extends RelativeLayout {
    Context context;
    String tag=getClass().getSimpleName();

    public TitleView(Context context) {
        super(context);
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d(tag, String.valueOf(R.layout.view_title));
        if(inflater!=null) {
            View layout = inflater.inflate(R.layout.view_title, TitleView.this, false);
            addView(layout);
        }
    }
}
