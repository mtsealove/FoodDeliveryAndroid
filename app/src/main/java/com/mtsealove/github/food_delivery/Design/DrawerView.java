package com.mtsealove.github.food_delivery.Design;

import android.app.AlertDialog;
import android.content.*;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.drawerlayout.widget.DrawerLayout;
import com.mtsealove.github.food_delivery.*;
import com.mtsealove.github.food_delivery.Entity.LastOrder;
import com.mtsealove.github.food_delivery.Entity.Restaurant;

import java.util.ArrayList;

public class DrawerView extends LinearLayout {
    Context context;
    ListView listView;
    static TextView nameTv;
    private TextView logoutTv;
    static RelativeLayout loginedLayout, loginLayout;

    public DrawerView(Context context) {
        super(context);
        init(context);
    }

    public DrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DrawerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.view_drawer, DrawerView.this, false);
        loginedLayout = layout.findViewById(R.id.loginedLayout);
        loginLayout = layout.findViewById(R.id.loginLayout);
        listView = layout.findViewById(R.id.listView);
        nameTv = layout.findViewById(R.id.nameTv);
        logoutTv = layout.findViewById(R.id.logoutTv);
        setList();
        addView(layout);

        checkLogin();
        logoutTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });
        loginLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLogin();
            }
        });
    }

    private void setList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("현재 주문 확인");
        list.add("이전 주문 확인");

        ArrayAdapter adapter = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        moveCurrentOrder();
                        break;
                    case 1:
                        moveLastOrder();
                        break;
                }
            }
        });
    }

    //현재 주문 화면으로 이동
    private void moveCurrentOrder() {
        CloseDrawer();
        Intent intent = new Intent(context, CurrentOrderActivity.class);
        context.startActivity(intent);

    }

    //이전 주문으로 이동
    private void moveLastOrder() {
        CloseDrawer();
        Intent intent=new Intent(context, LastOrderActivity.class);
        context.startActivity(intent);
    }

    //로그인 화면으로 이동
    private void moveLogin() {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    //로그인되어있는지 확인
    public static void checkLogin() {
        if (LoginActivity.login== null) {
            loginLayout.setVisibility(VISIBLE);
            loginedLayout.setVisibility(GONE);
        } else {
            loginLayout.setVisibility(GONE);
            loginedLayout.setVisibility(VISIBLE);
            nameTv.setText(LoginActivity.login.getName());
        }
    }

    //로그아웃
    private void Logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("로그아웃")
                .setMessage("로그아웃하시겠습니까?")
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginActivity.login = null;
                Toast.makeText(context, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
                checkLogin();
                CloseDrawer();
                RemoveAccount();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //기기에서 계정 삭제
    private void RemoveAccount() {
        SharedPreferences pref=context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.remove("id");
        editor.remove("pw");
        editor.commit();
    }

    private void CloseDrawer() {
        switch (context.getClass().getSimpleName()) {
            case "MainActivity":
                MainActivity.CloseDrawer();
                break;
            case "CurrentOrderActivity":
                CurrentOrderActivity.CloseDrawer();
                break;
            case "LastOrderActivity":
                LastOrderActivity.CloseDrawer();
                break;
            case "RestaurantListActivity":
                RestaurantListActivity.CloseDrawer();
                break;
        }
    }
}
