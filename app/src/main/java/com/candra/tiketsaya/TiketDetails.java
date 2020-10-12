package com.candra.tiketsaya;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class TiketDetails extends AppCompatActivity {

    Button btnBuyTicket;
    TextView tv_photoSpot, tv_wifi, tv_fest, tv_lokasiTiket, tv_titleTiket, tv_descTicket;
    ImageView iv_header_ticket_details;
    LinearLayout btn_back_details;

    DatabaseReference dRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiket_details);

        btn_back_details = findViewById(R.id.btn_back_details);
        btnBuyTicket = findViewById(R.id.btn_continueReg);
        iv_header_ticket_details = findViewById(R.id.header_tiketDetails);
        tv_titleTiket = findViewById(R.id.tv_titleTiket);
        tv_lokasiTiket = findViewById(R.id.tv_lokasiTiket);
        tv_photoSpot = findViewById(R.id.tvphotospot);
        tv_wifi = findViewById(R.id.tv_wifi);
        tv_fest = findViewById(R.id.tv_fest);
        tv_descTicket = findViewById(R.id.tv_descTicket);

        //mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        //mengambil data dari firebase berdasarkan intent
        dRef = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //menimpa data tiket detail dari firebase
                tv_titleTiket.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                tv_lokasiTiket.setText(dataSnapshot.child("lokasi").getValue().toString());
                tv_photoSpot.setText(dataSnapshot.child("is_photo_spot").getValue().toString());
                tv_wifi.setText(dataSnapshot.child("is_wifi").getValue().toString());
                tv_fest.setText(dataSnapshot.child("is_festival").getValue().toString());
                tv_descTicket.setText(dataSnapshot.child("short_desc").getValue().toString());
                Picasso.with(TiketDetails.this).load(dataSnapshot.child("url_thumbnail").getValue().toString())
                        .centerCrop().fit().into(iv_header_ticket_details);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnBuyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBuy = new Intent(TiketDetails.this, TicketCheckout.class);
                intentBuy.putExtra("jenis_tiket" ,jenis_tiket_baru);
                startActivity(intentBuy);
            }
        });

        btn_back_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}