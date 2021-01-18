package com.example.gymfit02.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DatabaseExercisePerformanceModel {

    private HashMap<String, Object> singleSet;
    private ArrayList<Object> sets;
    private String notes;
    private Date performanceDate;
    private Long totalVolume;


    // CONSTRUCTOR

    public DatabaseExercisePerformanceModel(HashMap<String, Object> singleSet, ArrayList<Object> sets) {
        this.singleSet = singleSet;
        this.sets = sets;
    }

    // GETTER

    public HashMap<String, Object> getSingleSet() {
        return singleSet;
    }

    public ArrayList<Object> getSets() {
        return sets;
    }

    public String getNotes() {
        return notes;
    }

    public Date getPerformanceDate() {
        return performanceDate;
    }

    public Long getTotalVolume() {
        return totalVolume;
    }


    // SETTER


    public void setSingleSet(HashMap<String, Object> singleSet) {
        this.singleSet = singleSet;
    }

    public void setSets(ArrayList<Object> sets) {
        this.sets = sets;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPerformanceDate(Date performanceDate) {
        this.performanceDate = performanceDate;
    }

    public void setTotalVolume(Long totalVolume) {
        this.totalVolume = totalVolume;
    }
}
