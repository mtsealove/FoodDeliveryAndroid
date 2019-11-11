package com.mtsealove.github.food_delivery.Design;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.mtsealove.github.food_delivery.MainActivity;
import com.mtsealove.github.food_delivery.R;

public class TitleView extends RelativeLayout {
    Context context;
    String tag = getClass().getSimpleName();
    ImageView menuIv;

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

        View layout = inflater.inflate(R.layout.view_title, TitleView.this, false);
        menuIv=layout.findViewById(R.id.menuIv);
        menuIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDrawer();
            }
        });
        addView(layout);
    }

    private void OpenDrawer() {
        switch (context.getClass().getSimpleName()){
            case "MainActivity":
                MainActivity.OpenDrawer();
                break;
        }
    }

}
