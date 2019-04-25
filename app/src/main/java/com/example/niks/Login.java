package com.example.niks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    TextView signup,forgotpwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signup =  findViewById(R.id.createaccount);
        forgotpwd =  findViewById(R.id.txtForgotpwd);
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
}
