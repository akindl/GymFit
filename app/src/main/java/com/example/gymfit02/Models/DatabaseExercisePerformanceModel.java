package com.example.gymfit02.Models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

public class DatabaseExercisePerformanceModel {

    private int oneRepMax;
    private String notes;
    private Timestamp performanceDate;
    private int totalVolume;
    private String workoutId;
    private int setCount;
    private String exerciseName;
    private String deviceName;

    public DatabaseExercisePerformanceModel() {
        // no argument constructor required for ProGuard (compress and optimize data)
    }

    public DatabaseExercisePerformanceModel(
            int oneRepMax,
            String notes,
            Timestamp performanceDate,
            int totalVolume,
            String workoutId,
            int setCount,
            String exerciseName,
            String deviceName) {
        this.oneRepMax = oneRepMax;
        this.notes = notes;
        this.performanceDate = performanceDate;
        this.totalVolume = totalVolume;
        this.workoutId = workoutId;
        this.setCount = setCount;
        this.exerciseName = exerciseName;
        this.deviceName = deviceName;
    }


    // GETTER


    public int getOneRepMax() {
        return oneRepMax;
    }

    public String getNotes() {
        return notes;
    }

    public Timestamp getPerformanceDate() {
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

    public String getExerciseName() {

        return exerciseName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    // SETTER

    public void setOneRepMax(int oneRepMax) {
        this.oneRepMax = oneRepMax;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPerformanceDate(Timestamp performanceDate) {
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

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }


}
