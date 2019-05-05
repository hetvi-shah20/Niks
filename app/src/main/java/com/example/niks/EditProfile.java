package com.example.niks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    EditText name,email,number;
    Button update;
    UserSessionManager userSessionManager;
    String username,useremail,userid;

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
        userSessionManager =  new UserSessionManager(EditProfile.this);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUpdaqteRequest();
            }
        });
        name.setHint(userSessionManager.getUserName());
        email.setHint(userSessionManager.getUserEmail());
        number.setHint(userSessionManager.getUserPhone());
    }

    private void sendUpdaqteRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.KEY_USER_DETAILS_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent =  new Intent(EditProfile.this,Profile.class);
                finish();
                startActivity(intent);

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
}
