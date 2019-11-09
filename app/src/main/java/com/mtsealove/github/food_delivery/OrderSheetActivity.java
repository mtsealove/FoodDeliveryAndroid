package com.mtsealove.github.food_delivery;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mtsealove.github.food_delivery.Design.SystemUiTuner;

import java.util.ArrayList;

public class OrderSheetActivity extends AppCompatActivity {
    ArrayList<Integer> orderList;
    String tag = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sheet);

        SystemUiTuner tuner = new SystemUiTuner(this);
        tuner.setStatusBarWhite();

        orderList = getIntent().getIntegerArrayListExtra("orderList");
        for (int i : orderList) {
            Log.d(tag, "ID: " + i);
        }
    }
}
