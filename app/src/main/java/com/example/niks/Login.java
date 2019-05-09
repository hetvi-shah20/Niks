package com.example.niks;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    TextView signup,forgotpwd;
    Button login;
   TextInputEditText emailid,password;
    UserSessionManager userSessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userSessionManager = new UserSessionManager(Login.this);

        signup =  findViewById(R.id.createaccount);
        forgotpwd =  findViewById(R.id.txtForgotpwd);
        login =  findViewById(R.id.btnLogin);
        emailid =  findViewById(R.id.etEmailId);
        password = findViewById(R.id.etPassword);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendLoginRequest();

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,Signup.class);
                startActivity(i);
            }
        });
        forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  i2= new Intent(Login.this,ForgotPassword.class);
                startActivity(i2);
            }
        });
    }

    private void sendLoginRequest() {
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, WebURL.KEY_LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseLoginResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(JSONField.KEY_USER_EMAIL,emailid.getText().toString());
                params.put(JSONField.KEY_USER_PASSWORD,password.getText().toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(stringRequest);



    }

    private void parseLoginResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has(JSONField.SUCCESS))
            {
                int success = jsonObject.optInt(JSONField.SUCCESS);
                if (success == 1)
                {
                    Log.d("TAG","login successful");
                    JSONObject jsonArray = jsonObject.optJSONObject(JSONField.USER_ARRAY);
                    Log.d("RESPONSE",JSONField.USER_ARRAY);

                    String userId =  jsonArray.optString(JSONField.KEY_USER_ID);
                    String userName = jsonArray.optString(JSONField.KEY_USER_NAME);
                    String userGender =  jsonArray.optString(JSONField.KEY_USER_GENDER);
                    String userEmail =  jsonArray.optString(JSONField.KEY_USER_EMAIL);
                    String userPassword = jsonArray.optString(JSONField.KEY_USER_PASSWORD);
                    String userMobile = jsonArray.optString(JSONField.KEY_USER_MOBILE);
                    String userAddress = jsonArray.optString(JSONField.KEY_USER_ADDRESS);

                    userSessionManager.setLoginStatus();
                    userSessionManager.setUserDetails(userId,userName,userGender,userEmail,userPassword,userMobile,userAddress);

                    Intent intent = new Intent(Login.this,Navigation.class);

                    finish();
                    startActivity(intent);
                    Toast.makeText(this, "login successful", Toast.LENGTH_SHORT).show();


                }
            else
                {
                    Toast.makeText(this, "Invalid login details", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
