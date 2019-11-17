package com.mtsealove.github.food_delivery;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.iid.FirebaseInstanceId;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mtsealove.github.food_delivery.Design.DrawerView;
import com.mtsealove.github.food_delivery.Design.SystemUiTuner;
import com.mtsealove.github.food_delivery.Entity.ResLogin;
import com.mtsealove.github.food_delivery.Restful.RestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.security.MessageDigest;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    String tag = getClass().getSimpleName();
    ImageView logoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logoIv = findViewById(R.id.logoIv);

        logoIv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(SplashActivity.this, IpActivity.class);
                startActivity(intent);
                return false;
            }
        });

        SystemUiTuner tuner = new SystemUiTuner(this);
        tuner.setStatusBarWhite();

        CheckPermission();
    }

    //매인으로 이동
    private void moveMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //권한 체크
    private void CheckPermission() {
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    //권한 설정 리스너
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Login();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            //권한이 없으면 프로그램이 제대로 작동하지 않기 때문에 2초 후에 종료
            Toast.makeText(SplashActivity.this, "권한을 허용하지 않았습니다.\n잠시 후 프로그램을 종료합니다", Toast.LENGTH_LONG).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    System.exit(0);
                }
            }, 2000);
        }
    };

    //자동 로그인
    private void Login() {
        //기기에 저장된 ID, pw 읽기
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        RestAPI restAPI = new RestAPI(this);
        final String ID = pref.getString("id", "");
        final String pw = pref.getString("pw", "");
        if (ID.length() != 0 && pw.length() != 0) {
            //기기 토큰 얻기
            String token = FirebaseInstanceId.getInstance().getToken();
            Call<ResLogin> call = restAPI.getRetrofitService().PostLogin(ID, pw, token);
            call.enqueue(new Callback<ResLogin>() {
                @Override
                public void onResponse(Call<ResLogin> call, Response<ResLogin> response) {
                    if (response.isSuccessful()) {
                        ResLogin login = response.body();
                        if (login.getID() == null) {    //로그인 실패
                            moveMain();
                        } else {    //로그인 성공
                            LoginActivity.login = login;
                            Toast.makeText(SplashActivity.this, "환영합니다.\n" + login.getName() + "님", Toast.LENGTH_SHORT).show();
                            moveMain();
                        }
                    } else {
                        Toast.makeText(SplashActivity.this, "서버에 연결할 수가 없습니다", Toast.LENGTH_SHORT).show();
                        moveMain();
                    }
                    Log.e(tag, response.toString());
                }

                @Override
                public void onFailure(Call<ResLogin> call, Throwable t) {
                    Toast.makeText(SplashActivity.this, "서버에 연결할 수가 없습니다", Toast.LENGTH_SHORT).show();
                    moveMain();
                    Log.e(tag, t.toString());
                }
            });
        } else {
            moveMain();
        }
    }
}
