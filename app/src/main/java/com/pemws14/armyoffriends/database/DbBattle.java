package com.pemws14.armyoffriends.database;

/**
 * Created by Aquila on 03.12.2014.
 */
public class DbBattle {
    int id;
    String enemyName;
    int enemyStrength;
    int ownStrength;
    Boolean result;
    String created_at;

    public DbBattle() {
    }

    public DbBattle(String enemyName, int enemyStrength, int ownStrength, Boolean result) {
        this.enemyName = enemyName;
        this.enemyStrength = enemyStrength;
        this.ownStrength = ownStrength;
        this.result = result;
    }

    public DbBattle(int id, String enemyName, int enemyStrength, int ownStrength, Boolean result, String created_at) {
        this(enemyName, enemyStrength, ownStrength, result);
        this.id = id;
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

    public int getEnemyStrength() {
        return enemyStrength;
    }

    public void setEnemyStrength(int enemyStrength) {
        this.enemyStrength = enemyStrength;
    }

    public int getOwnStrength() {
        return ownStrength;
    }

    public void setOwnStrength(int ownStrength) {
        this.ownStrength = ownStrength;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
