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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;
import com.example.niks.Model.Cart;
import com.example.niks.Model.Product;
import com.example.niks.Navigation;
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
    Cart cart ;
    CartAdapter.SetOnProductItemClickListener onProductItemClickListener;

    public CartAdapter.SetOnProductItemClickListener getOnProductItemClickListener() {
        return onProductItemClickListener;
    }

    public void setOnProductItemClickListener(CartAdapter.SetOnProductItemClickListener onProductItemClickListener) {
        this.onProductItemClickListener = onProductItemClickListener;
    }


    public CartAdapter(Context context, ArrayList<Cart> listCart) {
        this.context = context;
        this.listCart = listCart;
        cart =  new Cart();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.cart_row_item,viewGroup,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {

        final Cart cart = listCart.get(i);
        viewHolder.tvProductName.setText(cart.getProduct_name());
        viewHolder.tvProductWeight.setText(cart.getProduct_weigth());
        viewHolder.tvProductPrice.setText("₹"+cart.getProduct_unit_price());
        viewHolder.tvQty.setText(cart.getProduct_qty());

        Glide.with(context).load(cart.getProduct_image()).into(viewHolder.iVProductImage);
        viewHolder.ll_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductView.class);

                i.putExtra(JSONField.PRODUCT_ID,cart.getProduct_id());
                i.putExtra(JSONField.PRODUCT_NAME,cart.getProduct_name());
                i.putExtra(JSONField.PRODUCT_PRICE,cart.getProduct_unit_price());
                i.putExtra(JSONField.PRODUCT_DESCREPTION,cart.getProduct_description());
                i.putExtra(JSONField.PRODUCT_WEIGHT,cart.getProduct_weigth());
                i.putExtra(JSONField.PRODUCT_IMAGE,cart.getProduct_image());
                Log.d("pid",cart.getCart_id());
                context.startActivity(i);
                ((com.example.niks.Cart)context).finish();
            }
        });

        viewHolder.ivRemoveProductFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CartID = cart.getCart_id();
                Log.d("cart id",CartID);
                removeCartProduct(CartID);
            }
        });
    }

    private void removeCartProduct(final String CartID) {
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, WebURL.REMOVE_FROM_CART_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(JSONField.CART_ID,CartID);
                Log.d("cart id",CartID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void parseJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has(JSONField.FLAG))
            {
                int flag = jsonObject.optInt(JSONField.FLAG);
                String Message = jsonObject.optString(JSONField.MESSAGE);
                if(flag == 1)
                {
                    Intent i = new Intent(context, com.example.niks.Cart.class);
                    context.startActivity(i);
                    ((com.example.niks.Cart)context).finish();
                    Toast.makeText(context,Message, Toast.LENGTH_SHORT).show();
                }else  if(flag == 0 && Message.equals("Please Try Again Something Went Wrong"))
                {
                    Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public interface SetOnProductItemClickListener {
        public void onProductItemClicked(Product products, int position);
    }

}
