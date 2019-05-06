package com.example.niks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        Thread thread =  new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(2000);

                }catch (Exception e)
                {

                }finally {
                    Intent intent =  new Intent(SplashScreen.this,Navigation.class);
                    startActivity(intent);

                }
            }
        };
        thread.start();
    }
    protected void onPause()
    {
        super.onPause();
        finish();
    }



    }


