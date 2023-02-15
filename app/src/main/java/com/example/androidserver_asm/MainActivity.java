package com.example.androidserver_asm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androidserver_asm.Constant.Requests.RegisterRequest;
import com.example.androidserver_asm.Constant.Responses.RegisterResponse;
import com.example.androidserver_asm.Constant.Retrofits.IRetrofitService;
import com.example.androidserver_asm.Constant.Retrofits.RetrofitBuiler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText ed_accont, ed_pass;
    Button bt_login;
    TextView tv_signUp, tv_forgot;

    IRetrofitService service;

    private  boolean setEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_accont = findViewById(R.id.SignIn_account);
        ed_pass = findViewById(R.id.SignIn_password);
        bt_login = findViewById(R.id.SignIn_bt);
        tv_signUp = findViewById(R.id.SignIn_signUp);
        tv_forgot = findViewById(R.id.SignIn_forgot);

        ed_accont.setText("avcc@gmail.com");
        ed_pass.setText("123");

        service = RetrofitBuiler.createService(IRetrofitService.class);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = ed_accont.getText().toString();
                String password = ed_pass.getText().toString();

                RegisterRequest registerRequest = new RegisterRequest(account, password);
                service.login(registerRequest).enqueue(login);
                Log.d("Login >>> ", "onResponse1: "+setEmail);

            }
        });
        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(i);
            }
        });
        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ForgotPassActivity.class);
                startActivity(i);
            }

        });

    }

    Callback<RegisterResponse> login = new Callback<RegisterResponse>() {
        @Override
        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
            if (response.isSuccessful()){
                RegisterResponse registerResponse = response.body();
                setEmail = registerResponse.getResult();
                Log.d("Login >>> ", "onResponse: "+setEmail);
                if (setEmail == true){
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(i);
                }
            }
        }

        @Override
        public void onFailure(Call<RegisterResponse> call, Throwable t) {
            Log.d("Login >>> ", "onFailure: "+t.getMessage());
        }
    };
}