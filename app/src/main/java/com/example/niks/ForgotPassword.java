package com.example.niks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {

    EditText mail;
    Button submit;
    String etmail;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mail =  findViewById(R.id.etMailpwd);
        submit = findViewById(R.id.btnMail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Forgot Password");
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordRequest();
                Toast.makeText(ForgotPassword.this, "Check Your Email", Toast.LENGTH_SHORT).show();
                Intent intent =  new Intent(ForgotPassword.this,Login.class);
                finish();
                startActivity(intent);
                
            }
        });
        

    }

    private void sendPasswordRequest() {
        StringRequest stringRequest  = new StringRequest(Request.Method.POST, WebURL.KEY_FORGOTPWD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                etmail = mail.getText().toString();
                Map<String,String>params =  new HashMap<>();
                params.put(JSONField.KEY_USER_EMAIL,etmail);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword.this);
        requestQueue.add(stringRequest);
    }
}
