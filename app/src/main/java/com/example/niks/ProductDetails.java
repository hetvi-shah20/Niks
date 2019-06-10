package com.example.niks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.niks.Adapter.ProductAdapter;
import com.example.niks.Adapter.SubCategoryAdapter;
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;
import com.example.niks.Listner.ProductViewClickListner;
import com.example.niks.Model.Product;
import com.example.niks.Model.SubCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductDetails extends AppCompatActivity implements ProductViewClickListner {

    RecyclerView rvProduct;
    ArrayList<Product> listProducts;
    private String id;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        rvProduct =  findViewById(R.id.rvProduct);
        Intent intent =  getIntent();
        id  =  intent.getStringExtra(JSONField.SUBCATEGORY_ID);
        name = intent.getStringExtra(JSONField.SUBCATEGORY_NAME);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductDetails.this);
        rvProduct.setLayoutManager(linearLayoutManager);
        
        getProduct(id);

    }

    private void getProduct(final String id) {
        listProducts =  new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.KEY_PRODUCT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params =  new HashMap<>();
                params.put(JSONField.SUBCATEGORY_ID,id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetails.this);
        requestQueue.add(stringRequest);
    }

    private void parseJSON(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            int success = jsonObject.optInt(JSONField.SUCCESS);
            if(success == 1)
            {
                JSONArray jsonArray = jsonObject.optJSONArray(JSONField.PRODUCT_ARRAY);
                if(jsonArray.length() > 0)
                {
                    for(int i =0 ;i<jsonArray.length();i++)
                    {
                        JSONObject jsonSubCategory = jsonArray.getJSONObject(i);
                        String productid = jsonSubCategory.getString(JSONField.PRODUCT_ID);
                        String productname = jsonSubCategory.getString(JSONField.PRODUCT_NAME);
                        String productprice = jsonSubCategory.getString(JSONField.PRODUCT_PRICE);
                        String productweight = jsonSubCategory.getString(JSONField.PRODUCT_WEIGHT);

                        Product product = new Product();
                        product.setProduct_id(productid);
                        product.setProduct_name(productname);
                        product.setProduct_price(productprice);
                        product.setProduct_weight(productweight);

                        listProducts.add(product);


                    }
                    ProductAdapter productAdapter = new ProductAdapter(ProductDetails.this,listProducts);
                    productAdapter.setProductViewClickListner(ProductDetails.this);
                    rvProduct.setAdapter(productAdapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOnItemProductClick(ArrayList<Product> listProduct, int position) {

        Intent intent = new Intent(ProductDetails.this,IndividualProduct.class);
        Product product = listProduct.get(position);
        String productid = product.getProduct_id();
        String productName = product.getProduct_name();
        String productPrice = product.getProduct_price();
        String productWeight = product.getProduct_weight();
        intent.putExtra(JSONField.PRODUCT_ID,productid);
        intent.putExtra(JSONField.PRODUCT_NAME,productName);
        intent.putExtra(JSONField.PRODUCT_PRICE,productPrice);
        intent.putExtra(JSONField.PRODUCT_WEIGHT,productWeight);

        startActivity(intent);

    }
}
