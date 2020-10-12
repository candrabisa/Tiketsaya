package com.candra.tiketsaya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GetStarted extends AppCompatActivity {

    Button btnlogin, btn_newaccount;
    ImageView ivSplash;
    TextView tvSplash;
    Animation ttb, btt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        ttb = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);
        btt= AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);
        ivSplash = findViewById(R.id.iv_getStarted);
        tvSplash = findViewById(R.id.tv_getStarted);
        btn_newaccount = findViewById(R.id.btn_newaccount);
        btnlogin = findViewById(R.id.btn_login);

        ivSplash.startAnimation(ttb);
        tvSplash.startAnimation(ttb);
        btn_newaccount.startAnimation(btt);
        btnlogin.startAnimation(btt);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentlogin = new Intent(GetStarted.this, LoginActivity.class);
                startActivity(intentlogin);
                finish();
            }
        });
        btn_newaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(GetStarted.this, RegisterActivity.class);
                startActivity(intentRegister);
                finish();
            }
        });
    }
}