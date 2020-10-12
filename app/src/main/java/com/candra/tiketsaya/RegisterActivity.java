package com.candra.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    LinearLayout btn_back;
    Button btn_continue;
    EditText etUsername, etPassword, etEmail;

    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_continue = findViewById(R.id.btn_continueReg);
        btn_back = findViewById(R.id.btn_back);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intentBack);
                finish();
            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCon = new Intent(RegisterActivity.this, Register2Activity.class);
                startActivity(intentCon);

                //menyimpan data kepada local storage(handphone kalian)
                SharedPreferences Spref = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = Spref.edit();
                editor.putString(username_key, etUsername.getText().toString());
                editor.apply();

                //menyimpan data ke firebase
                reference = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(etUsername.getText().toString());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("username").setValue(etUsername.getText().toString());
                        dataSnapshot.getRef().child("password").setValue(etPassword.getText().toString());
                        dataSnapshot.getRef().child("email_address").setValue(etEmail.getText().toString());
                        dataSnapshot.getRef().child("user_balance").setValue(200);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }


}