package com.mtsealove.github.food_delivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mtsealove.github.food_delivery.Design.DrawerView;
import com.mtsealove.github.food_delivery.Design.SystemUiTuner;
import com.mtsealove.github.food_delivery.Entity.ResLogin;
import com.mtsealove.github.food_delivery.Restful.RestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText idEt, pwEt;
    Button loginBtn;
    String tag = getClass().getSimpleName();
    TextView signUpTv;
    public static ResLogin login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        idEt = findViewById(R.id.idEt);
        pwEt = findViewById(R.id.pwEt);
        loginBtn = findViewById(R.id.loginBtn);
        signUpTv=findViewById(R.id.signUpTv);

        SystemUiTuner tuner = new SystemUiTuner(this);
        tuner.setStatusBarWhite();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Login() {
        if (idEt.getText().toString().length() == 0) {
            Toast.makeText(this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (pwEt.getText().toString().length() == 0) {
            Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
        } else {
            RestAPI restAPI = new RestAPI(this);
            final String ID = idEt.getText().toString();
            final String pw = pwEt.getText().toString();
            String token= FirebaseInstanceId.getInstance().getToken();
            Call<ResLogin> call = restAPI.getRetrofitService().PostLogin(ID, pw, token);
            call.enqueue(new Callback<ResLogin>() {
                @Override
                public void onResponse(Call<ResLogin> call, Response<ResLogin> response) {
                    if (response.isSuccessful()) {
                        ResLogin login = response.body();
                        if (login.getID() == null) {
                            Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                        } else {
                            LoginActivity.login = login;
                            DrawerView.checkLogin();
                            Toast.makeText(LoginActivity.this, "환영합니다.\n" + login.getName() + "님", Toast.LENGTH_SHORT).show();
                            SaveAccount(ID, pw);
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResLogin> call, Throwable t) {
                    Log.e(tag, t.toString());
                }
            });
        }
    }

    private void SaveAccount(String id, String pw){
        SharedPreferences pref=getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("id", id);
        editor.putString("pw", pw);
        editor.commit();
    }
}
