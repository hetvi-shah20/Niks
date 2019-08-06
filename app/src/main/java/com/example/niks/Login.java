package com.example.niks;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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
import com.example.niks.Model.CheckUserResponse;
import com.example.niks.Retrofit.Common;
import com.example.niks.Retrofit.NiksAPI;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class Login extends AppCompatActivity {
    TextView signup,forgotpwd;
    Button login,errormsg,otp;
   TextInputEditText emailid,password;
    UserSessionManager userSessionManager;
    Toolbar toolbar;
    TextInputLayout tipPassword,tipEmailid;
    private Context context;
    NiksAPI mService;
    private static final int REQUEST_CODE =1000 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mService = Common.getAPI();
        userSessionManager = new UserSessionManager(Login.this);

        signup =  findViewById(R.id.createaccount);
        forgotpwd =  findViewById(R.id.txtForgotpwd);
        login =  findViewById(R.id.btnLogin);
        emailid =  findViewById(R.id.etEmailId);
        tipPassword = findViewById(R.id.tipPassword);
        tipEmailid = findViewById(R.id.tipEmailId);
        errormsg =  findViewById(R.id.errormsg);
        otp = findViewById(R.id.btnOTP);

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
        printKeyHash();

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
        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startLoginPage(LoginType.PHONE);
            }
        });
    }

    private void startLoginPage(LoginType loginType) {
        Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder builder=
                new AccountKitConfiguration.AccountKitConfigurationBuilder(loginType,
                        AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,builder.build());
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if (result.getError() != null) {
                Toast.makeText(this, "" + result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();

            } else if (result.wasCancelled()) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }else
            {
                if(result.getAccessToken() != null)
                {

                    final AlertDialog alertDialog= new
                            SpotsDialog.Builder().setContext(Login.this).build();
                    alertDialog.setMessage("Please wait.....");
                    alertDialog.show();

                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {

                            mService.checkUserExists(account.getPhoneNumber().toString())
                                    .enqueue(new Callback<CheckUserResponse>() {
                                        @Override
                                        public void onResponse(Call<CheckUserResponse> call, retrofit2.Response<CheckUserResponse> response) {
                                            CheckUserResponse userResponse = response.body();
                                            if(userResponse.isExists())
                                            {
                                                alertDialog.dismiss();
                                            }else {
                                                alertDialog.dismiss();


                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                                            String name= "hetvi";
                                            Log.d("method not called",name);
                                            alertDialog.dismiss();

                                        }
                                    });

                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {
                            Log.d("ERROR",accountKitError.getErrorType().getMessage());
                        }
                    });


                }
            }
        }
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
                String Message = jsonObject.optString(JSONField.MESSAGE);
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
                    Toast.makeText(this, "login successful", Toast.LENGTH_LONG).show();


                }
            else
                {

                    errormsg.setVisibility(View.VISIBLE);
                    errormsg.setText(Message);
                   // Toast.makeText(this, Message, Toast.LENGTH_LONG).show();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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


    private void printKeyHash() {

        try{
            PackageInfo info =  getPackageManager().getPackageInfo("com.example.drinkshop",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(md.digest(),Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
