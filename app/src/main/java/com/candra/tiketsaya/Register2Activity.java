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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Register2Activity extends AppCompatActivity {

    LinearLayout btn_back;
    Button btnContinue, btn_addPhoto;
    ImageView ivRegister;
    EditText etNama, etBio;

    Uri photo_location;
    Integer photo_max = 1;

    DatabaseReference dReferences;
    StorageReference fStorage;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        getUsernameLocal();

        btn_back = findViewById(R.id.btn_back2);
        btnContinue = findViewById(R.id.btn_signup);
        btn_addPhoto = findViewById(R.id.btn_AddPhotoRegister);
        ivRegister = findViewById(R.id.pic_photo_register_user);
        etNama = findViewById(R.id.etNama_Reg);
        etBio = findViewById(R.id.etBio_Reg);

        btn_addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPhoto();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(Register2Activity.this, RegisterActivity.class);
                startActivity(intentBack);
                finish();
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mengubah state signup button menjadi loading
                btnContinue.setEnabled(false);
                btnContinue.setText("Loading...");

                //minyimpan ke firebase
                dReferences = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(username_key_new);
                fStorage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);

                //validasi untuk file (apakah ada?)
                if (photo_location !=null){
                    StorageReference storageReference = fStorage.child(System.currentTimeMillis() + "." +
                            getFileExtension(photo_location));
                    storageReference.putFile(photo_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String uri_photo = taskSnapshot.getUploadSessionUri().toString();
                            dReferences.getRef().child("url_photo_profile").setValue(uri_photo);
                            dReferences.getRef().child("nama_lengkap").setValue(etNama.getText().toString());
                            dReferences.getRef().child("bio").setValue(etBio.getText().toString());
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Intent intentReg = new Intent(Register2Activity.this, SuccessRegister.class);
                            startActivity(intentReg);
                            finish();
                        }
                    });
                }
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

        if (requestCode == photo_max && resultCode == RESULT_OK && data !=null && data.getData() != null)
        {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(ivRegister);
        }
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}