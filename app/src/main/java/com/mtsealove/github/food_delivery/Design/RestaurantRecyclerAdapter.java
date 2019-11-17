package com.mtsealove.github.food_delivery.Design;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.INotificationSideChannel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mtsealove.github.food_delivery.Entity.Restaurant;
import com.mtsealove.github.food_delivery.R;
import com.mtsealove.github.food_delivery.StoreActivity;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class RestaurantRecyclerAdapter extends RecyclerView.Adapter<RestaurantRecyclerAdapter.ItemViewHolder> {
    Context context;

    public RestaurantRecyclerAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<Restaurant> listData = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_restaurant, parent, false);
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

    public void addItem(Restaurant data) {
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView profileIv;
        TextView nameTv, addressTv, distanceTv;
        LinearLayout clickLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            profileIv = itemView.findViewById(R.id.profileIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            addressTv = itemView.findViewById(R.id.addressTv);
            distanceTv = itemView.findViewById(R.id.distanceTv);
            clickLayout = itemView.findViewById(R.id.clickLayout);
        }

        void onBind(final Restaurant data) {
            if (data.getProfileImage() != null) {
                Glide.with(context)
                        .load(GetIP() + "/DeliveryService/Images/" + data.getProfileImage())
                        .into(profileIv);
            }
            nameTv.setText(data.getBusinessName());
            addressTv.setText("위치: " + data.getBusinessAddress());
            if (data.getDistance() < 1000)
                distanceTv.setText("거리: " + (int) (data.getDistance()) + "m");
            else distanceTv.setText("거리: " + (int) (data.getDistance() / 1000) + "Km");

            //클릭 시 해당 식당의 상품 리스트 출력
            clickLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StoreActivity.class);
                    intent.putExtra("restaurant", data);
                    context.startActivity(intent);
                }
            });
        }
    }

    private String GetIP() {
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        return "http://"+pref.getString("ip", "");
    }
}