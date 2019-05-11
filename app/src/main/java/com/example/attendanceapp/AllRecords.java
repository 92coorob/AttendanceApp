package com.example.attendanceapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.attendanceapp.Adaptors.UserListAdapter;
import com.example.attendanceapp.Model.UserListModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllRecords extends AppCompatActivity {

    private ListView listViewStudents;
    private ArrayList<UserListModel> userList;
    private ArrayAdapter<UserListModel> userAdapter;
    DatabaseReference userData;
    private static final String TAG = "AllRecords";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_records);

        //adapter
        userList = new ArrayList<>();
        userAdapter = new UserListAdapter(this, userList);
        this.listViewStudents = findViewById(R.id.records_list);
        this.listViewStudents.setAdapter(userAdapter);

        listViewStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listViewStudents.setItemsCanFocus(false);

        userData = FirebaseDatabase.getInstance().getReference("users");

        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!userList.isEmpty()) {
                    userList.removeAll(userList);
                }
                try {
                    for (final DataSnapshot restSnapshot : dataSnapshot.getChildren()) {

                        //set variables
                        final String status = restSnapshot.child("status").getValue().toString();

                        if (status.equals("student")) {
                            final String student_name = restSnapshot.getKey();
                            final String last_seen = restSnapshot.child("Date").getValue().toString();
                            userList.add(new UserListModel(student_name, last_seen));
                        }
                    }
                    userAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(AllRecords.this, "Error loading data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        this.listViewStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserListModel rest = userList.get(position);
                Intent intent = new Intent(AllRecords.this, AttendanceAdmin.class);
                //forward restaurant name into nxt page
                intent.putExtra("user", rest.getName());
                startActivity(intent);
            }
        });



    }





}