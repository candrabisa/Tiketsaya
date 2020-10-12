package com.candra.tiketsaya;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextView tv_Register;
    Button btnLogin;
    EditText etUsername, etPassword;
    ProgressDialog progressDialog;

    DatabaseReference dRef;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv_Register = findViewById(R.id.btn_new_account);
        btnLogin = findViewById(R.id.btn_login);
        etUsername = findViewById(R.id.etUsername_Login);
        etPassword = findViewById(R.id.etPassword_Login);

        tv_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //berpindah tampilan dari login ke register
                Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Melakukan proses login, harap menunggu");
                progressDialog.setCancelable(false);
                progressDialog.show();

                final String password = etPassword.getText().toString();
                dRef = FirebaseDatabase.getInstance().getReference().child("Users").child(etUsername.getText().toString());

                dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            //ambil data password dari database
                            String databaseFromFirebase = dataSnapshot.child("password").getValue().toString();

                            //validasi edittext password dengan password pada firebase
                            if (password.equals(databaseFromFirebase)){
                                //menyimpan data kepada local storage(handphone kalian)
                                SharedPreferences Spref = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                SharedPreferences.Editor editor = Spref.edit();
                                editor.putString(username_key, etUsername.getText().toString());
                                editor.apply();

                                //berpindah tampilan dari login ke main activiy
                                Intent intentLogin = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intentLogin);
                                progressDialog.dismiss();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Password Salah", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Data tidak ditemukan..", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}