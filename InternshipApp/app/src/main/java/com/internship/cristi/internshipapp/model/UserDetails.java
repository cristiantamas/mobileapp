package com.internship.cristi.internshipapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cristi on 1/13/18.
 */

public class UserDetails implements Parcelable {
    String userId;
    String name;
    String suername;
    String mail;
    String type;
    String about;
    String teamId;
    String imagePath;

    public UserDetails(){}

    public UserDetails(String userId, String name, String suername, String mail, String type,  String about, String imagePath, String teamId) {
        this.userId = userId;
        this.name = name;
        this.teamId = teamId;
        this.suername = suername;
        this.mail = mail;
        this.type = type;
        this.about = about;
        this.imagePath = imagePath;
    }

    protected UserDetails(Parcel in) {
        userId = in.readString();
        name = in.readString();
        suername = in.readString();
        mail = in.readString();
        type = in.readString();
        about = in.readString();
        teamId = in.readString();
        imagePath = in.readString();
    }

    public static final Creator<UserDetails> CREATOR = new Creator<UserDetails>() {
        @Override
        public UserDetails createFromParcel(Parcel in) {
            return new UserDetails(in);
        }

        @Override
        public UserDetails[] newArray(int size) {
            return new UserDetails[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuername() {
        return suername;
    }

    public void setSuername(String suername) {
        this.suername = suername;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(name);
        parcel.writeString(suername);
        parcel.writeString(mail);
        parcel.writeString(type);
        parcel.writeString(about);
        parcel.writeString(teamId);
        parcel.writeString(imagePath);
    }
}

