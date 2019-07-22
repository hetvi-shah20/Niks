package com.example.niks;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.niks.Adapter.CartAdapter;
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart extends AppCompatActivity {

    RecyclerView rvCart;
    private ArrayList<com.example.niks.Model.Cart> listCart = new ArrayList<com.example.niks.Model.Cart>();
    private CartAdapter cartAdapter;
    TextView tv_cart_amount;
    TextView tvCheckout;
    UserSessionManager userSessionManager;
    LinearLayout llCart,ll_amount;
    String totalAmount;
    JSONObject jsonProduct;
    Toolbar toolbar;
    String totalitems;
    JSONObject productObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        userSessionManager = new UserSessionManager(this);

        llCart = findViewById(R.id.ll_cart);
        tv_cart_amount = findViewById(R.id.tv_cart_amount);
        tvCheckout = findViewById(R.id.tvCheckout);
        rvCart = findViewById(R.id.rv_cart_product);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("My Cart");
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cartAdapter = new CartAdapter(this,listCart);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvCart.setLayoutManager(mLayoutManager);
        rvCart.setItemAnimator(new DefaultItemAnimator());
        rvCart.setAdapter(cartAdapter);

        ll_amount = findViewById(R.id.ll_amount);;

        tvCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAmount()) {
                    generateJSON();

                }
            }
        });

        getCartRVData();

    }

    private void generateJSON() {

        jsonProduct = new JSONObject();
        if (listCart.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            try {
                for (int i = 0; i < listCart.size(); i++) {
                    com.example.niks.Model.Cart cart = listCart.get(i);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(JSONField.PRODUCT_ID, cart.getProduct_id());
                    jsonObject.put(JSONField.PRICE, cart.getProduct_unit_price());
                    jsonObject.put(JSONField.QTY, cart.getProduct_qty());
                    jsonArray.put(jsonObject);
                }
                jsonProduct.put(JSONField.DETAILS_ARRAY, jsonArray);
                Log.d("JSON", jsonProduct.toString());
                Intent intent = new Intent(Cart.this, PlaceOrderActivity.class);
                totalitems = productObject.optString(JSONField.TOTAL_CART_ITEMS);
                intent.putExtra(JSONField.DETAILS_ARRAY,jsonProduct.toString());
                intent.putExtra(JSONField.TOTAL_AMOUNT,totalAmount);
                intent.putExtra(JSONField.TOTAL_CART_ITEMS,totalitems);
                Log.d("total amount",totalAmount);
                Log.d("total items",totalitems);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private String CalculateTotalAmount() {
        String AmountTotal = null;
        double sum = 0;
        if (listCart != null && listCart.size() != 0) {
            for (int i = 0; i < listCart.size(); i++) {
                sum = sum + Double.parseDouble(listCart.get(i).getProduct_amount());
                AmountTotal = String.valueOf(sum);
            }
        }
        return AmountTotal;
    }

    private boolean checkAmount() {
        boolean isAmountValid = false;
        if (ll_amount.getVisibility() == View.VISIBLE) {
            isAmountValid = true;
        } else {
            Toast.makeText(this, "Please Add Items in Cart and Try Again!", Toast.LENGTH_SHORT).show();
            isAmountValid = false;
        }
        return isAmountValid;
    }



    private void getCartRVData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.VIEW_CART_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseUpdateCartResponse(response);
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

    private void parseUpdateCartResponse(String response) {

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
                            productObject = jsonArray.optJSONObject(i);
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
                            listCart.add(cart);





                        }

                        cartAdapter.notifyDataSetChanged();

                        String TotalAmount = CalculateTotalAmount();

                        if (TotalAmount != null && !TotalAmount.isEmpty() && !TotalAmount.equals("")) {
                            String Total = TotalAmount;
                            double total = Double.parseDouble(Total);
                            DecimalFormat df1 = new DecimalFormat("0.00");
                            df1.setMaximumFractionDigits(2);
                            totalAmount = (df1.format(total));
                            Log.d("Total Amount", totalAmount);
                            tv_cart_amount.setText("₹" + totalAmount);
                            ll_amount.setVisibility(View.VISIBLE);
                            llCart.setVisibility(View.VISIBLE);
                                 tvCheckout.setBackgroundColor(Color.parseColor("#CC0000"));
                        } else {
                            ll_amount.setVisibility(View.GONE);
                            llCart.setVisibility(View.GONE);
                            tvCheckout.setBackgroundColor(Color.parseColor("#CC0000"));
                        }

                    }
                    else if (flag == 0 && message.equals("No Record Found.")) {
                        //Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
                        llCart.setVisibility(View.GONE);

                    } else {

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        llCart.setVisibility(View.GONE);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
