package com.example.niks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ChangePassword extends AppCompatActivity {
    EditText etOpass,etNpass,etCpass;
    Button reset;
    private UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        etCpass =  findViewById(R.id.etCpass);
        etNpass =  findViewById(R.id.etNpass);
        etOpass = findViewById(R.id.etOpass);
        reset = findViewById(R.id.btnChangePass);
        userSessionManager = new UserSessionManager(ChangePassword.this);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((etNpass.getText().toString()).equals(etCpass.getText().toString()))
                {
                    sendChangePedRequet();

                }
                else
                {
                    Toast.makeText(ChangePassword.this, "New Password and Confrim Password should be same", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void sendChangePedRequet() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.KEY_CHANGE_PWD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseChangePwd(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params = new HashMap<>();
                params.put(JSONField.KEY_USER_ID,userSessionManager.getUserId());
                params.put(JSONField.KEY_OLD_PWD,etOpass.getText().toString());
                params.put(JSONField.KEY_NEW_PWD,etNpass.getText().toString());
                params.put(JSONField.KEY_CONFRIM_PWD,etCpass.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue  = Volley .newRequestQueue(ChangePassword.this);
        requestQueue.add(stringRequest);
    }

    private void parseChangePwd(String response) {
        Log.d("RESPONSE",response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            int success = jsonObject.optInt(JSONField.SUCCESS);
            String message = jsonObject.optString(JSONField.MESSAGE);
            if (success == 1)
            {
                    Toast.makeText(this, "Password Chnaged Successfully", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
