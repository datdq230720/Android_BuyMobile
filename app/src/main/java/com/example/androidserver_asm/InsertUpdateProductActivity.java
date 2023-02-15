package com.example.androidserver_asm;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ContentInfoCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.androidserver_asm.Adapter.CategoriesAdapter;
import com.example.androidserver_asm.Constant.Requests.UploadRequests;
import com.example.androidserver_asm.Constant.Responses.ProductResponse;
import com.example.androidserver_asm.Constant.Responses.UploadReponses;
import com.example.androidserver_asm.Constant.Retrofits.IRetrofitService;
import com.example.androidserver_asm.Constant.Retrofits.RetrofitBuiler;
import com.example.androidserver_asm.Models.Categories;
import com.example.androidserver_asm.Models.Products;

import java.io.ByteArrayOutputStream;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertUpdateProductActivity extends AppCompatActivity {

    EditText ed_name, ed_price, ed_quantity, ed_describes;
    TextView tv_title;
    ImageView iv;
    Spinner sp_category;
    Button bt_save;

    IRetrofitService service;

    private String path;
    Integer category_id;
    private Products products;
    Boolean setUpdate;
    List<Categories> list;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_update_product);

        ed_name = findViewById(R.id.insert_ed_name);
        ed_price = findViewById(R.id.insert_ed_price);
        ed_quantity = findViewById(R.id.insert_ed_quantity);
        ed_describes = findViewById(R.id.insert_ed_describes);
        tv_title = findViewById(R.id.insert_tv_title);
        iv = findViewById(R.id.insert_iv);
        sp_category = findViewById(R.id.insert_sp_categories);
        bt_save = findViewById(R.id.insert_bt_save);

        service = RetrofitBuiler.createService(IRetrofitService.class);

        service.getCategories().enqueue(getAllCategory);

        if(!checkCamera()){
            requestCamera();

        }

        Bundle bundle = getIntent().getExtras();
        setUpdate = (Boolean) bundle.get("setUpdate");
        if (bundle != null & setUpdate == true){
            id = (Integer) bundle.get("id_product");
            service.getOneProduct(id).enqueue(getOneProduct);
        }


        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTakePictureClick(view);
            }
        });
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ed_name.getText().toString();
                float price = Float.parseFloat(ed_price.getText().toString());
                int quantity = Integer.parseInt(ed_quantity.getText().toString());
                String describes = ed_describes.getText().toString();
                int category_id2 = category_id;


                if (setUpdate == true){
                    Products products = new Products(id, name, price, quantity, path, category_id2, describes);
                    service.updateProduct(products).enqueue(updateProduct);
                    finish();
                }else {
                    Products products = new Products(name, price, quantity, path, category_id2, describes);
                    service.insertProduct(products).enqueue(insertProduct);
                }

            }
        });
    }

    public Boolean checkCamera(){
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_DENIED){
            return false;
        }
        Log.d("123", "onCreate: ");
        return true;
    }
    public void requestCamera(){
        ActivityCompat.requestPermissions(
                this, new String[]{Manifest.permission.CAMERA}, 2000
        );
    }
    public void onTakePictureClick(View view){
        Intent pick=new Intent(Intent.ACTION_GET_CONTENT);
        pick.setType("image/*");
        // lấy từ camera
        Intent pho=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Tích hợp
        Intent chosser=Intent.createChooser(pick, "chon");

        chosser.putExtra(Intent.EXTRA_INITIAL_INTENTS,new Intent[]{pho});
        try {
            someActivityResultLauncher.launch(chosser);
        }catch (Exception e){
            Log.d("insert: ", "onTakePictureClick: "+e);
        }
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data.getExtras()!=null)
                        {
                            Bundle extras = data.getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            Uri uri=data.getData();
                            Log.d("TAG", "onActivityResult: "+uri);
                            iv.setImageBitmap(imageBitmap);
                            // upload base64 lên sv
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            imageBitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
                            byte[] bytes = baos.toByteArray();

                            String base64 = Base64.encodeToString(bytes, 0);
                            UploadRequests uploadRequests = new UploadRequests(base64);
                            service.upload(uploadRequests).enqueue(uploadImage);
                        }
                        else{
                            Uri uri=data.getData();
                            iv.setImageURI(uri);
                        }
                    }
//                    Bundle extras = result.getData().getExtras();
//                    Bitmap imageBitmap = (Bitmap) extras.get("data");
//                    iv.setImageBitmap(imageBitmap);
                }
            });


    Callback<UploadReponses> uploadImage = new Callback<UploadReponses>() {
        @Override
        public void onResponse(Call<UploadReponses> call, Response<UploadReponses> response) {
            if (response.isSuccessful()){
                UploadReponses uploadImageReponses = response.body();
                Log.d("upload", "onResponse: "+uploadImageReponses.getPath());
                path = uploadImageReponses.getPath();

            }
        }

        @Override
        public void onFailure(Call<UploadReponses> call, Throwable t) {
            Log.d("ínert >>> ", "onFailure: "+t.getMessage());
        }
    };
    Callback<ProductResponse> insertProduct = new Callback<ProductResponse>() {
        @Override
        public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
            if (response.isSuccessful()){
                ProductResponse Reponses = response.body();
                Log.d("upload", "onResponse: "+Reponses.getResult());
                Toast.makeText(InsertUpdateProductActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                if (Reponses.getResult() == true){
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<ProductResponse> call, Throwable t) {
            Log.d("insẻt >>> ", "onFailure: "+t.getMessage());
        }
    };
    Callback<List<Categories>> getAllCategory = new Callback<List<Categories>>() {
        @Override
        public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response) {
            if (response.isSuccessful()){
                list = response.body();
                List<Categories> categoriesList = new ArrayList<>();
                categoriesList.addAll(list);
                CategoriesAdapter categoriesAdapter =new CategoriesAdapter(InsertUpdateProductActivity.this, categoriesList);
                sp_category.setAdapter(categoriesAdapter);

                Integer index = sp_category.getSelectedItemPosition();
                category_id = list.get(index).getId();
            }
        }

        @Override
        public void onFailure(Call<List<Categories>> call, Throwable t) {
            Log.d("getAllProduct >>> ", "onFailure: "+t.getMessage());
        }
    };
    Callback<ProductResponse> updateProduct = new Callback<ProductResponse>() {
        @Override
        public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
            if (response.isSuccessful()){
                ProductResponse Reponses = response.body();
                Log.d("update", "onResponse: "+Reponses.getResult());
                Toast.makeText(InsertUpdateProductActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                if (Reponses.getResult() == true){
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<ProductResponse> call, Throwable t) {
            Log.d("update >>> ", "onFailure: "+t.getMessage());
        }
    };
    Callback<Products> getOneProduct = new Callback<Products>() {
        @Override
        public void onResponse(Call<Products> call, Response<Products> response) {
            if (response.isSuccessful()){
                Products products = response.body();


                Glide.with(InsertUpdateProductActivity.this).load(products.getImage()).into(iv);
                ed_price.setText(products.getPrice().toString());
                ed_name.setText(products.getName());
                ed_quantity.setText(products.getQuantity().toString());
                ed_describes.setText(products.getDescribes());
                for (int i=0; i < list.size(); i++){
                    if(products.getCategory_id() == list.get(i).getId()){
                        sp_category.setSelection(i);
                        category_id = products.getCategory_id();
                    }
                }
                Log.d("Detail one product >>> ", "onResponse: "+products);
            }
        }

        @Override
        public void onFailure(Call<Products> call, Throwable t) {
            Log.d("Detail one product >>> ", "onFailure: "+t.getMessage());
        }
    };
}