package com.example.niks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Set;

public class Setting extends AppCompatActivity {

    TextView profile, changePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        profile = findViewById(R.id.setEditProfile);
        changePwd = findViewById(R.id.setChangePwd);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=  new Intent(Setting.this,EditProfile.class);
                startActivity(intent);

            }
        });

        changePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(Setting.this,ChangePassword.class);
                startActivity(intent);

            }
        });

    }
}