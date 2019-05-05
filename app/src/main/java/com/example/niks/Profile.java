package com.example.niks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.niks.R.menu;

public class Profile extends AppCompatActivity {
    TextView name,email,number,address,ename;
    UserSessionManager userSessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ename = findViewById(R.id.name);
        name = findViewById(R.id.tvName);
        email = findViewById(R.id.tvEmail);
        number = findViewById(R.id.tvNumber);
        address = findViewById(R.id.tvAddress);
        userSessionManager =  new UserSessionManager(Profile.this);
        display();
    }

    private void display() {
        ename.setText(userSessionManager.getUserName());
        name.setText(userSessionManager.getUserName());
        email.setText(userSessionManager.getUserEmail());
        number.setText(userSessionManager.getUserPhone());
        address.setText(userSessionManager.getUserAddress());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_profile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.editProfile:
                Intent intent = new Intent(Profile.this,EditProfile.class);
                startActivity(intent);
                break;

            case R.id.logout:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
