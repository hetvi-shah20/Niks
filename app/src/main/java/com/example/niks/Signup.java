package com.example.niks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
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

public class Signup extends AppCompatActivity {

    EditText name,email,password,mobile,address;
    RadioButton gendermale,genderfemale;
    Button submit;
    private String selectedGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name =  findViewById(R.id.etName);
        email =  findViewById(R.id.etEmail);
        password =  findViewById(R.id.etPassword);
        mobile =  findViewById(R.id.etPhone);
        address =  findViewById(R.id.etAddress);
        gendermale =  findViewById(R.id.male);
        genderfemale = findViewById(R.id.female);
        submit =  findViewById(R.id.btnSignup);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                switch (v.getId())
//                {
//                    case R.id.btnSignup:
                        if(checkUser() && checkEmail() && checkPassword() && checkGender() && checkMobileNumber() && checkAddress() )
                        {
                          selectedGender = genderfemale.isChecked() ? "Female" : "Male";
                            sendSignupRequest();
                            Intent intent =  new Intent(Signup.this,Login.class);
                            startActivity(intent);
                        }
//                        break;

                }


//            }


            private void sendSignupRequest() {
                StringRequest stringRequest =  new StringRequest(Request.Method.POST, WebURL.KEY_SIGNUP_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        parseJsonSignUp(response);
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
                        params.put(JSONField.KEY_USER_NAME,name.getText().toString());
                        params.put(JSONField.KEY_USER_EMAIL,email.getText().toString());
                        params.put(JSONField.KEY_USER_PASSWORD,password.getText().toString());
                        params.put(JSONField.KEY_USER_GENDER,selectedGender);
                        params.put(JSONField.KEY_USER_MOBILE,mobile.getText().toString());
                        params.put(JSONField.KEY_USER_ADDRESS,address.getText().toString());
                        Log.d("parameters",params.put(JSONField.KEY_USER_NAME,name.getText().toString()));
                        return params;
                    }
                };
                RequestQueue requestQueue =  Volley.newRequestQueue(Signup.this);
                requestQueue.add(stringRequest);

            }
        });
    }

    private void parseJsonSignUp(String response) {
        Log.d("RESPONSE",response);

    }


    private boolean checkGender()
    {
        boolean isValidGender = false;
        isValidGender = gendermale.isChecked() || genderfemale.isChecked();
        if(isValidGender == false)
        {
            Toast.makeText(this, "select gender", Toast.LENGTH_SHORT).show();
        }

        return isValidGender;
    }

    private boolean checkMobileNumber()
    {
        boolean isMobileNumberValid = false;
        if(mobile.getText().toString().trim().length()<= 0)
        {
            mobile.setError("Enter Mobile Number");
        }else if(mobile.getText().toString().trim().length() == 10)
        {
            isMobileNumberValid = true;
        }
        else
        {
            mobile.setError("Enter Correct Number");
        }
        return isMobileNumberValid;
    }


    private boolean checkUser()
    {
        boolean isUserValid = false;
        if(name.getText().toString().trim().length()<= 0)
        {
            name.setError("Enter name");
        }
        else
        {
            isUserValid = true;
        }
        return isUserValid;
    }

    private boolean checkPassword()
    {
        boolean isPasswordValid = false;
        if(password.getText().toString().trim().length()<= 0)
        {
            password.setError("Enter Password");
        }
        else
        {
            isPasswordValid = true;
        }
        return isPasswordValid;
    }

    private boolean checkEmail()
    {
        boolean isEmailValid = false;
        if(email.getText().toString().trim().length()<= 0)
        {
            email.setError("Enter Email id");
        }
        else  if(Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches())
        {
            isEmailValid = true;
        }
        else
        {
            email.setError("Enter correct Email ");
        }
        return isEmailValid;
    }

    private boolean checkAddress()
    {
        boolean isAddressValid = false;
        if(address.getText().toString().trim().length()<= 0)
        {
            address.setError("Enter address");
        }
        else
        {
            isAddressValid = true;
        }
        return isAddressValid;
    }

}
