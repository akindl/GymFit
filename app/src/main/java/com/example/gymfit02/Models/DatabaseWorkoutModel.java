package com.example.gymfit02.Models;

import com.google.firebase.Timestamp;

public class DatabaseWorkoutModel {


    private String name;
    private Timestamp executionDate;



    // CONSTRUCTOR

    public DatabaseWorkoutModel() {
        // empty constructor needed for firebase
    }

    public DatabaseWorkoutModel(String name, Timestamp executionDate) {
        this.name = name;
        this.executionDate = executionDate;
    }


    // GETTER

    public String getName() {
        return name;
    }

    public Timestamp getExecutionDate() {
        return executionDate;
    }


    // SETTER

    public void setName(String name) {
        this.name = name;
    }

    public void setExecutionDate(Timestamp executionDate) {
        this.executionDate = executionDate;
    }
}
