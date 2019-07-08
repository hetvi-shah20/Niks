package com.example.niks;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.niks.Adapter.CategoryAdapter;
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;
import com.example.niks.Listner.CategoryItemClickListner;
import com.example.niks.Model.Category;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,CategoryItemClickListner {

    TextView yourName;
    UserSessionManager userSessionManager ;
    RecyclerView rvCategory;
    ArrayList<Category> listCategory;
    String name;
    ViewFlipper viewFlipper;
    NotificationBadge badge ;
    Toolbar toolbar;
    com.example.niks.Model.Cart cart =  new com.example.niks.Model.Cart();
    ArrayList<com.example.niks.Model.Cart> listCart = new ArrayList<com.example.niks.Model.Cart>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        yourName =  findViewById(R.id.YourName);
        rvCategory = findViewById(R.id.rvCategory);
        badge = findViewById(R.id.badge);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Niks");
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        userSessionManager =  new UserSessionManager(Navigation.this);
        if(userSessionManager.getLoginStatus())
        {
            Toast.makeText(this, "welcome", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent =  new Intent(Navigation.this,Login.class);
            startActivity(intent);
        }


        LinearLayoutManager LayoutManager = new LinearLayoutManager(Navigation.this);
        rvCategory.setLayoutManager(LayoutManager);
        getCategory();
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

//       name =  userSessionManager.getUserEmail();
//       yourName.setText(name);



        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.YourName);
        TextView navUserEmail = (TextView) headerView.findViewById(R.id.YourEmail);

        navUsername.setText(userSessionManager.getUserName());
        navUserEmail.setText(userSessionManager.getUserEmail());
        getCartRVData();
       // badge.setText(cart.getTotal_cart_items());
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
                            String total_items = productObject.optString(JSONField.TOTAL_CART_ITEMS);

                            cart.setCart_id(cart_id);
                            cart.setProduct_name(product_name);
                            cart.setProduct_image(product_image);
                            cart.setProduct_qty(product_quantity);
                            cart.setProduct_description(product_details);
                            cart.setProduct_unit_price(product_price);
                            cart.setProduct_amount(product_amount);
                            cart.setProduct_weigth(product_weight);
                            cart.setTotal_cart_items(total_items);
                            listCart.add(cart);
                            Log.d("items",total_items);



                        }







                    }


                }
            }

          //  badge.setText(cart.getTotal_cart_items());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void getCategory() {
        listCategory = new ArrayList<>();
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, WebURL.KEY_DISPLAY_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJson(response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Navigation.this);
        requestQueue.add(stringRequest);
    }

    private void parseJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JSONField.SUCCESS);
            if(flag == 1)
            {
                JSONArray jsonArray = jsonObject.optJSONArray(JSONField.CATEGORY_ARRAY);
                if(jsonArray.length() > 0)
                {
                    for(int i = 0;i<jsonArray.length();i++)
                    {
                        JSONObject objCategory = jsonArray.optJSONObject(i);
                        String categoryId = objCategory.getString(JSONField.CATEGORY_ID);
                        String categoryName = objCategory.getString(JSONField.CATEGORY_NAME);
                        String categoryImage = objCategory.getString(JSONField.CATEGORY_IMAGE);



                        Category category = new Category();
                        category.setCat_id(categoryId);
                        category.setCat_name(categoryName);
                        category.setCat_image(categoryImage);
                        listCategory.add(category);
                    }
                    CategoryAdapter categoryAdapter = new CategoryAdapter(Navigation.this,listCategory);
                    categoryAdapter.setCategoryItemClickListner(Navigation.this);
                    rvCategory.setAdapter(categoryAdapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setOnItemClicked(ArrayList<Category> listCategory, int position) {
        Intent intent =  new Intent(Navigation.this,Subcategory.class);
        Category category = listCategory.get(position);
        String category_id = category.getCat_id();
        String category_name = category.getCat_name();
        intent.putExtra(JSONField.CATEGORY_ID,category_id);
        intent.putExtra(JSONField.CATEGORY_NAME,category_name);
        Log.d("id",category_id);
        Log.d("name",category_name);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_About) {

        } else if (id == R.id.nav_Contact) {

        } else if (id == R.id.nav_notification) {

        } else if (id == R.id.nav_Logout) {
            userSessionManager.logout();
            Intent intent =  new Intent(Navigation.this,Login.class);
            startActivity(intent);

        } else if (id == R.id.nav_Orders) {

        }else if (id == R.id.nav_Rate) {

        }else if (id == R.id.nav_share) {


        }else if(id == R.id.nav_Profile)
        {
            Intent intent = new Intent(Navigation.this,Profile.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_settings)
        {
            Intent intent2 = new Intent(Navigation.this,Setting.class);
            startActivity(intent2);

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.cart,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.imageCart:
                Intent intent = new Intent(Navigation.this,Cart.class);
                startActivity(intent);
        }
        return true;
    }
}
