package com.example.gymfit02.Models;

import com.google.firebase.Timestamp;

import java.util.Date;

public class DatabaseWorkoutModel {


    private String name;
    private Date executionDate;



    // CONSTRUCTOR

    public DatabaseWorkoutModel() {
        // empty constructor needed for firebase
    }

    public DatabaseWorkoutModel(String name, Timestamp executionDate) {
        this.name = name;
        this.executionDate = executionDate.toDate();
    }


    // GETTER

    public String getName() {
        return name;
    }

    public String getExecutionDate() {
        return executionDate.toString();
    }


    // SETTER

    public void setName(String name) {
        this.name = name;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }
}
