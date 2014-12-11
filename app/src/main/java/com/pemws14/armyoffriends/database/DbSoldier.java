package com.pemws14.armyoffriends.database;

public class DbSoldier {

    int id;
    String name;
    int level;
    int rank;
    String created_at;

    public DbSoldier() {
    }

    public DbSoldier(int id, String name, int level, int rank, String created_at) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.rank = rank;
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
