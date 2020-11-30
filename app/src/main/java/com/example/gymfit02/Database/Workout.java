package com.example.gymfit02.Database;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Workout {

    //TODO Vielleicht muss ein anderer Typ Date gewählt werden
    private Date date;
    private Map<String, Object> information = new HashMap<>();

    public Workout(Date date) {
        this.date = date;
        information.put("numberOfSets", 0);
        information.put("averageOfReps", 0);
        information.put("averageOfLoad", 0);
        information.put("volume", 0);
    }

}
