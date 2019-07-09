package com.example.niks;

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
import com.example.niks.Adapter.OrderAdapter;
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;
import com.example.niks.Model.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {


    Toolbar toolbar;
    RecyclerView rvOrder;
    private ArrayList<Order> listOrder = new ArrayList<>();
    private OrderAdapter orderAdapter;
    UserSessionManager userSessionManager;

    LinearLayout llOrders,llNoInternetConnection,llEmptyState;
    TextView tvEmptyStateMessage;
    TextView tvErrorTitle;
    Button btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        userSessionManager = new UserSessionManager(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("My Orders");
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        rvOrder = findViewById(R.id.rv_order);
        orderAdapter = new OrderAdapter(this,listOrder);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvOrder.setLayoutManager(mLayoutManager);
        rvOrder.setItemAnimator(new DefaultItemAnimator());
      //  rvOrder.setAdapter(orderAdapter);

        getOrder();
    }

    private void getOrder() {

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

                            Order order = new Order();
                            order.setOrderID(order_id);
                            order.setDate(order_date);
                            order.setTotalAmount(order_amount);
                            listOrder.add(order);
                        }
                        rvOrder.setAdapter(orderAdapter);
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
