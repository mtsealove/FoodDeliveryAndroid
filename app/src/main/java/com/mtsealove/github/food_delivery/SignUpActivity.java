package com.mtsealove.github.food_delivery;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mtsealove.github.food_delivery.Design.SystemUiTuner;
import com.mtsealove.github.food_delivery.Entity.ResIdExist;
import com.mtsealove.github.food_delivery.Entity.Result;
import com.mtsealove.github.food_delivery.Restful.RestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    EditText idEt, nameEt, pwEt, pwConfirmEt;
    TextView existTv, notExistTv;
    Button confirmBtn;
    boolean IdCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        idEt = findViewById(R.id.idEt);
        nameEt = findViewById(R.id.nameEt);
        pwEt = findViewById(R.id.pwEt);
        pwConfirmEt = findViewById(R.id.pwConfirmEt);
        existTv = findViewById(R.id.existTv);
        notExistTv = findViewById(R.id.notExistTv);
        confirmBtn = findViewById(R.id.confirmBtn);

        SystemUiTuner tuner = new SystemUiTuner(this);
        tuner.setStatusBarWhite();

        idEt.addTextChangedListener(idWatcher);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInput();
            }
        });
    }

    //아이디 값 변경에 대한 이벤트
    TextWatcher idWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            CheckIdExist(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    //아이디 존재하는지 확인
    private void CheckIdExist(String ID) {
        RestAPI restAPI = new RestAPI(this);
        Call<ResIdExist> call = restAPI.getRetrofitService().CheckIdExist(ID);
        call.enqueue(new Callback<ResIdExist>() {
            @Override
            public void onResponse(Call<ResIdExist> call, Response<ResIdExist> response) {
                if (response.isSuccessful()) {
                    if (response.body().isExist()) {
                        existTv.setVisibility(View.VISIBLE);
                        notExistTv.setVisibility(View.GONE);
                        IdCheck = false;
                    } else {
                        existTv.setVisibility(View.GONE);
                        notExistTv.setVisibility(View.VISIBLE);
                        IdCheck = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResIdExist> call, Throwable t) {

            }
        });
    }

    //회원가입 입력 값 체크
    private void CheckInput() {
        if (idEt.getText().toString().length() == 0) {
            Toast.makeText(this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (!IdCheck) {
            Toast.makeText(this, "중복된 아이디입니다", Toast.LENGTH_SHORT).show();
            return;
        } else if (nameEt.getText().toString().length() == 0) {
            Toast.makeText(this, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
        } else if (pwEt.getText().toString().length() == 0) {
            Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (!pwEt.getText().toString().equals(pwConfirmEt.getText().toString())) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
            return;
        } else {
            CreateMember();
        }
    }

    //서버에 회원 생성
    private void CreateMember() {
        String ID = idEt.getText().toString();
        String pw = pwEt.getText().toString();
        String name = nameEt.getText().toString();
        RestAPI restAPI = new RestAPI(this);
        Call<Result> call = restAPI.getRetrofitService().CreateMember(ID, name, pw);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResult().equals("Ok")) {
                        Toast.makeText(SignUpActivity.this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "알 수 없는 오류가 발생하였습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "서버에 연결할 수 없습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
