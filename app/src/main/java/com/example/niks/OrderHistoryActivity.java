package com.example.niks;

import android.content.Intent;
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
import com.example.niks.Adapter.OrderDetailsAdapter;
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;
import com.example.niks.Model.OrderDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderHistoryActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvOrderDetailsDate, tvOrderId, tvOrderDetailsTotalAmount,username;

    String OrderID, Date, TotalAmount;

    RecyclerView rvOrderDetails;

    private ArrayList<OrderDetails> listOrderDetails = new ArrayList<>();
    private OrderDetailsAdapter orderDetailsAdapter;

    UserSessionManager userSessionManager;

    LinearLayout llOrderDetails,llNoInternetConnection,llEmptyState;
    TextView tvEmptyStateMessage;
    TextView tvErrorTitle;
    Button btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);


        userSessionManager = new UserSessionManager(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Order Details");
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent1 = getIntent();
        OrderID = intent1.getStringExtra("OrderID");

        tvOrderDetailsDate = findViewById(R.id.tv_order_details_date);
        tvOrderId = findViewById(R.id.tv_order_details_id);
        tvOrderDetailsTotalAmount = findViewById(R.id.tv_order_details_gross_amount);
        username = findViewById(R.id.tv_order_username);


        rvOrderDetails = findViewById(R.id.rv_order_details);
        orderDetailsAdapter = new OrderDetailsAdapter(listOrderDetails,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvOrderDetails.setLayoutManager(mLayoutManager);
        rvOrderDetails.setItemAnimator(new DefaultItemAnimator());
        //orderDetailsAdapter.setOnProductItemClickListener(ProductActivity.this);
      //  rvOrderDetails.setAdapter(orderDetailsAdapter);

        getOrderDetailsData();

    }

    private void getOrderDetailsData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.ORDER_LISTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =  new HashMap<>();
                params.put(JSONField.KEY_USER_ID,userSessionManager.getUserId());
                params.put(JSONField.ORDER_ID,OrderID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseResponse(String response) {
        Log.d("TAG", "parseSubCategoryResponse: " + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has(JSONField.FLAG)) {
                int flag = jsonObject.optInt(JSONField.FLAG);
                if (flag == 1) {

                    JSONArray arrOrder = jsonObject.optJSONArray(JSONField.PRODUCT_ARRAY);
                    if (arrOrder.length() > 0) {
                        for (int i = 0; i < arrOrder.length(); i++) {
                            JSONObject orderObject = arrOrder.optJSONObject(i);
                            String order_id = orderObject.optString(JSONField.ORDER_ID);
                            String order_date = orderObject.optString(JSONField.ORDER_DATE);
                            String order_amount = orderObject.optString(JSONField.TOTAL_AMOUNT);

                            tvOrderId.setText(order_id);
                            tvOrderDetailsDate.setText(order_date);
                            username.setText(userSessionManager.getUserName());


                            String Total = order_amount;
                            double total = Double.parseDouble(Total);
                            DecimalFormat df1 = new DecimalFormat("0.00");
                            df1.setMaximumFractionDigits(2);
                            String totalFinalAmount = (df1.format(total));

                            tvOrderDetailsTotalAmount.setText("₹"+totalFinalAmount);

                            JSONArray arrOrderDetails = orderObject.optJSONArray(JSONField.ORDER_DETAILS);
                            for(int j = 0; j<arrOrderDetails.length() ; j++)
                            {
                                JSONObject orderDetailsObject = arrOrderDetails.optJSONObject(j);
                                String product_id = orderDetailsObject.optString(JSONField.PRODUCT_ID);
                                String product_name = orderDetailsObject.optString(JSONField.PRODUCT_NAME);
                                String quantity = orderDetailsObject.optString(JSONField.QTY);
                                String price = orderDetailsObject.optString(JSONField.PRICE);

                                OrderDetails orderDetails = new OrderDetails();
                                orderDetails.setProductName(product_name);
                                orderDetails.setQuantity(quantity);


                                double Price = Double.parseDouble(price);
                                double Qty = Double.parseDouble(quantity);
                                double totalAmount = Price*Qty;
                                DecimalFormat df2 = new DecimalFormat("0.00");
                                df2.setMaximumFractionDigits(2);
                                String totalAmt = (df2.format(totalAmount));

                                String pPrice = (df2.format(Price));

                                orderDetails.setPrice(pPrice);
                                orderDetails.setAmount(totalAmt);
                                listOrderDetails.add(orderDetails);


                            }
                            rvOrderDetails.setAdapter(orderDetailsAdapter);
                        }

                    }
                } else {

                    String Message = jsonObject.optString(JSONField.MESSAGE);
                    Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();

                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
}
