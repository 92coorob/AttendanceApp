package com.example.attendanceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ResetPasswordPop extends AppCompatActivity {

    int width, height;
    EditText newpwd,confpwd;
    Button confirm,close;
    String  newpass, confpass;
    private DatabaseReference myRef;
    String AES = "AES";
    String encrypt_key = "testencryptionkey";
    private String enc_password;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_pop);


        newpwd =findViewById(R.id.new_pwd_reset);
        confpwd =findViewById(R.id.conf_pwd_reset);
        confirm =findViewById(R.id.btn_confirm_reset);
        //close = findViewById(R.id.btn_close);

        Intent resetpw = getIntent();
        username = resetpw.getStringExtra("student");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(username);
/*
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAttendance = new Intent(PopPassword.this, Student.class);
                startActivity(goToAttendance);
            }
        });*/

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newpass = newpwd.getText().toString();
                confpass = confpwd.getText().toString();
                try {
                    enc_password = encrypt(newpass);
                    if (confpass.equals(newpass) && !confpass.isEmpty())   {
                        myRef.child("password").setValue(enc_password);
                        newpwd.setText("");
                        confpwd.setText("");

                        Toast.makeText(ResetPasswordPop.this, "Password changed", Toast.LENGTH_SHORT).show();
                        finish();

                    }

                    if (confpass.isEmpty() || newpass.isEmpty()){
                        Toast.makeText(ResetPasswordPop.this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        width = dm.widthPixels;
        height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
    }

    private String encrypt(String password) throws Exception{

        SecretKeySpec key = generateKey(encrypt_key);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encValue = c.doFinal(password.getBytes());
        String encryptedPass = Base64.encodeToString(encValue, Base64.DEFAULT);
        return encryptedPass;
    }


    //generate key
    private SecretKeySpec generateKey(String encrypt) throws Exception{
        final MessageDigest pwDigest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = encrypt.getBytes("UTF-8");
        pwDigest.update(bytes, 0, bytes.length);
        byte[] key = pwDigest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }
}
