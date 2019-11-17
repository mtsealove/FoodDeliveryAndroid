package com.mtsealove.github.food_delivery.Design;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mtsealove.github.food_delivery.Entity.LastOrder;
import com.mtsealove.github.food_delivery.R;

import java.util.ArrayList;

public class LastOrderView extends LinearLayout {
    Context context;
    TextView nameTv, timeTv, itemNameTv, priceTv;
    ArrayList<LastOrder> lastOrders;

    public LastOrderView(Context context) {
        super(context);
        init(context);
    }

    public LastOrderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LastOrderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LastOrderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_last_order, this, false);
        lastOrders = new ArrayList<>();

        nameTv = view.findViewById(R.id.nameTv);
        timeTv = view.findViewById(R.id.timeTv);
        itemNameTv = view.findViewById(R.id.itemNameTv);
        priceTv = view.findViewById(R.id.priceTv);
        addView(view);
    }

    public void AddOrder(LastOrder order) {
        lastOrders.add(order);
        Refresh();
    }

    private void Refresh() {
        int price = 0;
        String itemNames = "";
        for (LastOrder order : lastOrders) {
            price += order.getPrice();
            itemNames += "\n" + order.getItemName();
        }
        nameTv.setText("업체명: " + lastOrders.get(0).getBusinessName());
        timeTv.setText("주문 시간: " + lastOrders.get(0).getOrderTime());
        priceTv.setText("주문금액: " + price + "원");
        itemNameTv.setText("상품 목록"+itemNames);
    }
}
