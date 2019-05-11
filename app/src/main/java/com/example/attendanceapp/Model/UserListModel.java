package com.example.attendanceapp.Model;

import android.support.constraint.ConstraintLayout;

import java.io.Serializable;

public class UserListModel  implements Serializable {

    private String name;
    private String date;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }



    public UserListModel(String name, String date) {
        this.name = name;
        this.date = date;
    }

}