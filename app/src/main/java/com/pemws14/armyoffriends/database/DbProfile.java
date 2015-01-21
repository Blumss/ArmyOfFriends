package com.pemws14.armyoffriends.database;

public class DbProfile {
    private int userID;
    private String serverID;
    private String userName;
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
        this.armyStrength = armyStrength;
        this.maxSoldierLevel = maxSoldierLevel;
    }

    public DbProfile(String serverID, String userName, int playerLevel, int ep, int armyStrength, int maxSoldierLevel, String created_at) {
        this(serverID, userName, playerLevel, ep, armyStrength, maxSoldierLevel);
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
