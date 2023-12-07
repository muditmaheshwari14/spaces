package com.spaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {
    public static final int SPALSH_TIME = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent splashIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(splashIntent);
                finish();
            }
        }, SPALSH_TIME);
    }
}
