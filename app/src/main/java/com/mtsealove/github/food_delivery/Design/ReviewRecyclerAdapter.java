package com.mtsealove.github.food_delivery.Design;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mtsealove.github.food_delivery.Entity.Review;
import com.mtsealove.github.food_delivery.R;

import java.util.ArrayList;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ItemViewHolder> {
    Context context;

    public ReviewRecyclerAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<Review> listData = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_review, parent, false);
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

    public void addItem(Review data) {
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, contentTv, timeTv;

        ItemViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.userTv);
            contentTv = itemView.findViewById(R.id.contentTv);
            timeTv = itemView.findViewById(R.id.timeTv);
        }

        void onBind(final Review data) {
            nameTv.setText(data.getUserName());
            contentTv.setText(data.getContent());
            timeTv.setText(data.getTime().substring(0, 16) + "에 작성됨");
        }
    }
}