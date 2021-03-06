package com.example.attendanceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

public class AdminPage extends AppCompatActivity {

    CardView add_record, check_record, admin;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        add_record = findViewById(R.id.student);
        check_record = findViewById(R.id.attend_admin);
        logout = findViewById(R.id.button_logout);
        admin = findViewById(R.id.admin);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain = new Intent(AdminPage.this, Admin.class);
                startActivity(goToMain);
            }
        });


        add_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain = new Intent(AdminPage.this, CreateRecord.class);
                startActivity(goToMain);
            }
        });

        check_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain = new Intent(AdminPage.this, AllRecords.class);
                startActivity(goToMain);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(AdminPage.this, LoginPage.class);
                startActivity(back);
            }
        });
    }
    @Override
    public void onBackPressed() {
        //do nothing on backpress
    }
}
