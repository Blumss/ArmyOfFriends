package com.pemws14.armyoffriends.database;

import android.graphics.Bitmap;

import com.pemws14.armyoffriends.GameMechanics;

/**
 * Created by Martin on 24.01.2015.
 */
public class DbAchievement {
    private int id;
    private String title;
    private String description;
    private int required;
    private int achieved;
    private int fulfilled;
    private String created_at;
    private long created_at_unix;

    public DbAchievement() {
    }

    public DbAchievement(String title, String description, int required, int achieved) {
        this.setTitle(title);
        this.setDescription(description);
        this.setRequired(required);
        this.setAchieved(achieved);
    }

    public DbAchievement(int id, String title, String description, int required, int achieved, int fulfilled, String created_at, Long created_at_unix) {
        this(title, description, required, achieved);
        this.setId(id);
        this.setFulfilled(fulfilled);
        this.setCreated_at(created_at);
        this.setCreated_at_Unix(created_at_unix);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description= description;
    }

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public int getAchieved() {
        return achieved;
    }

    public void setAchieved(int achieved) {
        this.achieved = achieved;
    }

    public int getFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(int fulfilled) {
        this.fulfilled = fulfilled;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getCreated_at_Unix() {
        return created_at_unix;
    }

    public void setCreated_at_Unix(long created_at) {
        this.created_at_unix = created_at_unix;
    }

}
