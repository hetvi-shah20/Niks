package com.example.niks.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.niks.R;

import com.example.niks.Model.Cart;

import java.util.ArrayList;

public class PlaceOrderAdapter extends RecyclerView.Adapter<PlaceOrderAdapter.ViewHolder> {

    private Context context;
    ArrayList<Cart> listCart;
    Cart cart ;

    public PlaceOrderAdapter(Context context, ArrayList<Cart> listCart) {
        this.context = context;
        this.listCart = listCart;
        cart =  new Cart();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_checkout_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Cart cart = listCart.get(i);
        String Productname = cart.getProduct_name();
        String ProductId = cart.getProduct_id();
        String Productprice = cart.getProduct_unit_price();
        String ProductWeigth = cart.getProduct_weigth();
        String ProductQuntity = cart.getProduct_qty();
        String ProductTotalAmount = cart.getProduct_amount();
        viewHolder.name.setText(Productname);
        Log.d("name",Productname);
        viewHolder.quntity.setText(ProductQuntity);
        viewHolder.weight.setText(ProductWeigth);
        viewHolder.price.setText(Productprice);


    }

    @Override
    public int getItemCount() {
        Log.d("size", String.valueOf(listCart.size()));
        return listCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,price,weight,quntity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_product_name_po);
            price = itemView.findViewById(R.id.tv_product_discount_price_po);
            weight = itemView.findViewById(R.id.tv_weight_po);
            quntity =  itemView.findViewById(R.id.tvQty_po);
        }
    }
}
