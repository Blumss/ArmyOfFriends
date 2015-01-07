package com.pemws14.armyoffriends.database;

public class DbProfile {
    private int userID;
    private String userName;
    private int playerLevel;
    private int ep;
    private int armyStrength;
    private int maxSoldierLevel;
    private String created_at;

    public DbProfile() {
    }

    public DbProfile(String userName, int playerLevel, int ep, int armyStrength, int maxSoldierLevel) {
        this.userName = userName;
        this.playerLevel = playerLevel;
        this.ep = ep;
        this.armyStrength = armyStrength;
        this.maxSoldierLevel = maxSoldierLevel;
    }

    public DbProfile(int serverID, String userName, int playerLevel, int ep, int armyStrength, int maxSoldierLevel, String created_at) {
        this(userName, playerLevel, ep, armyStrength, maxSoldierLevel);
        this.userID = serverID;
        this.created_at = created_at;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        this.armyStrength = armyStrength;
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
