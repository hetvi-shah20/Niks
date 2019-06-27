package com.example.niks.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;
import com.example.niks.Model.Cart;
import com.example.niks.ProductView;
import com.example.niks.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {


    private Context context;
    ArrayList<Cart> listCart;

    public CartAdapter(Context context, ArrayList<Cart> listCart) {
        this.context = context;
        this.listCart = listCart;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.cart_row_item,viewGroup,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Cart cart = listCart.get(i);
        viewHolder.tvProductName.setText(cart.getProduct_name());
        viewHolder.tvProductWeight.setText(cart.getProduct_weigth());
        viewHolder.tvProductPrice.setText("₹"+cart.getProduct_unit_price());
        viewHolder.tvQty.setText(cart.getProduct_qty());
        Glide.with(context).load(WebURL.KEY_SUBCAT_IMAGE_URL + cart.getProduct_image()).into(viewHolder.iVProductImage);

    }




    @Override
    public int getItemCount() {
        Log.d("size", String.valueOf(listCart.size()));
        return listCart.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvProductName, tvProductPrice,tvProductWeight;
        ImageView iVProductImage;
        TextView tvQty;
        ImageView ivRemoveProductFromCart;
        LinearLayout ll_add_to_cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice =itemView.findViewById(R.id.tv_product_discount_price);
            tvProductWeight = itemView.findViewById(R.id.tv_Weight);
            iVProductImage = itemView.findViewById(R.id.iv_product_image);
            tvQty = itemView.findViewById(R.id.tvQty);
            ivRemoveProductFromCart = itemView.findViewById(R.id.ivRemoveProductFromCart);
            ll_add_to_cart = itemView.findViewById(R.id.ll_add_to_cart);
        }
    }
}
