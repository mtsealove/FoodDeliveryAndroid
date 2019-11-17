package com.mtsealove.github.food_delivery.Design;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mtsealove.github.food_delivery.Entity.Menu;
import com.mtsealove.github.food_delivery.Entity.Order;
import com.mtsealove.github.food_delivery.Fragments.ItemListFragment;
import com.mtsealove.github.food_delivery.OrderSheetActivity;
import com.mtsealove.github.food_delivery.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MenuOrderRecyclerAdapter extends RecyclerView.Adapter<MenuOrderRecyclerAdapter.ItemViewHolder> {
    Context context;

    public MenuOrderRecyclerAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<Menu> listData = new ArrayList<>();

    public ArrayList<Menu> getListData() {
        return listData;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_menu_order, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(Menu data) {
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIv;
        TextView nameTv, priceTv, desTv;
        LinearLayout clickLayout;
        Button removeBtn;

        ItemViewHolder(View itemView) {
            super(itemView);

            imgIv = itemView.findViewById(R.id.imgIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            priceTv = itemView.findViewById(R.id.priceTv);
            desTv = itemView.findViewById(R.id.desTv);
            removeBtn = itemView.findViewById(R.id.removeBtn);
            clickLayout = itemView.findViewById(R.id.clickLayout);
        }

        void onBind(final Menu data) {

            if (data.getImagePath() != null && data.getImagePath().length() != 0) {
                Glide.with(context)
                        .load(GetIP() + "/DeliveryService/Images/" + data.getImagePath())
                        .into(imgIv);
            }
            nameTv.setText(data.getItemName());
            priceTv.setText("₩" + data.getPrice());
            if (data.getDes() != null)
                desTv.setText(data.getDes());
            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listData.remove(data);
                     OrderSheetActivity.menuRv.setAdapter(MenuOrderRecyclerAdapter.this);
                     int price=0;
                    for(Menu menu: listData) {
                        price+=menu.getPrice();
                    }
                    OrderSheetActivity.priceTv.setText(price+"원");
                }
            });


        }
    }

    private String GetIP() {
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        return "http://"+pref.getString("ip", "");
    }
}