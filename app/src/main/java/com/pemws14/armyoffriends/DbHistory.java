package com.pemws14.armyoffriends;


public class DbHistory {
    int id;
    String enemyName;
    int ownStrength;
    int ownMaxLevel;
    int enemyStrength;
    int enemyMaxLevel;
    boolean result;
    String created_at;

    public DbHistory() {
    }

    public DbHistory(int id, String enemyName, int ownStrength, int ownMaxLevel, int enemyStrength, int enemyMaxLevel, boolean result, String created_at) {
        this.id = id;
        this.ownStrength = ownStrength;
        this.ownMaxLevel = ownMaxLevel;
        this.enemyName = enemyName;
        this.enemyStrength = enemyStrength;
        this.enemyMaxLevel = enemyMaxLevel;
        this.result = result;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public void setEnemyName(String enemyName) {
        this.enemyName = enemyName;
    }

    public int getOwnStrength() {
        return ownStrength;
    }

    public void setOwnStrength(int ownStrength) {
        this.ownStrength = ownStrength;
    }

    public int getOwnMaxLevel() {
        return ownMaxLevel;
    }

    public void setOwnMaxLevel(int ownMaxLevel) {
        this.ownMaxLevel = ownMaxLevel;
    }

    public int getEnemyStrength() {
        return enemyStrength;
    }

    public void setEnemyStrength(int enemyStrength) {
        this.enemyStrength = enemyStrength;
    }

    public int getEnemyMaxLevel() {
        return enemyMaxLevel;
    }

    public void setEnemyMaxLevel(int enemyMaxLevel) {
        this.enemyMaxLevel = enemyMaxLevel;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
