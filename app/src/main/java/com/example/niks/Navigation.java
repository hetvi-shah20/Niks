package com.example.niks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.niks.Adapter.CategoryAdapter;
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;
import com.example.niks.Model.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView yourName;
    UserSessionManager userSessionManager ;
    RecyclerView rvCategory;
    ArrayList<Category> listCategory;
    String name;
    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        yourName =  findViewById(R.id.YourName);
        rvCategory = findViewById(R.id.rvCategory);
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
                    rvCategory.setAdapter(categoryAdapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
}
