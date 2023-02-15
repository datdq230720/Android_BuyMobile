package com.example.androidserver_asm.Constant.Retrofits;

import com.example.androidserver_asm.Constant.Requests.RegisterRequest;
import com.example.androidserver_asm.Constant.Requests.UploadRequests;
import com.example.androidserver_asm.Constant.Responses.ProductResponse;
import com.example.androidserver_asm.Constant.Responses.RegisterResponse;
import com.example.androidserver_asm.Constant.Responses.UploadReponses;
import com.example.androidserver_asm.Models.Categories;
import com.example.androidserver_asm.Models.Products;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRetrofitService {

    @POST("/views/user_register.php")
    Call<RegisterResponse> register(@Body RegisterRequest body);

    @POST("/views/user-login.php")
    Call<RegisterResponse> login(@Body RegisterRequest body);

    @GET ("/views/getAllProduct.php")
    Call<List<Products>> getAllProduct();

    @GET("/views/get-one-product.php")
    Call<Products> getOneProduct (@Query("id") Integer id);

    @GET("/views/get-categories.php")
    Call<List<Categories>> getCategories();

    @GET("/views/get-one-category.php")
    Call<Categories> getOneCategory(@Query("id") Integer id);

    @POST("/views/insert-category.php")
    Call<Categories> insertCategory(@Body Categories body);

    @POST("/views/update-category.php")
    Call<ProductResponse> updateCategory(@Body Categories body);

    @GET("/views/delete-category.php")
    Call<ProductResponse> deleteCategory(@Query("id") Integer id);

    @POST("/views/upload.php")
    Call<UploadReponses> upload (@Body UploadRequests body);

    @POST("/views/insert-product.php")
    Call<ProductResponse> insertProduct (@Body Products body);

    @POST("/views/update-product.php")
    Call<ProductResponse> updateProduct(@Body Products body);

    @GET("/views/delete-product.php")
    Call<ProductResponse> deleteProduct(@Query("id") Integer id);

    @GET("/views/forgot-password.php")
    Call<RegisterResponse> forgotPassword(@Query("email") String email);


}
