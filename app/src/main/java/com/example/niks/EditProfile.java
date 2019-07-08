package com.example.niks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class EditProfile extends AppCompatActivity {
    EditText name,email,number;
    Button update;
    UserSessionManager userSessionManager;
    String username,useremail,userid;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        name =  findViewById(R.id.editName);
        email =  findViewById(R.id.editEmail);
        number =  findViewById(R.id.editNumber);
        number.setFocusable(false);
        number.setEnabled(false);
        update =  findViewById(R.id.btnUpdate);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Edit Profile");
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        userSessionManager =  new UserSessionManager(EditProfile.this);
         name.setText(userSessionManager.getUserName());
        Log.d("new name",userSessionManager.getUserName());
        email.setText(userSessionManager.getUserEmail());
        number.setText(userSessionManager.getUserPhone());
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUpdaqteRequest();

            }
        });

        newDetails();
    }

    private void newDetails() {

    }

    private void sendUpdaqteRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.KEY_USER_DETAILS_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Intent intent =  new Intent(EditProfile.this,Navigation.class);
//                finish();
//                startActivity(intent);
                    parseEditProfile(response);
//                name.setText(userSessionManager.getUserName());
//                Log.d("new name",userSessionManager.getUserName());
//                email.setText(userSessionManager.getUserEmail());
//                number.setText(userSessionManager.getUserPhone());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                username = name.getText().toString();
                useremail = email.getText().toString();

//                userid = userSessionManager.getUserId();
                Map<String,String> params =  new HashMap<>();
                params.put(JSONField.KEY_USER_ID,userSessionManager.getUserId());
                params.put(JSONField.KEY_USER_NAME,username);
                params.put(JSONField.KEY_USER_EMAIL,useremail);

                 return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        requestQueue.add(stringRequest);
    }

    private void parseEditProfile(String response) {

        Log.d("RESPONSE",response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JSONField.FLAG);
            String message = jsonObject.optString(JSONField.MESSAGE);
            if(flag == 1)
            {
                Toast.makeText(this, "Details updated successfully", Toast.LENGTH_LONG).show();
                userSessionManager.setUserDetails(userSessionManager.getUserId(),username,userSessionManager.getUserGender(),useremail,userSessionManager.getUserPhone(),userSessionManager.getUserAddress());
                Intent intent =  new Intent(EditProfile.this,Navigation.class);
                finish();
                startActivity(intent);

            }
            else
            {
                Toast.makeText(this,message, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
