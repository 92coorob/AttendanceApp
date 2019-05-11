package com.example.attendanceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Admin extends AppCompatActivity {

    TextView admin;
    Button update,back;
    SharedPreferences adminname_pref;
    String adminname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        admin = findViewById(R.id.txt_user);
        update = findViewById(R.id.btn_update);
        back = findViewById(R.id.btn_back_admin);

        adminname_pref = getSharedPreferences("students", MODE_PRIVATE);
        adminname = adminname_pref.getString("username", "");

        admin.setText(adminname);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changepass = new Intent(Admin.this, PopPassword.class);
                startActivity(changepass);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain = new Intent(Admin.this, AdminPage.class);
                startActivity(goToMain);
            }
        });
    }
}
