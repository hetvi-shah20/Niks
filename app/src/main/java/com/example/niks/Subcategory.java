package com.example.niks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.niks.Adapter.SubCategoryAdapter;
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;
import com.example.niks.Listner.SubCategoryItemClickListner;
import com.example.niks.Model.SubCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Subcategory extends AppCompatActivity implements SubCategoryItemClickListner {
    RecyclerView rvSubCategory;
    ArrayList<SubCategory> listSubCategory;
    private String id;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);
        rvSubCategory =  findViewById(R.id.rvSubCategory);
        Intent intent = getIntent();
        id = intent.getStringExtra(JSONField.CATEGORY_ID);
        name = intent.getStringExtra(JSONField.CATEGORY_NAME);

        GridLayoutManager gridLayoutManager= new GridLayoutManager(Subcategory.this,2);
        rvSubCategory.setLayoutManager(gridLayoutManager);

        getSubCategory(id);
    }

    private void getSubCategory(final String id) {
        listSubCategory = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.KEY_SUBCATEGORY_DISPLAY, new Response.Listener<String>() {
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
                Map<String,String> params =  new HashMap<>();
                params.put(JSONField.CATEGORY_ID,id);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Subcategory.this);
        requestQueue.add(stringRequest);
    }

    private void parseJSON(String s) {

        try {
            JSONObject jsonObject = new JSONObject(s);
            int success = jsonObject.optInt(JSONField.SUCCESS);
            if(success == 1)
            {
                JSONArray jsonArray = jsonObject.optJSONArray(JSONField.SUBCATEGORY_ARRAY);
                if(jsonArray.length() > 0)
                {
                    for(int i =0 ;i<jsonArray.length();i++)
                    {
                        JSONObject jsonSubCategory = jsonArray.getJSONObject(i);
                        String subcatid = jsonSubCategory.getString(JSONField.SUBCATEGORY_ID);
                        String subcatname = jsonSubCategory.getString(JSONField.SUBCATEGORY_NAME);
                        String subcatimage = jsonSubCategory.getString(JSONField.SUBCATEGORY_IMAGE);

                        SubCategory subCategory = new SubCategory();
                        subCategory.setSubcat_id(subcatid);
                        subCategory.setSubcat_name(subcatname);
                        subCategory.setSubcat_image(subcatimage);
                        listSubCategory.add(subCategory);

                    }
                    SubCategoryAdapter subCategoryAdapter =  new SubCategoryAdapter(Subcategory.this,listSubCategory);
                    subCategoryAdapter.setSubCategoryItemClickListner(Subcategory.this);
                    rvSubCategory.setAdapter(subCategoryAdapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOnSubcatClicked(ArrayList<SubCategory> listSubcat, int pos) {
        Intent intent = new Intent(Subcategory.this,ProductDetails.class);
        SubCategory subCategory = listSubCategory.get(pos);
        String id  = subCategory.getSubcat_id();
        String name = subCategory.getSubcat_name();
        intent.putExtra(JSONField.SUBCATEGORY_ID,id);
        intent.putExtra(JSONField.SUBCATEGORY_NAME,name);
        startActivity(intent);
    }
}
