package com.pemws14.armyoffriends.database;

import com.pemws14.armyoffriends.GameMechanics;

public class DbSoldier {

    int id;
    String name;
    int level;
    int rank;
    String created_at;

    public DbSoldier() {
    }

    public DbSoldier(String name, int level) {
        this.name = name;
        this.level = level;
        this.rank = GameMechanics.getRankByLevel(level);
    }

    public DbSoldier(int id, String name, int level, int rank, String created_at) {
        this(name, level);
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
