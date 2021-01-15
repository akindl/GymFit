package com.example.gymfit02.Models;

import java.util.HashMap;
import java.util.List;

public class DatabaseWorkoutExerciseModel {


    private String exerciseName;
    private String deviceName;
    private String creatorId;
    private String notes;
    private String trainingType;
    private List<String> workouts;



    // CONSTRUCTOR

    public DatabaseWorkoutExerciseModel() {
        // empty constructor needed for firebase
    }

    public DatabaseWorkoutExerciseModel(String exerciseName, String deviceName) {
        this.exerciseName = exerciseName;
        this.deviceName = deviceName;
    }


    // GETTER

    public String getExerciseName() {
        return exerciseName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public List<String> getWorkouts() {
        return workouts;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getNotes() {
        return notes;
    }

    public String getTrainingType() {
        return trainingType;
    }


    // SETTER


    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setWorkouts(List<String> workouts) {
        this.workouts = workouts;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }
}
