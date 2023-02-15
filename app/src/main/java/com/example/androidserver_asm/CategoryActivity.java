package com.example.androidserver_asm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.androidserver_asm.Adapter.CategoriesAdapter;
import com.example.androidserver_asm.Constant.Responses.ProductResponse;
import com.example.androidserver_asm.Constant.Retrofits.IRetrofitService;
import com.example.androidserver_asm.Constant.Retrofits.RetrofitBuiler;
import com.example.androidserver_asm.Models.Categories;
import com.example.androidserver_asm.Models.Products;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    ListView lv;
    CategoriesAdapter categoriesAdapter;
    IRetrofitService service;
    ImageView iv_back, iv_add;
    Dialog dialog;
    List<Categories> list;
    int ID;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        lv = findViewById(R.id.category_lv);
        iv_back = findViewById(R.id.category_back);
        iv_add = findViewById(R.id.category_add);

        service = RetrofitBuiler.createService(IRetrofitService.class);

        loadCategory();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CategoryActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(CategoryActivity.this, 0);
            }
        });

        lv.setOnItemLongClickListener((adapterView, view, position, id) -> {

            Categories categories = list.get(position);
            PopupMenu popupMenu = new PopupMenu(CategoryActivity.this, view);
            popupMenu.inflate(R.menu.setting_item);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.menu_edit:
                            ID = categories.getId();
                            name = categories.getName();
                            openDialog(CategoryActivity.this, 1);
                            return true;
                        case R.id.menu_delete:
                            service.deleteCategory(categories.getId()).enqueue(deleteCategory);
                            loadCategory();
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popupMenu.show();
            return true;
        });

    }

    public void loadCategory(){
        service.getCategories().enqueue(getAllCaregory);
    }
    Callback<List<Categories>> getAllCaregory = new Callback<List<Categories>>() {
        @Override
        public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response) {
            if (response.isSuccessful()){
                List<Categories> _list = response.body();
                list = _list;
                categoriesAdapter = new CategoriesAdapter(CategoryActivity.this, _list);
                lv.setAdapter(categoriesAdapter);
            }
        }

        @Override
        public void onFailure(Call<List<Categories>> call, Throwable t) {
            Log.d("getAllProduct >>> ", "onFailure: "+t.getMessage());
        }
    };

    protected void openDialog(final Context context, final int type){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_category);
        EditText ed_name;
        Button bt_Save;

        ed_name = dialog.findViewById(R.id.dag_name);
        bt_Save = dialog.findViewById(R.id.dag_save);

        if (type == 1){
            ed_name.setText(name);
        }

        bt_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ed_name.getText().toString();
                if (type == 0){
                    Categories categories = new Categories(name);
                    service.insertCategory(categories).enqueue(insertCategory);
                }else {
                    int id = ID;
                    Categories categories = new Categories(id, name);
                    service.updateCategory(categories).enqueue(updateCategory);
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    Callback<ProductResponse> deleteCategory = new Callback<ProductResponse>() {
        @Override
        public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

            ProductResponse Reponses = response.body();
            Log.d("update", "onResponse: "+Reponses.getResult());


            if (Reponses.getResult() == true){
                loadCategory();
            }
        }


        @Override
        public void onFailure(Call<ProductResponse> call, Throwable t) {
            Log.d("Detail one product >>> ", "onFailure: "+t.getMessage());
        }
    };
    Callback<ProductResponse> updateCategory = new Callback<ProductResponse>() {
        @Override
        public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

            loadCategory();

        }


        @Override
        public void onFailure(Call<ProductResponse> call, Throwable t) {
            Log.d("Detail one product >>> ", "onFailure: "+t.getMessage());
        }
    };
    Callback<Categories> insertCategory = new Callback<Categories>() {
        @Override
        public void onResponse(Call<Categories> call, Response<Categories> response) {

            if (response.isSuccessful()){
                loadCategory();

            }

        }


        @Override
        public void onFailure(Call<Categories> call, Throwable t) {
            Log.d("Detail one product >>> ", "onFailure: "+t.getMessage());
        }
    };
}