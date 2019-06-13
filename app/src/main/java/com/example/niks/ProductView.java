package com.example.niks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;

import java.util.HashMap;
import java.util.Map;

public class ProductView extends AppCompatActivity {
    TextView prdname,prdweigth,prdprice;
    Button btnCart;
    UserSessionManager userSessionManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        prdname =  findViewById(R.id.product_name);
        prdweigth = findViewById(R.id.product_weight);
        prdprice =  findViewById(R.id.product_price);

        final Intent intent = getIntent();
        final String pid = intent.getStringExtra(JSONField.PRODUCT_ID);
        String pname =  intent.getStringExtra(JSONField.PRODUCT_NAME);
        String pprice =  intent.getStringExtra(JSONField.PRODUCT_PRICE);
        String pweight =  intent.getStringExtra(JSONField.PRODUCT_WEIGHT);
        prdname.setText(pname);
        prdprice.setText(pprice);
        prdweigth.setText(pweight);
        userSessionManager =  new UserSessionManager(this);


    }

}
