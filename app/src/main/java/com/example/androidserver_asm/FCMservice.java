package com.example.androidserver_asm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class FCMservice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcmservice);
    }

    public void getToken(View view){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()){
                    Log.d(">>>>>>>>>>", "get Token error: " + task.getException());
                    return;
                }
                String token = task.getResult();
                Log.i(">>>>>>>>>>", "get Token: " + token);
            }
        });
    }
}

// token: cJ4mzSIHT1W51tVBZ-bLk_:APA91bEdVsnx7evI2GPmob_0tVqegyAJECx8_zV0QCOrHvkOpl7HN6O2rW-sRMMYTrXGO0zbHfs0DLeagFM8J_F6SoCRWv_OAt8ZNranjOtFFSQggMjgE_MENjKA5E3mAAXQmuX97AFN