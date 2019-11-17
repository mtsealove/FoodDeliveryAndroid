package com.mtsealove.github.food_delivery;


import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mtsealove.github.food_delivery.Design.DrawerView;
import com.mtsealove.github.food_delivery.Design.SystemUiTuner;
import com.mtsealove.github.food_delivery.Entity.Order;
import com.mtsealove.github.food_delivery.Restful.RestAPI;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CurrentOrderActivity extends AppCompatActivity {
    LinearLayout mapLayout;
    TextView currentLocationTv, statusTv;
    ListView itemList;
    String memberID;
    String tag = getClass().getSimpleName();
    String address, orderTime, StatusName;
    static DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);
        SystemUiTuner tuner = new SystemUiTuner(this);
        tuner.setStatusBarWhite();
        mapLayout = findViewById(R.id.mapLayout);
        currentLocationTv = findViewById(R.id.currentLocationTv);
        statusTv = findViewById(R.id.statusTv);
        itemList = findViewById(R.id.itemLv);
        drawerLayout=findViewById(R.id.drawerLayout);

        GetOrder();
    }

    //위치 설정
    private void setMap(double latitude, double longitude) {
        //카카오맵 설정
        MapView mapView = new MapView(this);
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude), 3, true);

        //마커 설정
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("현재 위치");
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        //마커 표시
        mapView.addPOIItem(marker);
        //화면에 추가
        mapLayout.addView(mapView);
    }

    //서버에서 데이터 받아오기
    private void GetOrder() {
        RestAPI restAPI = new RestAPI(this);
        if (LoginActivity.login == null) {
            memberID = FirebaseInstanceId.getInstance().getId();
        } else {
            memberID = LoginActivity.login.getID();
        }
        Call<List<Order>> call = restAPI.getRetrofitService().GetCurrentOrder(memberID);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() != 0)
                        SetRv(response.body());
                    else {
                        Toast.makeText(CurrentOrderActivity.this, "현재 주문한 상품이 없습니다", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

            }
        });
    }

    //화면에 상품 목록 표시
    private void SetRv(List<Order> orders) {
        String LastTime = null;
        ArrayList<String> items = new ArrayList<>();

        int i = 0;
        for (Order order : orders) {
            if (i == 0) {
                LastTime = order.getOrderTime();
            }
            i++;
            if (!order.getOrderTime().equals(LastTime)) {
                Log.d(tag, "끝");
                break;
            }
            Log.d(tag, order.toString());
            items.add(order.getItemName() + " " + order.getPrice() + "원");
            address = order.getCurrentLocation();
            orderTime = order.getOrderTime();
            StatusName = order.getStatusName();
        }
        currentLocationTv.setText("현재 위치: " + address);
        statusTv.setText(StatusName);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, items);
        itemList.setAdapter(arrayAdapter);

        GetLocation(address);
    }

    //지오코딩
    private void GetLocation(String address) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> list = geocoder.getFromLocationName(address, 1);
            if (list != null && list.size() != 0) {
                setMap(list.get(0).getLatitude(), list.get(0).getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
