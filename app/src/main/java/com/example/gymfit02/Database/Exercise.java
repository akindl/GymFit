package com.example.gymfit02.Database;

import android.media.Image;

import java.util.HashMap;

public class Exercise {

    private String name;
    private String note;
    private Image image;
    private HashMap<Object, String> infos;


    // private History (Subcollection)


    // CONSTRUCTOR

    public Exercise(String name) {
        this.name = name;
    }


    // GETTER

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public HashMap<Object, String> getInfos() {
        return infos;
    }


    // SETTER

    public void setNote(String note) {
        this.note = note;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setInfos(HashMap<Object, String> infos) {
        this.infos = infos;
    }
}
