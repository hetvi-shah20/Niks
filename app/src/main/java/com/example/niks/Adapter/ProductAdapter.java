package com.example.niks.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.niks.Model.Product;
import com.example.niks.R;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    ArrayList<Product> listProduct;

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Product product = listProduct.get(i);
        String name = product.getProduct_name();
        viewHolder.tvProduct.setText(name);
        String price = product.getProduct_price();
        viewHolder.tvPrice.setText(price);
        String weight = product.getProduct_weight();
        viewHolder.tvWeight.setText(weight);

    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvProduct,tvPrice,tvWeight;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProduct = itemView.findViewById(R.id.tvProduct);
            tvPrice =itemView.findViewById(R.id.tvPrice);
            tvWeight =itemView.findViewById(R.id.tvWeight);
        }
    }
}
