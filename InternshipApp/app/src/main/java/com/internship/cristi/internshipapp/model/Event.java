package com.internship.cristi.internshipapp.model;

/**
 * Created by cristi on 1/14/18.
 */

public class Event {

    String id;
    String name;
    String datetime;
    String owner;
    String room;
    String details;

    public Event(){}

    public Event(String name, String datetime, String owner, String room, String details) {
        this.name = name;
        this.datetime = datetime;
        this.owner = owner;
        this.room = room;
        this.details = details;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
