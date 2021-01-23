package com.example.gymfit02.Models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

public class DatabaseExercisePerformanceModel {

    private int oneRepMax;
    private String notes;
    private FieldValue performanceDate;
    private int totalVolume;
    private String workoutId;
    private int setCount;

    public DatabaseExercisePerformanceModel() {
        // no argument constructor required for ProGuard (compress and optimize data)
    }

    public DatabaseExercisePerformanceModel(int oneRepMax, String notes, FieldValue performanceDate, int totalVolume, String workoutId, int setCount) {
        this.oneRepMax = oneRepMax;
        this.notes = notes;
        this.performanceDate = performanceDate;
        this.totalVolume = totalVolume;
        this.workoutId = workoutId;
        this.setCount = setCount;
    }


    // GETTER


    public int getOneRepMax() {
        return oneRepMax;
    }

    public String getNotes() {
        return notes;
    }

    public FieldValue getPerformanceDate() {
        return performanceDate;
    }

    public int getTotalVolume() {
        return totalVolume;
    }

    public String getWorkoutId() {
        return workoutId;
    }

    public int getSetCount() {
        return setCount;
    }

    // SETTER


    public void setOneRepMax(int oneRepMax) {
        this.oneRepMax = oneRepMax;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPerformanceDate(FieldValue performanceDate) {
        this.performanceDate = performanceDate;
    }

    public void setTotalVolume(int totalVolume) {
        this.totalVolume = totalVolume;
    }

    public void setWorkoutId(String workoutId) {
        this.workoutId = workoutId;
    }

    public void setSetCount(int setCount) {
        this.setCount = setCount;
    }
}
