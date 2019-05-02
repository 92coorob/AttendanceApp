package com.example.attendanceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AttendanceAdmin extends AppCompatActivity {

    private Button week1,week2,week3,week4,week5,week6,week7,week8,week9,week10,delete;
    private DatabaseReference usersRef, myRef;
    String name;
    String year;
    String month,present;
    String day;
    String time;
    String[] temp,temp2;
    String hour;
    String minutes;
    private TextView displayText;
    SharedPreferences username_pref;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_admin);

        Intent intent = getIntent();
        username = intent.getStringExtra("user");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(username);

        displayText = findViewById(R.id.displayAttendance);
        week1 = findViewById(R.id.btn_week1);
        week2 = findViewById(R.id.btn_week2);
        week3 = findViewById(R.id.btn_week3);
        week4 = findViewById(R.id.btn_week4);
        week5 = findViewById(R.id.btn_week5);
        week6 = findViewById(R.id.btn_week6);
        week7 = findViewById(R.id.btn_week7);
        week8 = findViewById(R.id.btn_week8);
        week9 = findViewById(R.id.btn_week9);
        week10 = findViewById(R.id.btn_week10);
        delete = findViewById(R.id.btn_delete);

        usersRef = myRef.child("Date");

        SharedPreferences prefs = getSharedPreferences("bgColour", MODE_PRIVATE);
        present = prefs.getString("colour", "");
        if(present.equals("7")){
            week5.setBackgroundColor(getResources().getColor(R.color.green));
        }
        else if(present.equals("8")){

            week8.setBackgroundColor(getResources().getColor(R.color.green));
        }
        else if(present.equals("9")){

            week9.setBackgroundColor(getResources().getColor(R.color.green));
        }
        else if(present.equals("10")){

            week10.setBackgroundColor(getResources().getColor(R.color.green));
        }



        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.removeValue();
                Intent goToMain = new Intent(AttendanceAdmin.this, AllRecords.class);
                startActivity(goToMain);



            }
        });

        myRef.child("Date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String checkvalue = dataSnapshot.getValue().toString();
                name = dataSnapshot.getRef().toString();
                String[] parts = checkvalue.split("-");
                temp = parts[2].split(" ");
                year = parts[0];
                month = parts[1];
                day = temp[0];
                time = temp[1];
                temp2 = time.split(":");
                hour = temp2[0];
                minutes = temp2[1];
                int inthour = Integer.parseInt(hour);
                int intmonth = Integer.parseInt(month);
                int intday = Integer.parseInt(day);


                if(intmonth == 05 && intday == 02 && inthour >=17 && inthour <20){

                    SharedPreferences prefs = getSharedPreferences("bgColour", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    String colourSelected = "7";
                    editor.putString("colour", colourSelected);
                    editor.commit();

                }
                else if(intmonth == 05 && intday == 8 && inthour >=17 && inthour <20){

                    SharedPreferences prefs = getSharedPreferences("bgColour", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    String colourSelected = "8";
                    editor.putString("colour", colourSelected);
                    editor.commit();

                }

                else if(intmonth == 05 && intday == 15 && inthour >=17 && inthour <20){

                    SharedPreferences prefs = getSharedPreferences("bgColour", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    String colourSelected = "9";
                    editor.putString("colour", colourSelected);
                    editor.commit();

                }
                else if(intmonth == 05 && intday == 22 && inthour >=17 && inthour <20){

                    SharedPreferences prefs = getSharedPreferences("bgColour", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    String colourSelected = "10";
                    editor.putString("colour", colourSelected);
                    editor.commit();

                }
                displayText.setText(username + " Last seen at: "+checkvalue);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
