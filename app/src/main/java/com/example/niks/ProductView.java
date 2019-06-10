package com.example.niks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.niks.ApiHelper.JSONField;

public class ProductView extends AppCompatActivity {
    TextView prdname,prdweigth,prdprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        prdname =  findViewById(R.id.prdName);
        prdweigth = findViewById(R.id.prdWeight);
        prdprice =  findViewById(R.id.prdPrice);

        Intent intent = getIntent();
        final String pid = intent.getStringExtra(JSONField.PRODUCT_ID);
        String pname =  intent.getStringExtra(JSONField.PRODUCT_NAME);
        String pprice =  intent.getStringExtra(JSONField.PRODUCT_PRICE);
        String pweight =  intent.getStringExtra(JSONField.PRODUCT_WEIGHT);
        prdname.setText(pname);
        prdprice.setText(pprice);
        prdweigth.setText(pweight);

    }
}
