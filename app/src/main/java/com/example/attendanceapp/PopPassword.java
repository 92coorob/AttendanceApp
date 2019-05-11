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

public class PopPassword extends AppCompatActivity {

    int width, height;
    EditText oldpwd,newpwd,confpwd;
    Button confirm,close;
    String oldpass, newpass, confpass;
    private DatabaseReference myRef;
    String AES = "AES";
    String encrypt_key = "testencryptionkey";
    private String enc_password;
    SharedPreferences username_pref;
    String username,dec_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_password);

        oldpwd =findViewById(R.id.old_pwd);
        newpwd =findViewById(R.id.new_pwd);
        confpwd =findViewById(R.id.conf_pwd);
        confirm =findViewById(R.id.btn_confirm);
        //close = findViewById(R.id.btn_close);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        username_pref = getSharedPreferences("students", MODE_PRIVATE);
        username = username_pref.getString("username", "");
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

                oldpass = oldpwd.getText().toString();
                newpass = newpwd.getText().toString();
                confpass = confpwd.getText().toString();
                try {
                    enc_password = encrypt(newpass);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String check_pass=  dataSnapshot.child("password").getValue().toString();
                        try {
                            dec_password = decrypt(check_pass);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (dec_password.equals(oldpass) && confpass.equals(newpass))   {
                            myRef.child("password").setValue(enc_password);
                            oldpwd.setText("");
                            newpwd.setText("");
                            confpwd.setText("");


                            Toast.makeText(PopPassword.this, "Password changed", Toast.LENGTH_SHORT).show();
                        }
                        else if(!confpass.equals(newpass)){
                            Toast.makeText(PopPassword.this, "Confirmed password does not match new password", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(PopPassword.this, "Old password does not match exisitng records", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });

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

    private String decrypt(String password) throws Exception{
        SecretKeySpec key = generateKey(encrypt_key);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decValue = Base64.decode(password, Base64.DEFAULT);
        byte[] decPass = c.doFinal(decValue);
        String decryptedPass = new String(decPass);
        return decryptedPass;
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
