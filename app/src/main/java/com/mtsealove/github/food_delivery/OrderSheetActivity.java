package com.mtsealove.github.food_delivery;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mtsealove.github.food_delivery.Design.MenuOrderRecyclerAdapter;
import com.mtsealove.github.food_delivery.Design.SystemUiTuner;
import com.mtsealove.github.food_delivery.Entity.Menu;
import com.mtsealove.github.food_delivery.Entity.Result;
import com.mtsealove.github.food_delivery.Restful.RestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderSheetActivity extends AppCompatActivity {
    ArrayList<Integer> orderList;
    String tag = getClass().getSimpleName();
    String address = null;
    EditText addressEt;
    Button orderBtn;
    TextView priceTv;
    public static RecyclerView menuRv;
    MenuOrderRecyclerAdapter recyclerAdapter;
    String managerID;
    ProgressDialog itemDialog, orderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sheet);
        addressEt = findViewById(R.id.addressEt);
        menuRv = findViewById(R.id.menuRv);
        orderBtn = findViewById(R.id.orderBtn);
        priceTv = findViewById(R.id.priceTv);

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
        managerID = getIntent().getStringExtra("managerID");

        itemDialog = new ProgressDialog(this);
        itemDialog.setMessage("데이터를 불러오는 중입니다");
        itemDialog.setCancelable(false);
        itemDialog.show();
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

    int price = 0;

    private void GetItem(int ID) {
        RestAPI restAPI = new RestAPI(this);
        Call<Menu> call = restAPI.getRetrofitService().GetMenu(ID);
        call.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                if (response.isSuccessful()) {
                    Menu menu = response.body();
                    recyclerAdapter.addItem(menu);
                    price += menu.getPrice();
                    priceTv.setText(price + "원");
                    if (recyclerAdapter.getItemCount() == orderList.size()) {
                        menuRv.setAdapter(recyclerAdapter);
                        itemDialog.dismiss();
                    }
                }

            }

            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
                itemDialog.dismiss();
            }
        });
    }

    private void ConfirmOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("확인")
                .setMessage("주문하시겠습니까?")
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CreateOrder();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    int done = 0;

    //주문 만들기
    private void CreateOrder() {
        orderDialog = new ProgressDialog(this);
        orderDialog.setMessage("주문을 처리중입니다");
        orderDialog.setCancelable(false);
        orderDialog.show();
        done = 0;
        RestAPI restAPI = new RestAPI(this);
        //익명 사용자 ID
        String memberID = FirebaseInstanceId.getInstance().getId();
        //주문 시간
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String orderTime = dateFormat.format(new Date(System.currentTimeMillis()));

        address = addressEt.getText().toString();

        //상품 개수만큼 주문 수행
        for (int ItemID : orderList) {
            Call<Result> call = restAPI.getRetrofitService().PostOrder(managerID, memberID, orderTime, address, ItemID);
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    if (response.isSuccessful()) {
                        done++;
                        if (done == orderList.size()) {
                            orderDialog.dismiss();
                            Toast.makeText(OrderSheetActivity.this, "주문이 완료되었습니다", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(OrderSheetActivity.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Log.e(tag, t.toString());
                }
            });
        }
    }
}
