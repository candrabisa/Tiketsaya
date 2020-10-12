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

public class SuccessRegister extends AppCompatActivity {

    ImageView ivSuksesReg;
    TextView tvSuksesReg, tvSuksesReg2;
    Animation btt, app_splash, ttb;

    Button btnExplore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_register);

        ivSuksesReg = findViewById(R.id.iv_SuksesReg);
        tvSuksesReg = findViewById(R.id.tv_SuksesReg);
        tvSuksesReg2 = findViewById(R.id.tv_SuksesReg2);
        btnExplore = findViewById(R.id.btn_exlporenow);

        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);
        ttb = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);

        btnExplore.startAnimation(btt);
        ivSuksesReg.startAnimation(app_splash);
        tvSuksesReg.startAnimation(ttb);
        tvSuksesReg2.startAnimation(ttb);


        btnExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMenu = new Intent(SuccessRegister.this, MainActivity.class);
                startActivity(intentMenu);
                finish();
            }
        });
    }
}