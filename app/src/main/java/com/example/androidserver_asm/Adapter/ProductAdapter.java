package com.example.androidserver_asm.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidserver_asm.Constant.Responses.ProductResponse;
import com.example.androidserver_asm.Constant.Retrofits.IRetrofitService;
import com.example.androidserver_asm.Constant.Retrofits.RetrofitBuiler;
import com.example.androidserver_asm.DetailActivity;
import com.example.androidserver_asm.HomeActivity;
import com.example.androidserver_asm.InsertUpdateProductActivity;
import com.example.androidserver_asm.Models.Products;
import com.example.androidserver_asm.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    private HomeActivity context;
    private List<Products> list;
    IRetrofitService service;

    public ProductAdapter(HomeActivity _context){
        this.context = _context;
    }
    public void setDataProduct(List<Products> _list){
        this.list = _list;
    }

    public void updateData(){
    }



    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.layout_product_item, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products products = list.get(position);
        if (products == null){
            Log.d("44: ", "onBindViewHolder: productAdapter");
            return;
        }
        service = RetrofitBuiler.createService(IRetrofitService.class);
        Glide.with(context).load(products.getImage()).into(holder.iv);
        Log.d("TAG", "onBindViewHolder: "+products.getImage());
        holder.tv_name.setText(products.getName());
        holder.tv_price.setText(products.getPrice().toString()+"$");

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  i = new Intent(context, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("id_product", products.getId());
                i.putExtras(bundle);
                context.startActivity(i);
            }
        });
        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.inflate(R.menu.setting_item);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()){
                            case R.id.menu_edit:
                                Intent  i = new Intent(context, InsertUpdateProductActivity.class);
                                Bundle bundle = new Bundle();
                                Boolean setUpdate = true;
                                bundle.putSerializable("id_product", products.getId());
                                bundle.putSerializable("setUpdate", setUpdate);
                                i.putExtras(bundle);
                                context.startActivity(i);
                                return true;
                            case R.id.menu_delete:
//                                service.deleteProduct(products.getId()).enqueue(deleteProduct);
                                xoa(products.getId());
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();

                return true;
            }
        });
        return;
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv;
        private TextView tv_name, tv_price;
        private CardView relativeLayout;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout =itemView.findViewById(R.id.layout_item_product);
            iv =itemView.findViewById(R.id.product_item_iv);
            tv_name = itemView.findViewById(R.id.product_item_name);
            tv_price = itemView.findViewById(R.id.product_item_price);
        }



    }
    Callback<ProductResponse> deleteProduct = new Callback<ProductResponse>() {
        @Override
        public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

                ProductResponse Reponses = response.body();
                Log.d("update", "onResponse: "+Reponses.getResult());


                if (Reponses.getResult() == true){
                    context.loadProducts();
                }
        }


        @Override
        public void onFailure(Call<ProductResponse> call, Throwable t) {
            Log.d("Detail one product >>> ", "onFailure: "+t.getMessage());
        }
    };

    public void xoa(final int Id){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage("Bạn có chắc chắn muốn xóa không");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", (DialogInterface dialog, int id) -> {
            service.deleteProduct(Id).enqueue(deleteProduct);

        });
        builder.setNegativeButton("No", (dialog, id) ->{
            dialog.cancel();
        });
        AlertDialog alert = builder.create();
        builder.show();
    }

}
