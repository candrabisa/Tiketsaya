package com.candra.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class TicketCheckout extends AppCompatActivity {

    Button btn_payNow, btnMinus, btnPlus;
    TextView tv_jumlahTiket, tv_mySaldo, tv_totalHarga, tv_namaWisata, tv_lokasiWisata, tv_ketentuan;
    ImageView iv_noticeUang;

    DatabaseReference dRef, dRef2, dRef3, dRef4;

    Integer valueJumlahTiket = 1;
    Integer mySaldo = 0;
    Integer valueTotalHarga = 0;
    Integer valueHargaTiket = 0;
    Integer sisa_balance = 0;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    String date_wisata = "";
    String time_wisata = "";

    //membuat invoice secara acak
    Integer nomor_transaksi = new Random().nextInt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);
        getUsernameLocal();

        //mengambil data dari intent tiket details
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        btn_payNow = findViewById(R.id.btn_payNow);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);

        tv_namaWisata = findViewById(R.id.tv_namaWisataCheckout);
        tv_lokasiWisata = findViewById(R.id.tv_lokasiWisataCheckout);
        tv_ketentuan = findViewById(R.id.tv_ketentuan);

        iv_noticeUang = findViewById(R.id.noticeUang);

        tv_jumlahTiket = findViewById(R.id.tv_jumlahTiket);
        tv_mySaldo = findViewById(R.id.tv_mySaldo);
        tv_totalHarga = findViewById(R.id.tvTotalHarga);

        //default untuk checkout
        tv_mySaldo.setTextColor(Color.parseColor("#203DD1"));
        tv_jumlahTiket.setText(valueJumlahTiket.toString());
        iv_noticeUang.setVisibility(View.GONE);

        //secara default btn minus ilang jika hanya beli 1 tiket
        btnMinus.animate().alpha(0).setDuration(300).start();
        btnMinus.setEnabled(false);

        //mengambil data user dari firebase
        dRef2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        dRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mySaldo = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                tv_mySaldo.setText("US$ " + mySaldo+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //mengambil data dari firebase berdasarkan intent
        dRef = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //menimpa data tiket detail dari firebase
                tv_namaWisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                tv_lokasiWisata.setText(dataSnapshot.child("lokasi").getValue().toString());
                tv_ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());
                valueHargaTiket = Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());

                date_wisata = dataSnapshot.child("date_wisata").getValue().toString();
                time_wisata = dataSnapshot.child("time_wisata").getValue().toString();

                //total harga checkout
                valueTotalHarga = valueHargaTiket * valueJumlahTiket;
                tv_totalHarga.setText("US$ " + valueHargaTiket+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueJumlahTiket +=1;
                tv_jumlahTiket.setText(valueJumlahTiket.toString());
                if (valueJumlahTiket > 1){
                    btnMinus.animate().alpha(1).setDuration(300).start();
                    btnMinus.setEnabled(true);
                }
                valueTotalHarga = valueHargaTiket * valueJumlahTiket;
                tv_totalHarga.setText("US$ "+ valueTotalHarga+"");
                if (valueTotalHarga > mySaldo){
                    btn_payNow.animate().translationY(250)
                            .alpha(0).setDuration(350).start();
                    btn_payNow.setEnabled(false);
                    tv_mySaldo.setTextColor(Color.RED);
                    iv_noticeUang.setVisibility(View.VISIBLE);
                }
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueJumlahTiket -=1;
                tv_jumlahTiket.setText(valueJumlahTiket.toString());
                if (valueJumlahTiket < 2){
                    btnMinus.animate().alpha(0).setDuration(300).start();
                    btnMinus.setEnabled(false);
                }
                valueTotalHarga = valueHargaTiket * valueJumlahTiket;
                tv_totalHarga.setText("US$ "+ valueTotalHarga+"");
                if (valueTotalHarga < mySaldo) {
                    btn_payNow.animate().translationY(0)
                            .alpha(1).setDuration(350).start();
                    btn_payNow.setEnabled(true);
                    tv_mySaldo.setTextColor(Color.parseColor("#203DD1"));
                    iv_noticeUang.setVisibility(View.GONE);
                }
            }
        });

        btn_payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //menyimpan data user ke firebase dan membuat table baru "myTicket
                dRef3 = FirebaseDatabase.getInstance().getReference().child("myTickets").child(username_key_new)
                        .child(tv_namaWisata.getText().toString() + nomor_transaksi);

                dRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dRef3.getRef().child("id_ticket").setValue(tv_namaWisata.getText().toString() + nomor_transaksi);
                        dRef3.getRef().child("nama_wisata").setValue(tv_namaWisata.getText().toString());
                        dRef3.getRef().child("lokasi").setValue(tv_lokasiWisata.getText().toString());
                        dRef3.getRef().child("ketentuan").setValue(tv_ketentuan.getText().toString());
                        dRef3.getRef().child("jumlah_tiket").setValue(valueJumlahTiket.toString());
                        dRef3.getRef().child("date_wisata").setValue(date_wisata);
                        dRef3.getRef().child("time_wisata").setValue(time_wisata);

                        Intent intentSuccess = new Intent(TicketCheckout.this, SuccessBuy.class);
                        startActivity(intentSuccess);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //update data balance kepada users (yang saat ini login)
                //mengambil data user dari firebase
                dRef4 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                dRef4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sisa_balance = mySaldo - valueTotalHarga;
                        dRef4.getRef().child("user_balance").setValue(sisa_balance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}