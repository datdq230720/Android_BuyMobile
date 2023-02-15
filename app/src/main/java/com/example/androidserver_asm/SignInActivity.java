package com.example.androidserver_asm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidserver_asm.Constant.Requests.RegisterRequest;
import com.example.androidserver_asm.Constant.Responses.RegisterResponse;
import com.example.androidserver_asm.Constant.Retrofits.IRetrofitService;
import com.example.androidserver_asm.Constant.Retrofits.RetrofitBuiler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    EditText ed_accont, ed_pass, ed_confim;
    Button bt_signup;
    TextView tv_login;

    IRetrofitService service;

    private boolean setEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ed_accont = findViewById(R.id.SignUp_account);
        ed_confim = findViewById(R.id.SignUp_confimpass);
        ed_pass = findViewById(R.id.SignUp_pass);
        bt_signup = findViewById(R.id.SignUp_bt);
        tv_login = findViewById(R.id.SignUp_login);

        service = RetrofitBuiler.createService(IRetrofitService.class);


        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = ed_accont.getText().toString();
                String password = ed_pass.getText().toString();
                String confimPass = ed_confim.getText().toString();
                if (password.equals(confimPass)){
                    RegisterRequest registerRequest = new RegisterRequest(account, password);
                    service.register(registerRequest).enqueue(register);
                    Log.d("Signin2 >>> ", "onResponse: "+setEmail);

                }else {
                    Toast.makeText(SignInActivity.this, "Xác nhận mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }

            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    Callback<RegisterResponse> register = new Callback<RegisterResponse>() {
        @Override
        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
             if (response.isSuccessful()){
                 RegisterResponse registerResponse = response.body();
                 setEmail = registerResponse.getResult();
                 Log.d("Signin >>> ", "onResponse: "+setEmail);
                 if (setEmail == true){
                     Intent i = new Intent(SignInActivity.this, MainActivity.class);
                     startActivity(i);
                 }else {
                     Toast.makeText(SignInActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                 }
             }
        }

        @Override
        public void onFailure(Call<RegisterResponse> call, Throwable t) {
            Log.d("Signin >>> ", "onFailure: "+t.getMessage());
        }
    };
}