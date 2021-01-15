package com.example.gymfit02.Models;

import java.util.HashMap;

public class DatabaseExerciseModel {


    private String name;
    private String notes;
    private HashMap<String, Long> infos;



    // CONSTRUCTOR

    public DatabaseExerciseModel() {}

    public DatabaseExerciseModel(String name, String notes) {
        this.name = name;
        this.notes = notes;
    }


    // GETTER

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public HashMap<String, Long> getInfos() {
        return infos;
    }

    public String getNumberOfSets() {
        return getInfos().get("numberOfSets").toString() + " Sets";
    }

    public String getVolume() {
        return getInfos().get("volume").toString() + " kg";
    }

    public String getMaxLoad() {
        return getInfos().get("maxLoad").toString() + " kg";
    }



    // SETTER

    public void setName(String name) {
        this.name = name;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setInfos(HashMap<String, Long> infos) {
        this.infos = infos;
    }


}
