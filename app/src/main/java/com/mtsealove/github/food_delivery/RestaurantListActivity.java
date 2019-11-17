package com.mtsealove.github.food_delivery;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mtsealove.github.food_delivery.Design.RestaurantRecyclerAdapter;
import com.mtsealove.github.food_delivery.Design.SystemUiTuner;
import com.mtsealove.github.food_delivery.Entity.Restaurant;
import com.mtsealove.github.food_delivery.Restful.RestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RestaurantListActivity extends AppCompatActivity {
    int cat;
    String address;
    String tag = getClass().getSimpleName();
    List<Restaurant> restaurants;
    LocationManager lm;
    Location location;
    ProgressDialog progressDialog;
    TextView catTv;
    static DrawerLayout drawerLayout;

    RecyclerView recyclerView;
    boolean finish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        recyclerView = findViewById(R.id.restaurantRv);
        catTv = findViewById(R.id.catTv);
        drawerLayout = findViewById(R.id.drawerLayout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        SystemUiTuner systemUiTuner = new SystemUiTuner(this);
        systemUiTuner.setStatusBarWhite();

        init();
        GetRestaurantList();
    }

    @SuppressLint("MissingPermission")
    private void init() {
        //카테고리 받아오기
        Intent intent = getIntent();
        cat = intent.getIntExtra("cat", 1);
        address = intent.getStringExtra("address");

        String category = "";
        switch (cat) {
            case 1:
                category = "한식";
                break;
            case 2:
                category = "양식";
                break;
            case 3:
                category = "중식";
                break;
            case 4:
                category = "회/일식";
                break;
            case 5:
                category = "치킨";
                break;
            case 6:
                category = "햄버거";
                break;
            case 7:
                category = "도시락";
                break;
            case 8:
                category = "커피/음료";
                break;
            case 9:
                category = "디저트";
                break;
        }
        catTv.setText(category);

        //위치정보 얻
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    private void GetRestaurantList() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("로딩");
        progressDialog.setMessage("데이터를 불러오는 중입니다");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RestAPI restAPI = new RestAPI(this);
        Call<List<Restaurant>> call = restAPI.getRetrofitService().GetRestaurantList(cat);

        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if (response.isSuccessful()) {
                    restaurants = response.body();
                    progressDialog.setMessage("위치를 찾는 중입니다");
                    //현재 위치와 거리 계산기
                    for (Restaurant restaurant : restaurants) {
                        restaurant.setDistance(RestaurantListActivity.this, location);
                    }

                    final Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (!finish) {
                                boolean done = true;
                                //모든 지오코딩이 완료되었는지 확인
                                for (Restaurant restaurant : restaurants) {
                                    try {
                                        restaurant.getDistance();
                                    } catch (Exception e) {
                                        done = false;
                                    }
                                }
                                if (done) {
                                    finish = true;
                                    timer.cancel();
                                    SortList();
                                }
                            }
                        }
                    };
                    timer.schedule(timerTask, 100);

                    Log.d(tag, "데이터 파싱 완료");
                } else {
                    Log.e(tag, "파싱 실패");
                    Log.e(tag, response.toString());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.e(tag, t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    //거리순 정렬
    private void SortList() {
        int least;
        for (int i = 0; i < restaurants.size() - 1; i++) {
            least = i;
            for (int j = i + 1; j < restaurants.size(); j++) {
                if (restaurants.get(j).getDistance() < restaurants.get(least).getDistance())
                    least = j;
            }
            if (i != least) {
                Restaurant tmp = restaurants.get(least);
                restaurants.set(least, restaurants.get(i));
                restaurants.set(i, tmp);
            }
        }
        final RestaurantRecyclerAdapter adapter = new RestaurantRecyclerAdapter(this);
        for (Restaurant restaurant : restaurants) {
            Log.d(tag, restaurant.toString());
            Log.d(tag, Double.toString(restaurant.getDistance()));
            adapter.addItem(restaurant);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }
        });
    }

    public static void OpenDrawer(){
        if(!drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public static void CloseDrawer() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.openDrawer(GravityCompat.START);
    }
}
