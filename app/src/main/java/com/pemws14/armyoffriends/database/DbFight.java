package com.pemws14.armyoffriends.database;

import android.graphics.Bitmap;

public class DbFight {
    private int id;
    private String name;
    private Bitmap img;
    private int playerLevel;
    private int strength;
    private int maxLevel;
    private String created_at;
    private long created_at_unix;

    public DbFight() {
    }

    public DbFight(String name, Bitmap img, int playerLevel, int strength, int maxLevel) {
        this.setName(name);
        this.setImg(img);
        this.setPlayerLevel(playerLevel);
        this.setStrength(Math.max(strength,1));
        this.setMaxLevel(maxLevel);
    }

    public DbFight(int id, String name, Bitmap img, int playerLevel, int strength, int maxLevel, String created_at, long created_at_unix) {
        this(name, img, playerLevel, strength, maxLevel);
        this.setId(id);
        this.setCreated_at(created_at);
        this.setCreated_at_Unix(created_at_unix);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = Math.max(strength,1);
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
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

    public void setCreated_at_Unix(long created_at_unix) {
        this.created_at_unix = created_at_unix;
    }
}
