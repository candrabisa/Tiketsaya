package com.candra.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    LinearLayout btn_pisa, btn_pagoda, btn_monas, btn_candi, btn_sphinx, btn_torri;
    TextView tvNama, tvBio, tvSaldo;
    ImageView ivFoto;

    DatabaseReference dRef;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getUsernameLocal();

        btn_pisa = findViewById(R.id.ll_pisa);
        btn_candi = findViewById(R.id.ll_candi);
        btn_monas = findViewById(R.id.ll_monas);
        btn_pagoda = findViewById(R.id.ll_pagoda);
        btn_sphinx = findViewById(R.id.ll_sphinx);
        btn_torri = findViewById(R.id.ll_torri);
        tvNama = findViewById(R.id.tv_NamaKamu);
        tvBio = findViewById(R.id.tv_bioKamu);
        tvSaldo = findViewById(R.id.tv_userBalance);
        ivFoto = findViewById(R.id.iv_fotoKamu);

        dRef = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvNama.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                tvBio.setText(dataSnapshot.child("bio").getValue().toString());
                tvSaldo.setText("US$ " + dataSnapshot.child("user_balance").getValue().toString());

                Picasso.with(MainActivity.this)
                        .load(dataSnapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(ivFoto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ivFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProfile = new Intent (MainActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
            }
        });
        btn_pisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TiketDetails.class);
                intent.putExtra("jenis_tiket", "Pisa");
                startActivity(intent);
            }
        });
        btn_torri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TiketDetails.class);
                intent.putExtra("jenis_tiket", "Torri");
                startActivity(intent);
            }
        });
        btn_pagoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TiketDetails.class);
                intent.putExtra("jenis_tiket", "Pagoda");
                startActivity(intent);
            }
        });
        btn_candi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TiketDetails.class);
                intent.putExtra("jenis_tiket", "Candi");
                startActivity(intent);
            }
        });
        btn_sphinx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TiketDetails.class);
                intent.putExtra("jenis_tiket", "Sphinx");
                startActivity(intent);
            }
        });
        btn_monas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TiketDetails.class);
                intent.putExtra("jenis_tiket", "Monas");
                startActivity(intent);
            }
        });
    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}