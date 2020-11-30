package com.example.gymfit02.Database;

import android.media.Image;

public class User {

    private String name;
    private String email;
    private String password;
    private Image profilImage;
    private boolean kgOrPounds; // true = kg | false = pounds


    // CONSTRUCTOR

    public User() {}

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }



    // GETTER

    public boolean isKgOrPounds() {
        return kgOrPounds;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Image getProfilImage() {
        return profilImage;
    }


    // SETTER

    public void setKgOrPounds(boolean kgOrPounds) {
        this.kgOrPounds = kgOrPounds;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfilImage(Image profilImage) {
        this.profilImage = profilImage;
    }


}
