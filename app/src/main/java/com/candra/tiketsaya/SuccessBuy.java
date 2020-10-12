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

public class SuccessBuy extends AppCompatActivity {

    Button btn_dasboard, btn_viewtiket;
    ImageView ivSukses;
    TextView tvSuksesBuy, tvSuksesBuy2;
    Animation app_splash, btt, ttb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_buy);

        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);
        ttb = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);

        btn_dasboard = findViewById(R.id.btn_dashboard);
        btn_viewtiket = findViewById(R.id.btn_viewticket);
        ivSukses = findViewById(R.id.iv_suksesBuy);
        tvSuksesBuy = findViewById(R.id.tv_suksesBuy);
        tvSuksesBuy2 = findViewById(R.id.tv_suksesBuy2);

        //run animation
        ivSukses.startAnimation(app_splash);
        tvSuksesBuy.startAnimation(ttb);
        tvSuksesBuy2.startAnimation(ttb);
        btn_dasboard.startAnimation(btt);
        btn_viewtiket.startAnimation(btt);

        btn_dasboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDasboard = new Intent(SuccessBuy.this, MainActivity.class);
                startActivity(intentDasboard);
                finish();
            }
        });
    }
}