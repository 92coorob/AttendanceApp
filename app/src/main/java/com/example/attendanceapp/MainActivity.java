package com.example.attendanceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    GridLayout mainGrid;
    private CardView Attendance,student;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        Attendance = findViewById(R.id.Attendance);
        logout = findViewById(R.id.logout_main);
        student = findViewById(R.id.student);


        Attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAttendance = new Intent(MainActivity.this, Attendance.class);
                startActivity(goToAttendance);
            }
        });
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToStudent = new Intent(MainActivity.this, Student.class);
                startActivity(goToStudent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain = new Intent(MainActivity.this, LoginPage.class);
                startActivity(goToMain);
            }
        });
    }



    @Override
    public void onBackPressed() {
        //do nothing on backpress
    }
}
