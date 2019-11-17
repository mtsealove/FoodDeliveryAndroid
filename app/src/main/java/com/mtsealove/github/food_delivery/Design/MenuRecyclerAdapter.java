package com.mtsealove.github.food_delivery.Design;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mtsealove.github.food_delivery.Entity.Menu;
import com.mtsealove.github.food_delivery.Entity.Restaurant;
import com.mtsealove.github.food_delivery.Fragments.ItemListFragment;
import com.mtsealove.github.food_delivery.R;
import com.mtsealove.github.food_delivery.StoreActivity;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MenuRecyclerAdapter extends RecyclerView.Adapter<MenuRecyclerAdapter.ItemViewHolder> {
    Context context;

    public MenuRecyclerAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<Menu> listData = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_menu, parent, false);
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
        Button orderBtn;

        ItemViewHolder(View itemView) {
            super(itemView);

            imgIv = itemView.findViewById(R.id.imgIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            priceTv = itemView.findViewById(R.id.priceTv);
            desTv = itemView.findViewById(R.id.desTv);
            orderBtn = itemView.findViewById(R.id.orderBtn);
            clickLayout = itemView.findViewById(R.id.clickLayout);
        }

        void onBind(final Menu data) {

            if (data.getImagePath() != null&&data.getImagePath().length()!=0) {
                Glide.with(context)
                        .load(GetIP() + "/DeliveryService/Images/" + data.getImagePath())
                        .into(imgIv);
            }
            nameTv.setText(data.getItemName());
            priceTv.setText("₩" + data.getPrice());
            if (data.getDes() != null)
                desTv.setText(data.getDes());

            orderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemListFragment.orderList.add(data.getID());
                    Toast.makeText(context, "메뉴가 추가되었습니다", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private String GetIP() {
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        return "http://"+pref.getString("ip", "");
    }
}