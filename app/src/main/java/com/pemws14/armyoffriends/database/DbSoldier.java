package com.pemws14.armyoffriends.database;

import com.pemws14.armyoffriends.GameMechanics;

public class DbSoldier {
    private int id;
    private String name;
    private int level;
    private int rank;
    private String created_at;
    private long created_at_unix;

    public DbSoldier() {
    }

    public DbSoldier(String name, int level) {
        this.setName(name);
        this.setLevel(level);
        this.setRank(GameMechanics.getRankByLevel(level));
    }

    public DbSoldier(int id, String name, int level, int rank, String created_at, Long created_at_unix) {
        this(name, level);
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

    public long getCreated_at_Unix() {
        return created_at_unix;
    }

    public void setCreated_at_Unix(long created_at) {
        this.created_at_unix = created_at_unix;
    }
}
