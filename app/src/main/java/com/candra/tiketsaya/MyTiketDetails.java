package com.candra.tiketsaya;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyTiketDetails extends AppCompatActivity {

    TextView destinasi, namakota_detail, totalTicket_Detail, tanggal_ticketDetail, time_ticketDetail, informasi_detail;

    DatabaseReference dRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tiket_details);

        destinasi = findViewById(R.id.destinasi);
        namakota_detail = findViewById(R.id.namakota_detail);
        tanggal_ticketDetail = findViewById(R.id.tanggal_ticketDetail);
        time_ticketDetail = findViewById(R.id.time_ticketDetail);
        informasi_detail = findViewById(R.id.informasi_detail);

        //mengambil data dari intent tiket details
        Bundle bundle = getIntent().getExtras();
        final String nama_wisata_baru = bundle.getString("nama_wisata");

        //mengambil data dari firebase
        dRef = FirebaseDatabase.getInstance().getReference().child("Wisata").child(nama_wisata_baru);
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                destinasi.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                namakota_detail.setText(dataSnapshot.child("lokasi").getValue().toString());
                tanggal_ticketDetail.setText(dataSnapshot.child("date_wisata").getValue().toString());
                time_ticketDetail.setText(dataSnapshot.child("time_wisata").getValue().toString());
                informasi_detail.setText(dataSnapshot.child("ketentuan").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}