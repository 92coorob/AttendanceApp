package com.example.attendanceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Student extends AppCompatActivity {

    TextView user;
    Button update;
    SharedPreferences username_pref;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        user = findViewById(R.id.txt_user);
        update = findViewById(R.id.btn_update);

        username_pref = getSharedPreferences("students", MODE_PRIVATE);
        username = username_pref.getString("username", "");

        user.setText(username);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain = new Intent(Student.this, PopPassword.class);
                startActivity(goToMain);
            }
        });
    }
}
