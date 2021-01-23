package com.example.gymfit02.Models;


public class DatabaseExercisePerformanceSetModel {

    private int setNumber;
    private int reps;
    private int load;
    private String rpe;
    private Boolean check;
    private int oneRepMax;


    // CONSTRUCTOR

    public DatabaseExercisePerformanceSetModel() {
        // empty constructor needed for firebase
    }

    public DatabaseExercisePerformanceSetModel(int setNumber, int reps, int load, String rpe, int oneRepMax, Boolean check) {
        this.setNumber = setNumber;
        this.reps = reps;
        this.load = load;
        this.rpe = rpe;
        this.check = check;
        this.oneRepMax = oneRepMax;
    }


    // GETTER

    public int getSetNumber() {
        return setNumber;
    }

    public int getReps() {
        return reps;
    }

    public int getLoad() {
        return load;
    }

    public String getRpe() {
        return rpe;
    }

    public Boolean getCheck() {
        return check;
    }

    public int getOneRepMax() {
        return oneRepMax;
    }

    // SETTER

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public void setRpe(String rpe) {
        this.rpe = rpe;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public void setOneRepMax(int oneRepMax) {
        this.oneRepMax = oneRepMax;
    }
}
