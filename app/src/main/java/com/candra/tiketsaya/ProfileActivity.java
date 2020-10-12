package com.candra.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    RelativeLayout btn_ticketDetails;
    Button btn_EditProf;
    ImageView iv_Profile;
    TextView tv_NamaUser, tv_Bio;
    LinearLayout btn_signout;

    DatabaseReference dRef, dRef2;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    RecyclerView myticket_place;
    ArrayList<myTiket> list;
    tiketAdapter ticketAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getUsernameLocal();

        btn_ticketDetails = findViewById(R.id.rl_ticketDetails);
        btn_EditProf = findViewById(R.id.btn_editProfile);
        iv_Profile = findViewById(R.id.iv_ProfileDetails);
        tv_NamaUser = findViewById(R.id.tv_NamaUser);
        tv_Bio = findViewById(R.id.tv_HobiUser);
        btn_signout= findViewById(R.id.btn_signout);

        myticket_place = findViewById(R.id.myTicket_Place);
        myticket_place.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<myTiket>();

        dRef = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tv_NamaUser.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                tv_Bio.setText(dataSnapshot.child("bio").getValue().toString());
                Picasso.with(ProfileActivity.this)
                        .load(dataSnapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(iv_Profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        btn_ticketDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intentTDetails = new Intent(ProfileActivity.this, MyTiketDetails.class);
//                startActivity(intentTDetails);
//            }
//        });

        dRef2 = FirebaseDatabase.getInstance().getReference().child("myTickets").child(username_key_new);
        dRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    myTiket a = dataSnapshot1.getValue(myTiket.class);
                    list.add(a);
                }
                ticketAdapter = new tiketAdapter(ProfileActivity.this, list);
                myticket_place.setAdapter(ticketAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_EditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEditProf = new Intent(ProfileActivity.this, EditProfile.class);
                startActivity(intentEditProf);
            }
        });

        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, null);
                editor.apply();

                Intent intenthome = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intenthome);
                finish();
            }
        });
    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}