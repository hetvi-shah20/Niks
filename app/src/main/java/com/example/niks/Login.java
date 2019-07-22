package com.example.niks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    TextView signup,forgotpwd;
    Button login;
   TextInputEditText emailid,password;
    UserSessionManager userSessionManager;
    Toolbar toolbar;
    TextInputLayout tipPassword,tipEmailid;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userSessionManager = new UserSessionManager(Login.this);

        signup =  findViewById(R.id.createaccount);
        forgotpwd =  findViewById(R.id.txtForgotpwd);
        login =  findViewById(R.id.btnLogin);
        emailid =  findViewById(R.id.etEmailId);
        tipPassword = findViewById(R.id.tipPassword);
        tipEmailid = findViewById(R.id.tipEmailId);


        password = findViewById(R.id.etPassword);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Login");
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validation();
                if(checkEmail() && checkPassword())
                {
                    sendLoginRequest();
                }

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
                    userSessionManager.setUserDetails(userId,userName,userGender,userEmail,userMobile,userAddress);

                    Intent intent = new Intent(Login.this,Navigation.class);

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

    @SuppressLint("ResourceAsColor")
    private boolean validation()
    {

        boolean isValid = true;

        if(emailid.getText().toString().equals("")) {
            tipEmailid.setError("Email should not be empty");
          //  tipEmailid.setErrorTextColor(ColorStateList.valueOf(R.color.error_Validation_color));
            isValid = false;
        }
        else{
            tipEmailid.setErrorEnabled(false);
        }

        if(password.getText().toString().equals(""))
        {
            tipPassword.setError("Password should not be empty");
            isValid = false;
        }else {
            tipPassword.setErrorEnabled(false);
        }
        return isValid;
    }


    private boolean checkEmail() {
        boolean isEmailValid = false;
        if (emailid.getText().toString().trim().length() <= 0) {
            emailid.setError("Enter Email");
            isEmailValid = false;
        } else if (Patterns.EMAIL_ADDRESS.matcher(emailid.getText().toString().trim()).matches()) {
            isEmailValid = true;
        } else {
            emailid.setError("Enter Correct Email");
            isEmailValid = false;
        }
        return isEmailValid;
    }

    private boolean checkPassword() {
        boolean isPasswordValid = false;
        if (password.getText().toString().trim().length() <= 0) {
            password.setError("Enter Password");
            isPasswordValid = false;
        } else {
            isPasswordValid = true;
        }
        return isPasswordValid;
    }
}
