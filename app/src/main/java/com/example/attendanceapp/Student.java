package com.example.attendanceapp;

import android.app.ProgressDialog;
import android.content.ContentProvider;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static java.security.AccessController.getContext;

public class Student extends AppCompatActivity {

    TextView user;
    Button update,back;
    SharedPreferences username_pref;
    String username;
    ImageView profPic;
    private Uri imageUri;
    private StorageTask uploadTask;
    private static final int IMAGE_REQUEST = 1;
    private DatabaseReference usersRef, myRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("uploads");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);



        user = findViewById(R.id.txt_user);
        update = findViewById(R.id.btn_update);
        back = findViewById(R.id.btn_back_admin);
        profPic = findViewById(R.id.prof_pic);

        username_pref = getSharedPreferences("students", MODE_PRIVATE);
        username = username_pref.getString("username", "");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(username);

        user.setText(username);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain = new Intent(Student.this, PopPassword.class);
                startActivity(goToMain);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain = new Intent(Student.this, MainActivity.class);
                startActivity(goToMain);
            }
        });

        profPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent = new Intent();
             intent.setType("image/*");
             intent.setAction(Intent.ACTION_GET_CONTENT);
             startActivityForResult(intent, IMAGE_REQUEST);
            }
        });
        try {
            usersRef = myRef.child("imageURL");
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() == null){
                        profPic.setImageResource(R.drawable.user);
                    }
                    else {
                        String checkvalue = dataSnapshot.getValue().toString();

                        Glide.with(Student.this).load(checkvalue).into(profPic);
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });


        }
        catch (Exception e){
            profPic.setImageResource(R.drawable.user);

        }



    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = Student.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(Student.this);
        pd.setMessage("uploading");
        pd.show();

        if (imageUri != null){
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();

                    }
                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloaduri = task.getResult();
                        String mUri = downloaduri.toString();


                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", mUri);
                        myRef.updateChildren(map);
                        pd.dismiss();
                        onResume();
                    }
                    else {
                        Toast.makeText(Student.this,"Failed", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Student.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });

        } else {
            Toast.makeText(Student.this,"No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();

            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(Student.this,"Upload in progress", Toast.LENGTH_SHORT).show();
            }else {
                uploadImage();
            }
        }

    }

    @Override
    protected void onResume() {
        try {
            usersRef = myRef.child("imageURL");
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() == null){
                        profPic.setImageResource(R.drawable.user);
                    }
                    else {
                        String checkvalue = dataSnapshot.getValue().toString();

                        Glide.with(Student.this).load(checkvalue).into(profPic);
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });


        }
        catch (Exception e){
            profPic.setImageResource(R.drawable.user);

        }
        super.onResume();
    }
}
