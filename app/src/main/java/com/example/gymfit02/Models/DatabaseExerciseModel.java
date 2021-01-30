package com.example.gymfit02.Models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseExerciseModel {


    private String exerciseName;
    private String deviceName;
    private String creatorId;
    private String notes;
    private List<String> workouts;



    // CONSTRUCTOR

    public DatabaseExerciseModel() {}

    public DatabaseExerciseModel(String exerciseName, String deviceName, String creatorId, String notes, List<String> workouts) {
        this.exerciseName = exerciseName;
        this.deviceName = deviceName;
        this.creatorId = creatorId;
        this.notes = notes;
        this.workouts = workouts;
    }

    // GETTER

    public String getExerciseName() {
        return exerciseName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getNotes() {
        return notes;
    }

    public List<String> getWorkouts() {
        return workouts;
    }


    // SETTER

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setWorkouts(List<String> workouts) {
        this.workouts = workouts;
    }
}
