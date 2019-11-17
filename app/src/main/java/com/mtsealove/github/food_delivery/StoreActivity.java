package com.mtsealove.github.food_delivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.mtsealove.github.food_delivery.Design.PageAdapter;
import com.mtsealove.github.food_delivery.Design.SystemUiTuner;
import com.mtsealove.github.food_delivery.Entity.Restaurant;

public class StoreActivity extends AppCompatActivity {
    Restaurant restaurant;
    TextView nameTv;
    ImageView profileIv;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        nameTv = findViewById(R.id.nameTv);
        profileIv = findViewById(R.id.profileIv);
        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tabLayout);

        setTab();

        init();

        SystemUiTuner tuner = new SystemUiTuner(this);
        tuner.setStatusBarWhite();
    }

    private void setTab() {
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(0);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //초기화
    private void init() {
        Intent intent = getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        //가게명 설정
        nameTv.setText(restaurant.getBusinessName());
        //이미지 설정
        if (restaurant.getProfileImage() != null) {
            Glide.with(this)
                    .load(GetIP() + "/DeliveryService/Images/" + restaurant.getProfileImage())
                    .into(profileIv);
        }
    }

    public String getManagerID() {
        return restaurant.getID();
    }

    //IP 얻기
    private String GetIP() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return "http://"+pref.getString("ip", "");
    }
}
