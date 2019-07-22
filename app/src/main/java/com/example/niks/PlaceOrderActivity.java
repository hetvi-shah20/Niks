package com.example.niks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
    TextView tvSelectAddress,tvTotalItems,tvTotalAmount,deliveryAddress;
    Toolbar  toolbar;
    JSONObject jsonProduct;
    LinearLayout lldeliveyAddress,llselectAddress;
     String totalitems,totalamounts;
     String productArray;
    String shipid,shipflatno,shipStreet,shipLandmark,shipArea,ShipCity,ShipPincode;
    public static final String main_key = "my_pref";
    public static final String item_key = "itemKey";
    public static final String amount_key = "amountKey";
    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        userSessionManager = new UserSessionManager(this);
        rvPlaceOrder = findViewById(R.id.rvOrderItems);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvSelectAddress = findViewById(R.id.tvSelectAddress);
        tvTotalItems = findViewById(R.id.tvTotalItems);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        lldeliveyAddress = findViewById(R.id.llDeliveryAddress);
        llselectAddress =  findViewById(R.id.selectAddress);
        deliveryAddress = findViewById(R.id.deliveryAddress);



        setSupportActionBar(toolbar);
        toolbar.setTitle("Place Order");
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


       final Intent intent = getIntent();
        totalitems = intent.getStringExtra(JSONField.TOTAL_CART_ITEMS);
        totalamounts = intent.getStringExtra(JSONField.TOTAL_AMOUNT);
        productArray = intent.getStringExtra(JSONField.DETAILS_ARRAY);
        Log.d("product array",productArray);
        tvTotalItems.setText(totalitems);
        //Log.d("totalitems",totalitems);
        tvTotalAmount.setText("₹"+totalamounts);




//
//        sharedPreferences = getSharedPreferences(main_key,MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(item_key,totalitems);
//        editor.putString(amount_key,totalamounts);
//        editor.commit();

       // placeOrderAdapter =  new PlaceOrderAdapter(this,cartArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvPlaceOrder.setLayoutManager(mLayoutManager);
        rvPlaceOrder.setItemAnimator(new DefaultItemAnimator());


        getCartItems();
        tvSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 =  new Intent(PlaceOrderActivity.this,MyAddressActivity.class);
                intent1.putExtra(JSONField.DETAILS_ARRAY,productArray);
                intent1.putExtra(JSONField.TOTAL_AMOUNT,totalamounts);
                intent1.putExtra(JSONField.TOTAL_CART_ITEMS,totalitems);
                startActivity(intent1);

            }
        });



        if (getIntent().hasExtra(JSONField.SHIPPING_ID)) {
            deliveryAddress();
        }

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

    @Override
    protected void onResume() {
        super.onResume();
        final Intent intent = getIntent();
        totalitems = intent.getStringExtra(JSONField.TOTAL_CART_ITEMS);
        totalamounts = intent.getStringExtra(JSONField.TOTAL_AMOUNT);
        tvTotalItems.setText(totalitems);
        Log.d("totalitems",totalitems);
        tvTotalAmount.setText("₹"+totalamounts);

    }



    private void deliveryAddress() {
        Intent intent1 = getIntent();
        shipid = intent1.getStringExtra(JSONField.SHIPPING_ID);
        shipflatno = intent1.getStringExtra(JSONField.SHIPPING_FLATNO);
        shipArea = intent1.getStringExtra(JSONField.SHIPPING_AREA);
        shipLandmark = intent1.getStringExtra(JSONField.SHIPPING_LANDMARK);
        shipStreet = intent1.getStringExtra(JSONField.SHIPPING_STREET);
        ShipCity = intent1.getStringExtra(JSONField.SHIPPING_CITY);
        ShipPincode = intent1.getStringExtra(JSONField.SHIPPING_PINCODE);
        Log.d("pincode",ShipPincode);


        lldeliveyAddress.setVisibility(View.VISIBLE);
        String Address = " " +shipflatno + "," + shipStreet +"\n"+ " " + shipLandmark +"\n" + " " + shipArea +"\n" +" " + ShipCity + " " + "-" + ShipPincode ;
        Log.d("full Address",Address);
        deliveryAddress.setText(Address);


//        SharedPreferences sharedPreferences2 = getSharedPreferences(main_key,MODE_PRIVATE);
//        String prefAmount = sharedPreferences2.getString(amount_key,"");
//        String preItems = sharedPreferences2.getString(item_key,"");
//        Log.d("preAmount ",prefAmount);



//        final Intent intent = getIntent();
//        totalitems = intent.getStringExtra(JSONField.TOTAL_CART_ITEMS);
//        totalamounts = intent.getStringExtra(JSONField.TOTAL_AMOUNT);
//
//        tvTotalItems.setText(totalitems);
//        tvTotalAmount.setText("₹"+totalamounts);

    }
}
