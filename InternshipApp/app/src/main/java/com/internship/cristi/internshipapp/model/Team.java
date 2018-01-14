package com.internship.cristi.internshipapp.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cristi on 1/13/18.
 */

public class Team {

    String name;
    String about;
    String technology;

    public Team(String name, String about, String technology) {
        this.name = name;
        this.about = about;
        this.technology = technology;
    }

    public Team() {

    }

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public String getTechnology() {
        return technology;
    }
}
