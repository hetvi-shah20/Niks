package com.example.niks.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.niks.Listner.ProductViewClickListner;
import com.example.niks.Model.Product;
import com.example.niks.R;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    ArrayList<Product> listProduct;

    ProductViewClickListner productViewClickListner;

    public ProductViewClickListner getProductViewClickListner() {
        return productViewClickListner;
    }

    public void setProductViewClickListner(ProductViewClickListner productViewClickListner) {
        this.productViewClickListner = productViewClickListner;
    }

    public ProductAdapter(Context context, ArrayList<Product> listProduct) {
        this.context = context;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.product_row_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {
        Product product = listProduct.get(i);
        String name = product.getProduct_name();
        viewHolder.tvProduct.setText(name);
        String price = product.getProduct_price();
        viewHolder.tvPrice.setText(price);
        String weight = product.getProduct_weight();
        viewHolder.tvWeight.setText(weight);
        viewHolder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductViewClickListner listner = getProductViewClickListner();
                listner.setOnItemProductClick(listProduct,i);
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductViewClickListner listner = getProductViewClickListner();
                listner.setOnItemProductClick(listProduct,i);
            }
        });



    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvProduct,tvPrice,tvWeight;
        Button btnView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProduct = itemView.findViewById(R.id.tvProduct);
            tvPrice =itemView.findViewById(R.id.tvPrice);
            tvWeight =itemView.findViewById(R.id.tvWeight);
            btnView = itemView.findViewById(R.id.btnView);
        }

    }
}
