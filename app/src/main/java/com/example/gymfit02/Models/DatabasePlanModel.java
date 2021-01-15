package com.example.gymfit02.Models;

public class DatabasePlanModel {

    private String name;
    private Long numberOfExercises;
    private Long numberOfSets;
    private Long minRepetition;
    private Long maxRepetition;
    private Long totalVolume;


    // CONSTRUCTOR

    public DatabasePlanModel() {}

    public DatabasePlanModel(String name) {
        this.name = name;
    }


    // GETTER


    public String getName() {
        return name;
    }

    public Long getMinRepetition() {
        return minRepetition;
    }

    public Long getMaxRepetition() {
        return maxRepetition;
    }

    public String getTotalVolume() {
        return totalVolume.toString() + " kg";
    }

    public String getRepetitionRange() {
        return minRepetition.toString() + " - " + maxRepetition.toString() + " Wdh ";
    }

    public String getNumberOfExercises() {
        return numberOfExercises.toString() + " Übungen";
    }

    public String getNumberOfSets() {
        return numberOfSets.toString() + " Sätze";
    }



}
