package com.mtsealove.github.food_delivery;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mtsealove.github.food_delivery.Design.MenuOrderRecyclerAdapter;
import com.mtsealove.github.food_delivery.Design.SystemUiTuner;
import com.mtsealove.github.food_delivery.Entity.Menu;
import com.mtsealove.github.food_delivery.Restful.RestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderSheetActivity extends AppCompatActivity {
    ArrayList<Integer> orderList;
    String tag = getClass().getSimpleName();
    String address = null;
    EditText addressEt;
    Button orderBtn;
    public static RecyclerView menuRv;
    MenuOrderRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sheet);
        addressEt = findViewById(R.id.addressEt);
        menuRv = findViewById(R.id.menuRv);
        orderBtn=findViewById(R.id.orderBtn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        menuRv.setLayoutManager(layoutManager);
        SystemUiTuner tuner = new SystemUiTuner(this);
        tuner.setStatusBarWhite();
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmOrder();
            }
        });

        orderList = getIntent().getIntegerArrayListExtra("orderList");
        for (int id : orderList) {
            Log.d(tag, String.valueOf(id));
            GetItem(id);
        }

        recyclerAdapter = new MenuOrderRecyclerAdapter(this);
        GetLocation();
    }

    //기본 주문지 설정
    @SuppressLint("MissingPermission")
    private void GetLocation() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
            if (addressList != null && addressList.size() != 0) {
                address = addressList.get(0).getAddressLine(0);
                addressEt.setText(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void GetItem(int ID) {
        RestAPI restAPI = new RestAPI(this);
        Call<Menu> call = restAPI.getRetrofitService().GetMenu(ID);
        call.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                if (response.isSuccessful()) {
                    Menu menu = response.body();
                    recyclerAdapter.addItem(menu);
                    if(recyclerAdapter.getItemCount()==orderList.size()) {
                        menuRv.setAdapter(recyclerAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<Menu> call, Throwable t) {

            }
        });
    }

    private void ConfirmOrder() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("확인")
                .setMessage("주문하시겠습니까?")
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog=builder.create();
        dialog.show();
    }
}
