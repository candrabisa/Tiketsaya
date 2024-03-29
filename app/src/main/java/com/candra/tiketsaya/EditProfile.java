package com.candra.tiketsaya;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfile extends AppCompatActivity {

    Button btn_save, btn_cariFoto;
    LinearLayout btn_back;
    ImageView iv_ProfilEdit;
    EditText et_nameEdit, et_hobiEdit, et_usernameEdit, et_passwordEdit, et_emailEdit;

    DatabaseReference dRef;
    StorageReference storage;

    Uri photo_location;
    Integer photo_max = 1;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getUsernameLocal();

        et_nameEdit = findViewById(R.id.et_nameEdit);
        et_hobiEdit = findViewById(R.id.et_hobiEdit);
        et_usernameEdit = findViewById(R.id.et_usernameEdit);
        et_passwordEdit = findViewById(R.id.et_passwordEdit);
        et_emailEdit = findViewById(R.id.et_emailEdit);
        iv_ProfilEdit = findViewById(R.id.iv_ProfilEdit);
        btn_save = findViewById(R.id.btn_saveProfile);
        btn_cariFoto = findViewById(R.id.btn_cariFoto);
        btn_back = findViewById(R.id.btn_back2);

        dRef = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                et_nameEdit.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                et_hobiEdit.setText(dataSnapshot.child("bio").getValue().toString());
                et_usernameEdit.setText(dataSnapshot.child("username").getValue().toString());
                et_passwordEdit.setText(dataSnapshot.child("password").getValue().toString());
                et_emailEdit.setText(dataSnapshot.child("email_address").getValue().toString());
                Picasso.with(EditProfile.this).load(dataSnapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(iv_ProfilEdit);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("username").setValue(et_usernameEdit.getText().toString());
                        dataSnapshot.getRef().child("password").setValue(et_passwordEdit.getText().toString());
                        dataSnapshot.getRef().child("nama_lengkap").setValue(et_nameEdit.getText().toString());
                        dataSnapshot.getRef().child("bio").setValue(et_hobiEdit.getText().toString());
                        dataSnapshot.getRef().child("email_address").setValue(et_emailEdit.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Intent intent = new Intent(EditProfile.this, ProfileActivity.class);
                startActivity(intent);
                finish();

                // validasi untuk file apakah ada?
                if (photo_location !=null){
                    StorageReference storageReference = storage.child(System.currentTimeMillis() + "." +
                            getFileExtension(photo_location));
                    storageReference.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String uri_photo = taskSnapshot.getUploadSessionUri().toString();
                            dRef.getRef().child("url_photo_profile").setValue(uri_photo);
                        }
                    }) .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Intent intent = new Intent(EditProfile.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
        btn_cariFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPhoto();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto(){
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data !=null && data.getData() !=null)
        {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(iv_ProfilEdit);
        }
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}