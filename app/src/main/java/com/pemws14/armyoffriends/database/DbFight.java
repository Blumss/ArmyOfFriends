package com.pemws14.armyoffriends.database;

public class DbFight {
    int id;
    String name;
    int strength;
    int maxLevel;
    String created_at;

    public DbFight() {
    }

    public DbFight(String name, int strength, int maxLevel) {
        this.name = name;
        this.strength = strength;
        this.maxLevel = maxLevel;
    }

    public DbFight(int id, String name, int strength, int maxLevel, String created_at) {
        this(name,strength,maxLevel);
        this.id = id;
        this.created_at = created_at;
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

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
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
}
