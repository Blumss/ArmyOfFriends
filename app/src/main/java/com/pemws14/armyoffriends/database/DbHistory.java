package com.pemws14.armyoffriends.database;


import android.graphics.Bitmap;

public class DbHistory {
    private int id;
    private int ownPlayerLevel;
    private int ownStrength;
    private int ownMaxLevel;
    private String enemyName;
    private Bitmap img;
    private int enemyPlayerLevel;
    private int enemyStrength;
    private int enemyMaxLevel;
    private boolean result;
    private String created_at;
    private long created_at_unix;


    public DbHistory() {
    }

    public DbHistory(int ownPlayerLevel, int ownStrength, int ownMaxLevel, String enemyName, Bitmap img, int enemyPlayerLevel, int enemyStrength, int enemyMaxLevel, boolean result) {
        this.setOwnPlayerLevel(ownPlayerLevel);
        this.setOwnStrength(ownStrength);
        this.setOwnMaxLevel(ownMaxLevel);
        this.setEnemyName(enemyName);
        this.setImg(img);
        this.setEnemyPlayerLevel(enemyPlayerLevel);
        this.setEnemyStrength(enemyStrength);
        this.setEnemyMaxLevel(enemyMaxLevel);
        this.setResult(result);
    }

    public DbHistory(int id, int ownPlayerLevel, int ownStrength, int ownMaxLevel, String enemyName, Bitmap img, int enemyPlayerLevel, int enemyStrength, int enemyMaxLevel, boolean result, String created_at, long created_at_unix) {
        this(ownPlayerLevel, ownStrength, ownMaxLevel, enemyName, img, enemyPlayerLevel, enemyStrength, enemyMaxLevel, result);
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

    public int getOwnPlayerLevel() {
        return ownPlayerLevel;
    }

    public void setOwnPlayerLevel(int ownPlayerLevel) {
        this.ownPlayerLevel = ownPlayerLevel;
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

    public String getEnemyName() {
        return enemyName;
    }

    public void setEnemyName(String enemyName) {
        this.enemyName = enemyName;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public int getEnemyPlayerLevel() {
        return enemyPlayerLevel;
    }

    public void setEnemyPlayerLevel(int enemyPlayerLevel) {
        this.enemyPlayerLevel = enemyPlayerLevel;
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

    public long getCreated_at_Unix() {
        return created_at_unix;
    }

    public void setCreated_at_Unix(long created_at_unix) {
        this.created_at_unix = created_at_unix;
    }

}
