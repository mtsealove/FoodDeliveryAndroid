package com.mtsealove.github.food_delivery;

import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mtsealove.github.food_delivery.Design.LastOrderView;
import com.mtsealove.github.food_delivery.Design.SystemUiTuner;
import com.mtsealove.github.food_delivery.Entity.LastOrder;
import com.mtsealove.github.food_delivery.Restful.RestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class LastOrderActivity extends AppCompatActivity {
    LinearLayout contentLayout;
    String memberID;
    static DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_order);
        contentLayout = findViewById(R.id.contentLayout);
        drawerLayout=findViewById(R.id.drawerLayout);

        SystemUiTuner tuner=new SystemUiTuner(this);
        tuner.setStatusBarWhite();

        GetID();
        getLastOrders();
    }

    //주문 ID 얻어오기
    private void GetID() {
        if (LoginActivity.login == null) {
            memberID = FirebaseInstanceId.getInstance().getId();
        } else {
            memberID = LoginActivity.login.getID();
        }
    }

    //데이터 가져오기
    private void getLastOrders() {
        RestAPI restAPI = new RestAPI(this);
        Call<List<LastOrder>> call = restAPI.getRetrofitService().GetLastOrders(memberID);
        call.enqueue(new Callback<List<LastOrder>>() {
            @Override
            public void onResponse(Call<List<LastOrder>> call, Response<List<LastOrder>> response) {
                if (response.isSuccessful()) {
                    LastOrder tmp = null;
                    LastOrderView orderView = new LastOrderView(LastOrderActivity.this);
                    for (LastOrder order : response.body()) {
                        if (tmp == null) {
                        } else if (!tmp.getOrderTime().equals(order.getOrderTime())) {  //주문 시간으로 판단
                            contentLayout.addView(orderView);
                            orderView = new LastOrderView(LastOrderActivity.this);
                        } else {
                            orderView.AddOrder(order);
                        }
                        tmp = order;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<LastOrder>> call, Throwable t) {

            }
        });
    }

    public static void OpenDrawer() {
        if(!drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void CloseDrawer() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
    }
}
