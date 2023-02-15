package com.example.androidserver_asm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.androidserver_asm.Adapter.ProductAdapter;
import com.example.androidserver_asm.Constant.Responses.ProductResponse;
import com.example.androidserver_asm.Constant.Retrofits.IRetrofitService;
import com.example.androidserver_asm.Constant.Retrofits.RetrofitBuiler;
import com.example.androidserver_asm.Models.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    List<Products> listProduct = new ArrayList<>();
    IRetrofitService service;




    RecyclerView rv;
    private ProductAdapter productAdapter;
    ImageView iv_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rv = findViewById(R.id.home_recyclerView);
        iv_add = findViewById(R.id.home_add);

        productAdapter = new ProductAdapter(HomeActivity.this);
        GridLayoutManager gird = new GridLayoutManager(HomeActivity.this, 2);
        rv.setLayoutManager(gird);

        Log.d("Home: ", "onCreate: ");
        service = RetrofitBuiler.createService(IRetrofitService.class);

        getToken();

        loadProducts();

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(HomeActivity.this, view);
                popupMenu.inflate(R.menu.add_home);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()){
                            case R.id.menu_ds_category:
                                Intent i = new Intent(HomeActivity.this, CategoryActivity.class);
                                startActivity(i);
                                return true;
                            case R.id.menu_add_category:
                                return true;
                            case R.id.menu_add_product:
                                Intent i2 = new Intent(HomeActivity.this, InsertUpdateProductActivity.class);
                                Bundle bundle = new Bundle();
                                Boolean setUpdate = false;
                                bundle.putSerializable("setUpdate", setUpdate);
                                i2.putExtras(bundle);
                                startActivity(i2);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

//        productAdapter = new ProductAdapter(this);
//        GridLayoutManager gird = new GridLayoutManager(this, 2);
//        rv.setLayoutManager(gird);
//        productAdapter.setDataProduct(listProduct);
//
//        rv.setAdapter(productAdapter);

    }

    public void getToken(){
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
    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    Callback<List<Products>> getAllProduct = new Callback<List<Products>>() {
        @Override
        public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
            if (response.isSuccessful()){
                List<Products> list = response.body();
                productAdapter.setDataProduct(list);
                rv.setAdapter(productAdapter);
            }
        }

        @Override
        public void onFailure(Call<List<Products>> call, Throwable t) {
            Log.d("getAllProduct >>> ", "onFailure: "+t.getMessage());
        }
    };



    public void loadProducts() {
        service.getAllProduct().enqueue(getAllProduct);
    }
}