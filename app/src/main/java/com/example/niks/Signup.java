package com.example.niks;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;
import com.example.niks.databinding.ActivitySignupBinding;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    // XML file name is activity_signup.xml so it will automatically create ActivitySignupBinding this file..
    ActivitySignupBinding binding;

    private String selectedGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        // THATS it... now removing unwated code...
        // you can use every variables via ID of layout. like....
        // no need of findViewByID

        setSupportActionBar(binding.customToolbar.toolbar);
        binding.customToolbar.toolbar.setTitle("Sign Up");
        setTitle("");
        binding.customToolbar.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        binding.customToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkUser() && checkEmail() && checkPassword() && checkGender() && checkMobileNumber() && checkAddress())
                {
                    selectedGender = binding.female.isChecked() ? "Female" : "Male";
                    sendSignupRequest();
                    // aa ahiya n ave.. niche ni line...
//                            Intent intent =  new Intent(Signup.this,Login.class);
//                            startActivity(intent);
                }
            }
        });
    }

    private void sendSignupRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.KEY_SIGNUP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                parseJsonSignUp(response);
                Intent intent =  new Intent(Signup.this,Login.class);
                            startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(JSONField.KEY_USER_NAME, binding.etName.getText().toString());
                params.put(JSONField.KEY_USER_EMAIL, binding.etEmail.getText().toString());
                params.put(JSONField.KEY_USER_PASSWORD, binding.etPassword.getText().toString());
                params.put(JSONField.KEY_USER_GENDER, selectedGender);
                params.put(JSONField.KEY_USER_MOBILE, binding.etPhone.getText().toString());
                params.put(JSONField.KEY_USER_ADDRESS, binding.etAddress.getText().toString());
//                Log.d("parameters", params.put(JSONField.KEY_USER_NAME, name.getText().toString()));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Signup.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    private void parseJsonSignUp(String response) {
        Log.d("RESPONSE", response);

    }


    private boolean checkGender() {
        boolean isValidGender = false;
        isValidGender = binding.male.isChecked() || binding.female.isChecked();
        if (!isValidGender) {
            Toast.makeText(this, "select gender", Toast.LENGTH_SHORT).show();
        }

        return isValidGender;
    }

    private boolean checkMobileNumber() {
        boolean isMobileNumberValid = false;
        if (binding.etPhone.getText().toString().trim().length() <= 0) {
            binding.etPhone.setError("Enter Mobile Number");
        } else if (binding.etPhone.getText().toString().trim().length() == 10) {
            isMobileNumberValid = true;
        } else {
            binding.etPhone.setError("Enter Correct Number");
        }
        return isMobileNumberValid;
    }


    private boolean checkUser() {
        boolean isUserValid = false;
        if (binding.etName.getText().toString().trim().length() <= 0) {
            binding.etName.setError("Enter name");
        } else {
            isUserValid = true;
        }
        return isUserValid;
    }

    private boolean checkPassword() {
        boolean isPasswordValid = false;
        if (binding.etPassword.getText().toString().trim().length() <= 0) {
            binding.etPassword.setError("Enter Password");
        } else {
            isPasswordValid = true;
        }
        return isPasswordValid;
    }

    private boolean checkEmail() {
        boolean isEmailValid = false;
        if (binding.etEmail.getText().toString().trim().length() <= 0) {
            binding.etEmail.setError("Enter Email id");
        } else if (Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText().toString().trim()).matches()) {
            isEmailValid = true;
        } else {
            binding.etEmail.setError("Enter correct Email ");
        }
        return isEmailValid;
    }

    private boolean checkAddress() {
        boolean isAddressValid = false;
        if (binding.etAddress.getText().toString().trim().length() <= 0) {
            binding.etAddress.setError("Enter address");
        } else {
            isAddressValid = true;
        }
        return isAddressValid;
    }


}
