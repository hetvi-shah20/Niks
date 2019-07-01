package com.example.niks;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.niks.Adapter.PlaceOrderAdapter;
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;
import com.example.niks.Model.Cart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaceOrderActivity extends AppCompatActivity {
    RecyclerView rvPlaceOrder;
    ArrayList<Cart>  cartArrayList =  new ArrayList<>();
    private PlaceOrderAdapter placeOrderAdapter;
    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        userSessionManager = new UserSessionManager(this);
        rvPlaceOrder = findViewById(R.id.rvOrderItems);
       // placeOrderAdapter =  new PlaceOrderAdapter(this,cartArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvPlaceOrder.setLayoutManager(mLayoutManager);
        rvPlaceOrder.setItemAnimator(new DefaultItemAnimator());


        getCartItems();
    }

    private void getCartItems() {
        cartArrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.VIEW_CART_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
                Log.d("inside response",response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(JSONField.KEY_USER_ID,userSessionManager.getUserId());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseJSON(String response) {
        Log.d("TAG", "parseSubCategoryResponse: " + response);
        try {
            JSONObject jsonObject =new JSONObject(response);
            if(jsonObject.has(JSONField.FLAG))
            {
                int flag =jsonObject.optInt(JSONField.FLAG);
                String message =jsonObject.optString(JSONField.MESSAGE);
                if(flag == 1)
                {
                    JSONArray jsonArray =jsonObject.optJSONArray(JSONField.CART_ARRAY);

                    if(jsonArray.length() > 0)
                    {
                        for(int i = 0;i<jsonArray.length();i++)

                        {
                            com.example.niks.Model.Cart cart =  new com.example.niks.Model.Cart();
                            JSONObject productObject = jsonArray.optJSONObject(i);
                            String cart_id = productObject.optString(JSONField.CART_ID);
                            String product_id = productObject.optString(JSONField.PRODUCT_ID);
                            String product_name = productObject.optString(JSONField.PRODUCT_NAME);
                            String product_image = productObject.optString(JSONField.PRODUCT_IMAGE);
                            String product_quantity = productObject.optString(JSONField.PRODUCT_QTY);
                            String product_details = productObject.optString(JSONField.PRODUCT_DESCREPTION);
                            String product_amount = productObject.optString(JSONField.PRODUCT_AMOUNT);
                            String product_price = productObject.optString(JSONField.PRODUCT_UNIT_PRICE);
                            String product_weight = productObject.optString(JSONField.PRODUCT_WEIGHT);


                            cart.setCart_id(cart_id);
                            cart.setProduct_id(product_id);
                            cart.setProduct_name(product_name);
                            cart.setProduct_image(product_image);
                            cart.setProduct_qty(product_quantity);
                            cart.setProduct_description(product_details);
                            cart.setProduct_unit_price(product_price);
                            cart.setProduct_amount(product_amount);
                            cart.setProduct_weigth(product_weight);
                            Log.d("amount",product_amount);
                            cartArrayList.add(cart);


                        }




                    }

                }
                placeOrderAdapter =  new PlaceOrderAdapter(this,cartArrayList);
                rvPlaceOrder.setAdapter(placeOrderAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
