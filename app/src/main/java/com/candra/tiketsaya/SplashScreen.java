package com.candra.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    Animation app_splash, btt;
    ImageView iv_splash;
    TextView tv_splash;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getUsernameLocal();

        //load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);

        //load element
        iv_splash = findViewById(R.id.iv_splash);
        tv_splash = findViewById(R.id.tv_splash);

        //run animation
        iv_splash.startAnimation(app_splash);
        tv_splash.startAnimation(btt);
//        Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intentHome = new Intent(SplashScreen.this, LoginActivity.class);
//                    startActivity(intentHome);
//                    finish();
//                }
//            }, 2000); //2 detik
    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
        if (username_key_new.isEmpty()){
            //setting timer 2 detik
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, GetStarted.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000); //1000ms = 1s
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intentHome = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intentHome);
                    finish();
                }
            }, 2000); //2 detik
        }
    }
}