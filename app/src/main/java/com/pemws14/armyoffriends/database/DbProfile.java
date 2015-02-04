package com.pemws14.armyoffriends.database;

import android.graphics.Bitmap;

public class DbProfile {
    private String serverID;
    private String userName;
    private Bitmap img;
    private int playerLevel;
    private int ep;
    private int armyStrength;
    private int maxSoldierLevel;
    private String created_at;

    public DbProfile() {
    }

    public DbProfile(String serverID, String userName, int playerLevel, int ep, int armyStrength, int maxSoldierLevel) {
        this.serverID = serverID;
        this.userName = userName;
        this.playerLevel = playerLevel;
        this.ep = ep;
        this.armyStrength = Math.max(armyStrength,1);
        this.maxSoldierLevel = maxSoldierLevel;
    }

    public DbProfile(String serverID, String userName, Bitmap img, int playerLevel, int ep, int armyStrength, int maxSoldierLevel) {
        this(serverID, userName, playerLevel, ep, armyStrength, maxSoldierLevel);
        this.img = img;
    }

    public DbProfile(String serverID, String userName, Bitmap img, int playerLevel, int ep, int armyStrength, int maxSoldierLevel, String created_at) {
        this(serverID, userName, img, playerLevel, ep, armyStrength, maxSoldierLevel);
        this.created_at = created_at;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public int getEp() {
        return ep;
    }

    public void setEp(int ep) {
        this.ep = ep;
    }

    public int getArmyStrength() {
        return armyStrength;
    }

    public void setArmyStrength(int armyStrength) {
        this.armyStrength = Math.max(armyStrength,1);
    }

    public int getMaxSoldierLevel() {
        return maxSoldierLevel;
    }

    public void setMaxSoldierLevel(int maxSoldierLevel) {
        this.maxSoldierLevel = maxSoldierLevel;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
