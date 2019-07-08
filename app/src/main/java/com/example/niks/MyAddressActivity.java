package com.example.niks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.niks.Adapter.AddressAdapter;
import com.example.niks.Adapter.SubCategoryAdapter;
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;
import com.example.niks.Model.Shipping;
import com.example.niks.Model.SubCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAddressActivity extends AppCompatActivity {

    RecyclerView rvAddress;
    Toolbar toolbar;
    ArrayList<Shipping> Addresslist;
    UserSessionManager userSessionManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        rvAddress =  findViewById(R.id.rvAddress);

        userSessionManager =  new UserSessionManager(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("My Address");
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyAddressActivity.this);
        rvAddress.setLayoutManager(linearLayoutManager);
        getAddresses();
    }

    private void getAddresses() {
        Addresslist =  new ArrayList<>();
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, WebURL.VIEW_ADDRESS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
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
        RequestQueue requestQueue = Volley.newRequestQueue(MyAddressActivity.this);
        requestQueue.add(stringRequest);
    }

    private void parseJSON(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);
            if(success == 1)
            {
                JSONArray jsonArray = jsonObject.optJSONArray(JSONField.SHIPPING_ARRAY);
                if(jsonArray.length() > 0)
                {
                    for(int i =0 ;i<jsonArray.length();i++)
                    {
                        JSONObject jsonAddress = jsonArray.getJSONObject(i);
                        String shippingid = jsonAddress.getString(JSONField.SHIPPING_ID);
                        String shippingflatno = jsonAddress.getString(JSONField.SHIPPING_FLATNO);
                        String shippingStreet = jsonAddress.getString(JSONField.SHIPPING_STREET);
                        String shippingLandmark = jsonAddress.getString(JSONField.SHIPPING_LANDMARK);
                        String shippingArea = jsonAddress.getString(JSONField.SHIPPING_AREA);
                        String shippingCity = jsonAddress.getString(JSONField.SHIPPING_CITY);
                        String shippingPincode = jsonAddress.getString(JSONField.SHIPPING_PINCODE);

                        Shipping shipping = new Shipping();
                        shipping.setShipping_id(shippingid);
                        shipping.setShipping_flatno(shippingflatno);
                        shipping.setShipping_street(shippingStreet);
                        shipping.setShipping_landmark(shippingLandmark);
                        shipping.setShipping_area(shippingArea);
                        shipping.setShipping_city(shippingCity);
                        shipping.setShipping_pincode(shippingPincode);
                        Addresslist.add(shipping);

                    }
                    AddressAdapter addressAdapter = new AddressAdapter(this,Addresslist);
                    rvAddress.setAdapter(addressAdapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_address,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.addAddressiv:
                Intent intent = new Intent(MyAddressActivity.this,AddAddressActivity.class);
                startActivity(intent);
        }
        return true;
    }
}
