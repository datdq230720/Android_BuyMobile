package com.example.androidserver_asm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.androidserver_asm.Adapter.CategoriesAdapter;
import com.example.androidserver_asm.Constant.Retrofits.IRetrofitService;
import com.example.androidserver_asm.Constant.Retrofits.RetrofitBuiler;
import com.example.androidserver_asm.Models.Categories;
import com.example.androidserver_asm.Models.ID;
import com.example.androidserver_asm.Models.Products;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    ImageView iv_back, iv_img;
    TextView tv_price, tv_name, tv_quantity, tv_category;
    Button bt_buy;

    IRetrofitService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        iv_back = findViewById(R.id.detail_back_home_iv);
        iv_img = findViewById(R.id.detail_iv);
        tv_price = findViewById(R.id.detail_price);
        tv_name = findViewById(R.id.detail_name);
        tv_category = findViewById(R.id.detal_category);
        tv_quantity = findViewById(R.id.detail_quantity);
        bt_buy = findViewById(R.id.detail_by_bt);

        service = RetrofitBuiler.createService(IRetrofitService.class);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            Log.d("DetailActivity", "onCreate: Dữ liệu rỗng");
            return;
        }
        int id = (Integer) bundle.get("id_product");
        service.getOneProduct(id).enqueue(getOneProduct);



        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });
    }

    Callback<Categories> getOneCategory = new Callback<Categories>() {
        @Override
        public void onResponse(Call<Categories> call, Response<Categories> response) {
            if (response.isSuccessful()){
                Categories categories = response.body();
                tv_category.setText(categories.getName());
            }
        }

        @Override
        public void onFailure(Call<Categories> call, Throwable t) {
            Log.d("Detail one category >>> ", "onFailure: "+t.getMessage());
        }
    };
    Callback<Products> getOneProduct = new Callback<Products>() {
        @Override
        public void onResponse(Call<Products> call, Response<Products> response) {
            if (response.isSuccessful()){
                Products products = response.body();

                service.getOneCategory(products.getCategory_id()).enqueue(getOneCategory);

                Glide.with(DetailActivity.this).load(products.getImage()).into(iv_img);
                tv_price.setText(products.getPrice()+"$");
                tv_name.setText(products.getName());
                tv_quantity.setText("Số lượng :"+products.getQuantity());
                Log.d("Detail one product >>> ", "onResponse: "+products);
            }
        }

        @Override
        public void onFailure(Call<Products> call, Throwable t) {
            Log.d("Detail one product >>> ", "onFailure: "+t.getMessage());
        }
    };
}