package com.mtsealove.github.food_delivery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mtsealove.github.food_delivery.Design.DrawerView;
import com.mtsealove.github.food_delivery.Design.SystemUiTuner;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //음식 종류
    LinearLayout korean, western, chinese, fish, chicken, burger, lunch_box, coffee, dessert;
    TextView addressTv;
    ProgressBar loadPb;
    static DrawerLayout drawerLayout;
    AdView adView;
    String tag = getClass().getSimpleName();

    //위치 관련
    LocationManager lm;

    Geocoder geocoder;
    private String Address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SystemUiTuner tuner = new SystemUiTuner(this);
        tuner.setStatusBarWhite();

        korean = findViewById(R.id.korean);
        western = findViewById(R.id.western);
        chinese = findViewById(R.id.chinese);
        fish = findViewById(R.id.fish);
        chicken = findViewById(R.id.chicken);
        burger = findViewById(R.id.burger);
        lunch_box = findViewById(R.id.lunch_box);
        coffee = findViewById(R.id.coffee);
        dessert = findViewById(R.id.dessert);
        addressTv = findViewById(R.id.addressTv);
        loadPb = findViewById(R.id.loadPb);
        drawerLayout = findViewById(R.id.drawerLayout);

        GetLocation();
        korean.setOnClickListener(CatClickListener);
        western.setOnClickListener(CatClickListener);
        chinese.setOnClickListener(CatClickListener);
        fish.setOnClickListener(CatClickListener);
        chicken.setOnClickListener(CatClickListener);
        burger.setOnClickListener(CatClickListener);
        lunch_box.setOnClickListener(CatClickListener);
        coffee.setOnClickListener(CatClickListener);
        dessert.setOnClickListener(CatClickListener);
        adView = findViewById(R.id.adView);

        SetAdView();
        DrawerView.checkLogin();
    }

    //광고 설정
    private void SetAdView() {
        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "G"); // 사용 연령 3세 이상
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        adView.loadAd(adRequest);
    }

    //위치 정보 알아내기
    @SuppressLint("MissingPermission")
    private void GetLocation() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        SetAddress(location);

        //1분/1M 에 한번 업데이트
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 1, locationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 1, locationListener);
    }

    //위치정보 변경 리스너
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            SetAddress(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    //위경도를 변환하여 주소로 표시
    private void SetAddress(Location location) {
        geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
            if (addressList != null) {
                Address = addressList.get(0).getAddressLine(0);
                addressTv.setText(Address);
                loadPb.setVisibility(View.GONE);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener CatClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int cat = 1;
            //클릭한 뷰에 따라 다른 카테고리 설정
            switch (v.getId()) {
                case R.id.korean:
                    cat = 1;
                    break;
                case R.id.western:
                    cat = 2;
                    break;
                case R.id.chinese:
                    cat = 3;
                    break;
                case R.id.fish:
                    cat = 4;
                    break;
                case R.id.chicken:
                    cat = 5;
                    break;
                case R.id.burger:
                    cat = 6;
                    break;
                case R.id.lunch_box:
                    cat = 7;
                    break;
                case R.id.coffee:
                    cat = 8;
                    break;
                case R.id.dessert:
                    cat = 9;
                    break;
            }
            //카테고리를 넘기고 액티비티 이동
            Intent intent = new Intent(MainActivity.this, RestaurantListActivity.class);
            intent.putExtra("address", Address);
            intent.putExtra("cat", cat);
            startActivity(intent);
        }
    };

    //메뉴 열기
    public static void OpenDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    //메듀 닫기
    public static void CloseDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
    }
}
