package com.example.niks;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextInputEditText  CustomerFlatNo,CustomerSteet,CustomerLandmark,CustomerAddPincode, CustomerAddCity, CustomerAddArea;
    TextView btnAddressAddAddress,DeliveryAddress;
    String productArray;
    String TotalAmount;
    LinearLayout llselectAddress,llShowAddress;
    UserSessionManager userSessionManager;
    String shippingid,shipFlatno,shipStreet,shipLandmark,shipArea,shipCity,shipPincode;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        userSessionManager = new UserSessionManager(this);


        Intent i = getIntent();
        productArray = i.getStringExtra(JSONField.DETAILS_ARRAY);
        TotalAmount = i.getStringExtra(JSONField.TOTAL_AMOUNT);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        toolbar.setTitle("Shipping Address");
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        btnAddressAddAddress = findViewById(R.id.btnAddAddress);
        CustomerFlatNo =  findViewById(R.id.etFlatNo);
        CustomerSteet = findViewById(R.id.etStreetName);
        CustomerLandmark = findViewById(R.id.etLandMark);
        CustomerAddArea = findViewById(R.id.etArea);
        CustomerAddCity = findViewById(R.id.etCity);
        CustomerAddPincode = findViewById(R.id.etPincode);
//        llselectAddress =  findViewById(R.id.selectAddress);
//        llShowAddress =  findViewById(R.id.showAddress);
      //  DeliveryAddress =  findViewById(R.id.deliveryAddress);


        btnAddressAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAddress();
            }
        });

//        Intent intent =  getIntent();
//        shippingid = intent.getStringExtra(JSONField.SHIPPING_ID);
//        shipFlatno = intent.getStringExtra(JSONField.SHIPPING_FLATNO);
//        shipStreet = intent.getStringExtra(JSONField.SHIPPING_STREET);
//        shipLandmark = intent.getStringExtra(JSONField.SHIPPING_LANDMARK);
//        shipArea = intent.getStringExtra(JSONField.SHIPPING_AREA);
//        shipCity =  intent.getStringExtra(JSONField.SHIPPING_CITY);
//
//        if(intent!= null)
//        {
//            String address = shipFlatno + "," + shipStreet +" " + shipLandmark + " " + shipArea + " " + shipCity + " "+ shipPincode ;
//            DeliveryAddress.setText(address);
//
//        }
//
//        Log.d("flat no",shipFlatno);
//
//
//


    }

    private void AddAddress() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.ADD_ADDRESS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseOrderJson(response);
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
                params.put(JSONField.KEY_USER_ID, userSessionManager.getUserId());
                Log.d("userid",userSessionManager.getUserId());
                params.put(JSONField.SHIPPING_FLATNO,CustomerFlatNo.getText().toString().trim());
                params.put(JSONField.SHIPPING_STREET,CustomerSteet.getText().toString().trim());
                params.put(JSONField.SHIPPING_LANDMARK,CustomerLandmark.getText().toString().trim());
                params.put(JSONField.SHIPPING_AREA,CustomerAddArea.getText().toString().trim());
                params.put(JSONField.SHIPPING_CITY,CustomerAddCity.getText().toString().trim());
                params.put(JSONField.SHIPPING_PINCODE,CustomerAddPincode.getText().toString().trim());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseOrderJson(String response) {
        Log.d("INSERT_RESPONSE", response);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JSONField.FLAG);
            String strMessage = jsonObject.optString(JSONField.MESSAGE);
            if (flag == 1) {
                btnAddressAddAddress.setEnabled(false);
                showOrderPlaceDialog(strMessage);
            } else {
                btnAddressAddAddress.setEnabled(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showOrderPlaceDialog(String strMessage) {

        final Dialog orderDialog = new Dialog(this);
        orderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        orderDialog.setCanceledOnTouchOutside(false);
        orderDialog.setCancelable(false);
        orderDialog.setContentView(R.layout.address_added_succesfully_placed_pop_up);
        orderDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        orderDialog.getWindow().getDecorView().setBackground(new ColorDrawable(Color.TRANSPARENT));
        Button btnContinue = orderDialog.findViewById(R.id.btn_place_dialog_continue);
        btnContinue.setEnabled(true);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDialog.dismiss();
                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(AddAddressActivity.this);
                taskStackBuilder.addParentStack(Navigation.class);
                Intent intent = new Intent(AddAddressActivity.this, MyAddressActivity.class);
                taskStackBuilder.addNextIntentWithParentStack(intent);
                taskStackBuilder.startActivities();
            }
        });
        orderDialog.show();

    }

    private boolean checkTotalAmount() {
        boolean isTotalAmountValid = false;
        if (TotalAmount != null && !TotalAmount.isEmpty() && !TotalAmount.equals("")) {
            isTotalAmountValid = true;
        } else {
            Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
            isTotalAmountValid = false;
        }
        return isTotalAmountValid;
    }


    private boolean checkProductArray() {
        boolean isProductArrayValid = false;
        if (productArray != null && !productArray.isEmpty() && !productArray.equals("")) {
            isProductArrayValid = true;
        } else {
            Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
            isProductArrayValid = false;
        }
        return isProductArrayValid;
    }


    private boolean checkFlatNo() {
        boolean isAddressValid = false;
        if (CustomerFlatNo.getText().toString().trim().length() > 0) {
            isAddressValid = true;
        } else {
            CustomerFlatNo.setError("Enter Address");
        }
        return isAddressValid;
    }
    private boolean checkStreet() {
        boolean isAddressValid = false;
        if (CustomerSteet.getText().toString().trim().length() > 0) {
            isAddressValid = true;
        } else {
            CustomerSteet.setError("Enter Address");
        }
        return isAddressValid;
    }
    private boolean checkLandmark() {
        boolean isAddressValid = false;
        if (CustomerLandmark.getText().toString().trim().length() > 0) {
            isAddressValid = true;
        } else {
            CustomerLandmark.setError("Enter Address");
        }
        return isAddressValid;
    }

    private boolean checkPincode() {
        boolean isAddressValid = false;
        if (CustomerAddPincode.getText().toString().trim().length() > 0) {
            isAddressValid = true;
        } else {
            CustomerAddPincode.setError("Enter Address");
        }
        return isAddressValid;
    }
    private boolean checkCity() {
        boolean isAddressValid = false;
        if (CustomerAddCity.getText().toString().trim().length() > 0) {
            isAddressValid = true;
        } else {
            CustomerAddCity.setError("Enter Address");
        }
        return isAddressValid;
    }


    private boolean checkArea() {
        boolean isAddressValid = false;
        if (CustomerAddCity.getText().toString().trim().length() > 0) {
            isAddressValid = true;
        } else {
            CustomerAddCity.setError("Enter Address");
        }
        return isAddressValid;
    }

}
