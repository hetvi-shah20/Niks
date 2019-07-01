package com.example.niks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ProductView extends AppCompatActivity {
    Toolbar toolbar;
    TextView prdname,prdweigth,prdprice,prdDescription;
    Button btnAddtoCard;
    UserSessionManager userSessionManager ;
    String pp;
    ImageView product_image;
    EditText etQty;
    String ProductID, ProductName, ProductPrice,ProductWeight, ProductDescription, ProductImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        userSessionManager = new UserSessionManager(this);
        prdname =  findViewById(R.id.product_name);
        prdweigth = findViewById(R.id.product_weight);
        prdprice =  findViewById(R.id.product_price);
        etQty =  findViewById(R.id.etQty);
        prdDescription = findViewById(R.id.product_description);
        btnAddtoCard =  findViewById(R.id.btnAddtoCard);
        product_image = findViewById(R.id.product_image);

      //  toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        final Intent intent = getIntent();
        ProductID = intent.getStringExtra(JSONField.PRODUCT_ID);
        ProductName =  intent.getStringExtra(JSONField.PRODUCT_NAME);
        ProductPrice =  intent.getStringExtra(JSONField.PRODUCT_PRICE);
        ProductWeight =  intent.getStringExtra(JSONField.PRODUCT_WEIGHT);
        ProductDescription =intent.getStringExtra(JSONField.PRODUCT_DESCREPTION);
        ProductImage = intent.getStringExtra(JSONField.PRODUCT_IMAGE);

//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });

        prdname.setText(ProductName);
        //prdprice.setText(pprice);

        String price = ProductPrice;
        double productPrice = Double.parseDouble(price);
        DecimalFormat df1 = new DecimalFormat("0.00");
        df1.setMaximumFractionDigits(2);
        pp = (df1.format(productPrice));
        prdprice.setText("₹" + pp);
        prdweigth.setText(ProductWeight);
        prdDescription.setText(ProductDescription);

        Glide.with(this).load(ProductImage).into(product_image);

//        product_image.setImageResource(ProductImage,R.drawable.automotive);
        btnAddtoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkProductID() && checkProductName() && checkProductPrice()  && checkProductQuantity()) {
                    if (btnAddtoCard.getTag().equals("INSERT")) {
                        AddProductToCart();
                        Log.d("Inside Insert Cart", "Yes");
                    } else {
                       updateCartProduct();
                        Log.d("Inside Update Cart", "Yes");
                    }
                }

            }
        });
    }

    private void getProductDetailsFromCart() {
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, WebURL.CART_PRODUCT_DETAIL_URL, new Response.Listener<String>() {
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
                Map<String, String> params = new HashMap<>();
                params.put(JSONField.KEY_USER_ID, userSessionManager.getUserId());
                params.put(JSONField.PRODUCT_ID, ProductID);
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
                String Message = jsonObject.optString(JSONField.MESSAGE);
                if (flag == 1) {

                    JSONArray arrProduct = jsonObject.optJSONArray(JSONField.CART_PRODUCT_ARRAY);
                    if (arrProduct.length() > 0) {
                        for (int i = 0; i < arrProduct.length(); i++) {
                            JSONObject productObject = arrProduct.optJSONObject(i);
                            String product_quantity = productObject.optString(JSONField.PRODUCT_QTY);

                            etQty.setText(product_quantity);
                            Log.d("Value of Quantity", product_quantity);
                            btnAddtoCard.setTag("Go To Cart");
                            btnAddtoCard.setText("Go To Cart");
                            btnAddtoCard.setVisibility(View.VISIBLE);
                        }
                    }
                } else if (flag == 0 && Message.equals("No Record Found.")) {
                    etQty.setText("");
                    btnAddtoCard.setText("ADD To cart");
                    btnAddtoCard.setTag("INSERT");
                } else {

                    etQty.setText("");
                    btnAddtoCard.setText("ADD To cart");
                    btnAddtoCard.setVisibility(View.GONE);
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    private void updateCartProduct() {


        String productQty = etQty.getText().toString().trim();
        if (ProductPrice != null && !ProductPrice.isEmpty() && !ProductPrice.equals("") && productQty != null && !productQty.isEmpty() && !productQty.equals("")) {
            double price = Double.parseDouble(ProductPrice);
            DecimalFormat df1 = new DecimalFormat("0.00");
            df1.setMaximumFractionDigits(2);
            String productPrice = (df1.format(price));
            Log.d("Product Price", productPrice);

            double amount = Double.parseDouble(ProductPrice) * Double.parseDouble(productQty);
            DecimalFormat df2 = new DecimalFormat("0.00");
            df1.setMaximumFractionDigits(2);
            String productAmount = (df2.format(amount));
            Log.d("Product Amount", productAmount);

            String UserID = userSessionManager.getUserId();

           updateCart(UserID,ProductID,productAmount,productPrice,productQty);


        } else {
            Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateCart(final String userID, final String productID, final String productAmount, final String productPrice, final String productQty) {

    StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.KEY_CART_UPDATE_URL, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            parseUpdateCartResponse(response);
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    })
    {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put(JSONField.KEY_USER_ID,userID);
            params.put(JSONField.PRODUCT_ID, productID);
            params.put(JSONField.PRODUCT_AMOUNT, productAmount);
            params.put(JSONField.PRODUCT_UNIT_PRICE, productPrice);
            params.put(JSONField.PRODUCT_QTY, productQty);
            return params;

        }
    };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void parseUpdateCartResponse(String response) {

        Log.d("TAG", "parseSubCategoryResponse: " + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has(JSONField.FLAG)) {
                int flag = jsonObject.optInt(JSONField.FLAG);
                String Message = jsonObject.optString(JSONField.MESSAGE);
                if (flag == 1) {
                    Intent i = new Intent(ProductView.this, Cart.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
                } else if (flag == 1 && Message.equals("Please Try Again Something Went Wrong")) {
                    Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }


    }



    private boolean checkProductQuantity() {
        boolean isProductQuantityValid = false;
        if (etQty.getText().toString().trim().length() > 0) {
            isProductQuantityValid = true;
        } else {
            etQty.setError("Enter Quantity");
        }
        return isProductQuantityValid;
    }

    private boolean checkProductDescription() {
        boolean isProductDescriptionValid = false;
        if (ProductDescription != null && !ProductDescription.isEmpty() && !ProductDescription.equals("")) {
            isProductDescriptionValid = true;
        } else {
            isProductDescriptionValid = false;
            Toast.makeText(this, "Product Description Not received!", Toast.LENGTH_SHORT).show();
        }
        return isProductDescriptionValid;
    }

    private boolean checkproductImage() {
        boolean isProductImageValid = false;
        if (ProductImage != null && !ProductImage.isEmpty() && !ProductImage.equals("")) {
            isProductImageValid = true;
        } else {
            isProductImageValid = false;
            Toast.makeText(this, "Product Image Not received!", Toast.LENGTH_SHORT).show();
        }
        return isProductImageValid;
    }

    private boolean checkProductPrice() {
        boolean isProductPriceValid = false;
        if (ProductPrice != null && !ProductPrice.isEmpty() && !ProductPrice.equals("")) {
            isProductPriceValid = true;
        } else {
            isProductPriceValid = false;
            Toast.makeText(this, "Product Price Not received!", Toast.LENGTH_SHORT).show();
        }
        return isProductPriceValid;
    }

    private boolean checkProductName() {
        boolean isProductNameValid = false;
        if (ProductName != null && !ProductName.isEmpty() && !ProductName.equals("")) {
            isProductNameValid = true;
        } else {
            isProductNameValid = false;
            Toast.makeText(this, "Product Name Not received!", Toast.LENGTH_SHORT).show();
        }
        return isProductNameValid;

    }


    private boolean checkProductID() {
        boolean isProductIDalid = false;
        if (ProductID != null && !ProductID.isEmpty() && !ProductID.equals("")) {
            isProductIDalid = true;
        } else {
            isProductIDalid = false;
            Toast.makeText(this, "Product ID Not received!", Toast.LENGTH_SHORT).show();
        }
        return isProductIDalid;
    }


    private void AddProductToCart() {

        String productQty = etQty.getText().toString().trim();

        if (ProductPrice != null && !ProductPrice.isEmpty() && !ProductPrice.equals("") && productQty != null && !productQty.isEmpty() && !productQty.equals("")) {
            double price = Double.parseDouble(ProductPrice);
            DecimalFormat df1 = new DecimalFormat("0.00");
            df1.setMaximumFractionDigits(2);
            String productPrice = (df1.format(price));
            Log.d("Product Price", productPrice);


            double amount = Double.parseDouble(ProductPrice) * Double.parseDouble(productQty);
            DecimalFormat df2 = new DecimalFormat("0.00");
            df1.setMaximumFractionDigits(2);
            String productAmount = (df2.format(amount));
            Log.d("Product Amount", productAmount);

            String UserID = userSessionManager.getUserId();

            AddToCart(UserID,ProductID,ProductName,productAmount,ProductWeight,ProductDescription,ProductImage,productQty,productPrice);

        } else {
            Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private void AddToCart(final String userID,final String productID, final String productName,final String productAmount,final String productWeight,final String productDescription,final String productImage,final String productQty,final String productPrice) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.KEY_CART_INSERT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseAddToCartResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(JSONField.KEY_USER_ID,userID);
                params.put(JSONField.PRODUCT_ID, productID);
                params.put(JSONField.PRODUCT_NAME, productName);
                params.put(JSONField.PRODUCT_AMOUNT, productAmount);
                params.put(JSONField.PRODUCT_WEIGHT, productWeight);
                params.put(JSONField.PRODUCT_DESCREPTION, productDescription);
                params.put(JSONField.PRODUCT_IMAGE, productImage);
                params.put(JSONField.PRODUCT_QTY, productQty);
                params.put(JSONField.PRODUCT_UNIT_PRICE, productPrice);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseAddToCartResponse(String response) {
        Log.d("TAG", "parseSubCategoryResponse: " + response);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            if (jsonObject.has(JSONField.FLAG)) {
                int flag = jsonObject.optInt(JSONField.FLAG);
                String Message = jsonObject.optString(JSONField.MESSAGE);
                if (flag == 1) {
                    Intent i = new Intent(ProductView.this, Cart.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
                }
                else if (flag == 1 && Message.equals("Please Try Again Something Went Wrong")) {
                    Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().hasExtra(JSONField.PRODUCT_ID)) {
            getProductDetailsFromCart();
        }
    }




}
