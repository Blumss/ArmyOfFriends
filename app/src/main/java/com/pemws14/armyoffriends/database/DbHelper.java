package com.pemws14.armyoffriends.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;

import com.pemws14.armyoffriends.GameMechanics;

public class DbHelper extends SQLiteOpenHelper {

    private static final int maxArmySize = GameMechanics.getMaxArmySize(4); //TODO: get real current MaxArmySize

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "AoFDatabase";

    // Table Names
    private static final String TABLE_SOLDIER = "soldier";
    private static final String TABLE_FIGHT = "fight";
    private static final String TABLE_HISTORY = "history";
    private static final String TABLE_PROFILE = "profile";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_CREATED_AT_UNIX = "created_at_unix";
    private static final String KEY_PLAYER_NAME = "name";
    private static final String KEY_PLAYER_LEVEL = "player_level";
    private static final String KEY_STRENGTH = "strength";
    private static final String KEY_MAX_LEVEL = "maxLevel";

    // soldier Table - column names
    private static final String KEY_SOLDIER_LEVEL = "level";
    private static final String KEY_RANK = "rank";

    //history Table - column names
    private static final String KEY_OWN_PLAYER_LEVEL = "own_player_level";
    private static final String KEY_OWN_STRENGTH = "own_strength";
    private static final String KEY_OWN_MAX_LEVEL = "own_max_level";
    private static final String KEY_ENEMY_NAME = "enemy_name";
    private static final String KEY_ENEMY_PLAYER_LEVEL = "enemy_player_level";
    private static final String KEY_ENEMY_STRENGTH = "enemy_strength";
    private static final String KEY_ENEMY_MAX_LEVEL = "enemy_max_level";
    private static final String KEY_RESULT = "result";

    //profile Table - column names
    private static final String KEY_USERID = "userID";
    private static final String KEY_EP = "ep";


    // Table Create Statements
    // soldier table create statement
    private static final String CREATE_TABLE_SOLDIER = "CREATE TABLE "
            + TABLE_SOLDIER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PLAYER_NAME
            + " TEXT," + KEY_SOLDIER_LEVEL + " INTEGER," + KEY_RANK + " INTEGER," + KEY_CREATED_AT
            + " DATETIME," + KEY_CREATED_AT_UNIX + " LONG" + ")";

    // fight table create statement
    private static final String CREATE_TABLE_FIGHT = "CREATE TABLE "
            + TABLE_FIGHT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PLAYER_NAME
            + " TEXT," + KEY_PLAYER_LEVEL + " INTEGER," + KEY_STRENGTH + " INTEGER," + KEY_MAX_LEVEL + " INTEGER," + KEY_CREATED_AT
            + " DATETIME" + ")";

    //history table create statement
    private static final String CREATE_TABLE_HISTORY = "CREATE TABLE "
            + TABLE_HISTORY + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_OWN_PLAYER_LEVEL + " INTEGER,"
            + KEY_OWN_STRENGTH + " INTEGER," + KEY_OWN_MAX_LEVEL + " INTEGER,"  + KEY_ENEMY_NAME + " TEXT,"
            + KEY_ENEMY_PLAYER_LEVEL + " INTEGER,"+ KEY_ENEMY_STRENGTH + " INTEGER," + KEY_ENEMY_MAX_LEVEL + " INTEGER,"
            + KEY_RESULT + " BOOLEAN," + KEY_CREATED_AT + " DATETIME" + ")";

    //profile table create statement
    private static final String CREATE_TABLE_PROFILE = "CREATE TABLE "
            + TABLE_PROFILE + "(" + KEY_USERID + " INTEGER PRIMARY KEY," + KEY_PLAYER_NAME
            + " TEXT," + KEY_PLAYER_LEVEL + " INTEGER," + KEY_EP + " INTEGER," +  KEY_STRENGTH + " INTEGER," + KEY_MAX_LEVEL + " INTEGER," + KEY_CREATED_AT
            + " DATETIME" + ")";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_SOLDIER);
        db.execSQL(CREATE_TABLE_FIGHT);
        db.execSQL(CREATE_TABLE_HISTORY);
        db.execSQL(CREATE_TABLE_PROFILE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOLDIER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIGHT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        // create new tables
        onCreate(db);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public void removeDb(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOLDIER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIGHT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
    }

    /*
    * Creating a soldier
    */
    public long createSoldier(DbSoldier soldier) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_NAME, soldier.getName());
        values.put(KEY_SOLDIER_LEVEL, soldier.getLevel());
        values.put(KEY_RANK, soldier.getRank());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_CREATED_AT_UNIX, getUnix());

        // insert row
        long soldier_id = db.insert(TABLE_SOLDIER, null, values);

        return soldier_id;
    }

    /*
    * SELECT * FROM soldier WHERE id = i;
    */
    public DbSoldier getSoldier(long soldier_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_SOLDIER + " WHERE "
                + KEY_ID + " = " + soldier_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        DbSoldier soldier = new DbSoldier();
        soldier.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        soldier.setName((c.getString(c.getColumnIndex(KEY_PLAYER_NAME))));
        soldier.setLevel(c.getInt(c.getColumnIndex(KEY_SOLDIER_LEVEL)));
        soldier.setRank(c.getInt(c.getColumnIndex(KEY_RANK)));
        soldier.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        soldier.setCreated_at_Unix(c.getLong(c.getColumnIndex(KEY_CREATED_AT_UNIX)));

        return soldier;
    }

    /*
    * SELECT * FROM soldier WHERE rank = r;
    */
    public List<DbSoldier> getSoldiersWithRank(int soldier_rank) {
        List<DbSoldier> soldiers = new ArrayList<DbSoldier>();
        String selectQuery = "SELECT  * FROM " + TABLE_SOLDIER + " WHERE " + KEY_RANK + " = " + soldier_rank + " ORDER BY " + KEY_SOLDIER_LEVEL + " DESC ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                DbSoldier soldier = new DbSoldier();
                soldier.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                soldier.setName((c.getString(c.getColumnIndex(KEY_PLAYER_NAME))));
                soldier.setLevel(c.getInt(c.getColumnIndex(KEY_SOLDIER_LEVEL)));
                soldier.setRank(c.getInt(c.getColumnIndex(KEY_RANK)));
                soldier.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                soldier.setCreated_at_Unix(c.getLong(c.getColumnIndex(KEY_CREATED_AT_UNIX)));

                // adding to soldier list
                soldiers.add(soldier);
            } while (c.moveToNext());
        }

        return soldiers;
    }

    /*
    * SELECT MAX(level) FROM soldier;
    */
    public int getMaxLevel() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT MAX(" + KEY_SOLDIER_LEVEL + ") AS maxLevel FROM " + TABLE_SOLDIER ;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        return c.getInt(c.getColumnIndex("maxLevel"));
    }

    /*
* SELECT * FROM soldier ORDER BY level DESC;
* */
    public List<DbSoldier> getLimitedSoldiers() {
        List<DbSoldier> soldiers = new ArrayList<DbSoldier>();
        String selectQuery = "SELECT  * FROM " + TABLE_SOLDIER + " ORDER BY " + KEY_SOLDIER_LEVEL + " DESC " + " LIMIT " + maxArmySize;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                DbSoldier soldier = new DbSoldier();
                soldier.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                soldier.setName((c.getString(c.getColumnIndex(KEY_PLAYER_NAME))));
                soldier.setLevel(c.getInt(c.getColumnIndex(KEY_SOLDIER_LEVEL)));
                soldier.setRank(c.getInt(c.getColumnIndex(KEY_RANK)));
                soldier.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to soldier list
                soldiers.add(soldier);
            } while (c.moveToNext());
        }

        return soldiers;
    }

    /*
    * SELECT * FROM soldier ORDER BY level DESC;
    * */
    public List<DbSoldier> getAllSoldiers() {
        List<DbSoldier> soldiers = new ArrayList<DbSoldier>();
        String selectQuery = "SELECT  * FROM " + TABLE_SOLDIER + " ORDER BY " + KEY_SOLDIER_LEVEL + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                DbSoldier soldier = new DbSoldier();
                soldier.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                soldier.setName((c.getString(c.getColumnIndex(KEY_PLAYER_NAME))));
                soldier.setLevel(c.getInt(c.getColumnIndex(KEY_SOLDIER_LEVEL)));
                soldier.setRank(c.getInt(c.getColumnIndex(KEY_RANK)));
                soldier.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to soldier list
                soldiers.add(soldier);
            } while (c.moveToNext());
        }

        return soldiers;
    }

    /*
    * Updating a soldier
    */
    public int updateSoldier(DbSoldier soldier) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_NAME, soldier.getName());
        values.put(KEY_SOLDIER_LEVEL, soldier.getLevel());
        values.put(KEY_RANK, soldier.getRank());
        values.put(KEY_CREATED_AT, soldier.getCreated_at());

        // updating row
        return db.update(TABLE_SOLDIER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(soldier.getId()) });
    }

    /*
    * Deleting a soldier
    */
    public void deleteSoldier(long soldier_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SOLDIER, KEY_ID + " = ?",
                new String[] { String.valueOf(soldier_id) });
    }

    /*
    * Creating a Fight
    */
    public long createFight(DbFight fight) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_NAME, fight.getName());
        values.put(KEY_PLAYER_LEVEL,fight.getPlayerLevel());
        values.put(KEY_STRENGTH,fight.getStrength());
        values.put(KEY_MAX_LEVEL, fight.getMaxLevel());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long fight_id = db.insert(TABLE_FIGHT, null, values);

        return fight_id;
    }

    /*
    * SELECT * FROM fight WHERE id = i;
    */
    public DbFight getFight(long fight_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_FIGHT + " WHERE "
                + KEY_ID + " = " + fight_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        DbFight fight = new DbFight();
        fight.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        fight.setName((c.getString(c.getColumnIndex(KEY_PLAYER_NAME))));
        fight.setPlayerLevel(c.getInt(c.getColumnIndex(KEY_PLAYER_LEVEL)));
        fight.setStrength(c.getInt(c.getColumnIndex(KEY_STRENGTH)));
        fight.setMaxLevel(c.getInt(c.getColumnIndex(KEY_MAX_LEVEL)));
        fight.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return fight;
    }


    /*
    * SELECT * FROM fight;
    */
    public List<DbFight> getAllFights() {
        List<DbFight> fights = new ArrayList<DbFight>();
        String selectQuery = "SELECT  * FROM " + TABLE_FIGHT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                DbFight fight = new DbFight();
                fight.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                fight.setName((c.getString(c.getColumnIndex(KEY_PLAYER_NAME))));
                fight.setPlayerLevel(c.getInt(c.getColumnIndex(KEY_PLAYER_LEVEL)));
                fight.setStrength(c.getInt(c.getColumnIndex(KEY_STRENGTH)));
                fight.setMaxLevel(c.getInt(c.getColumnIndex(KEY_MAX_LEVEL)));
                fight.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to fight list
                fights.add(fight);
            } while (c.moveToNext());
        }

        return fights;
    }

    /*
    * Updating a fight
    */
    public int updateFight(DbFight fight) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_NAME, fight.getName());
        values.put(KEY_PLAYER_LEVEL,fight.getPlayerLevel());
        values.put(KEY_STRENGTH,fight.getStrength());
        values.put(KEY_MAX_LEVEL, fight.getMaxLevel());
        values.put(KEY_CREATED_AT, getDateTime());

        // updating row
        return db.update(TABLE_FIGHT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(fight.getId()) });
    }

    /*
    * Deleting a fight
    */
    public void deleteFight(long fight_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FIGHT, KEY_ID + " = ?",
                new String[] { String.valueOf(fight_id) });
    }

    /*
    * Creating a History Entry
    */
    public long createHistory(DbHistory history) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_OWN_PLAYER_LEVEL, history.getOwnPlayerLevel());
        values.put(KEY_OWN_STRENGTH, history.getOwnStrength());
        values.put(KEY_OWN_MAX_LEVEL, history.getOwnMaxLevel());
        values.put(KEY_ENEMY_NAME, history.getEnemyName());
        values.put(KEY_ENEMY_PLAYER_LEVEL, history.getEnemyPlayerLevel());
        values.put(KEY_ENEMY_STRENGTH, history.getEnemyStrength());
        values.put(KEY_ENEMY_MAX_LEVEL, history.getEnemyMaxLevel());
        values.put(KEY_RESULT, history.getResult());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long history_id = db.insert(TABLE_HISTORY, null, values);

        return history_id;
    }

    /*
    * SELECT * FROM history WHERE id = i;
    */
    public DbHistory getHistory(long history_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_HISTORY + " WHERE "
                + KEY_ID + " = " + history_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        DbHistory history = new DbHistory();
        history.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        history.setOwnPlayerLevel(c.getInt(c.getColumnIndex(KEY_OWN_PLAYER_LEVEL)));
        history.setOwnStrength(c.getInt(c.getColumnIndex(KEY_OWN_STRENGTH)));
        history.setOwnMaxLevel(c.getInt(c.getColumnIndex(KEY_OWN_MAX_LEVEL)));
        history.setEnemyName(c.getString(c.getColumnIndex(KEY_ENEMY_NAME)));
        history.setEnemyPlayerLevel(c.getInt(c.getColumnIndex(KEY_ENEMY_PLAYER_LEVEL)));
        history.setEnemyStrength(c.getInt(c.getColumnIndex(KEY_ENEMY_STRENGTH)));
        history.setEnemyMaxLevel(c.getInt(c.getColumnIndex(KEY_ENEMY_MAX_LEVEL)));
        history.setResult(c.getInt(c.getColumnIndex(KEY_RESULT)) > 0);
        history.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return history;
    }

    /*
    * SELECT * FROM history;
    */
    public List<DbHistory> getAllHistory() {
        List<DbHistory> historyEntries = new ArrayList<DbHistory>();
        String selectQuery = "SELECT  * FROM " + TABLE_HISTORY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                DbHistory history = new DbHistory();
                history.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                history.setOwnPlayerLevel(c.getInt(c.getColumnIndex(KEY_OWN_PLAYER_LEVEL)));
                history.setOwnStrength(c.getInt(c.getColumnIndex(KEY_OWN_STRENGTH)));
                history.setOwnMaxLevel(c.getInt(c.getColumnIndex(KEY_OWN_MAX_LEVEL)));
                history.setEnemyName(c.getString(c.getColumnIndex(KEY_ENEMY_NAME)));
                history.setEnemyPlayerLevel(c.getInt(c.getColumnIndex(KEY_ENEMY_PLAYER_LEVEL)));
                history.setEnemyStrength(c.getInt(c.getColumnIndex(KEY_ENEMY_STRENGTH)));
                history.setEnemyMaxLevel(c.getInt(c.getColumnIndex(KEY_ENEMY_MAX_LEVEL)));
                history.setResult(c.getInt(c.getColumnIndex(KEY_RESULT)) > 0);
                history.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to fight list
                historyEntries.add(history);
            } while (c.moveToNext());
        }

        return historyEntries;
    }


    /*
    * Updating a history
    */
    public int updateHistory(DbHistory history) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_OWN_PLAYER_LEVEL, history.getOwnPlayerLevel());
        values.put(KEY_OWN_STRENGTH, history.getOwnStrength());
        values.put(KEY_OWN_MAX_LEVEL, history.getOwnMaxLevel());
        values.put(KEY_ENEMY_NAME, history.getEnemyName());
        values.put(KEY_ENEMY_PLAYER_LEVEL, history.getEnemyPlayerLevel());
        values.put(KEY_ENEMY_STRENGTH, history.getEnemyStrength());
        values.put(KEY_ENEMY_MAX_LEVEL, history.getEnemyMaxLevel());
        values.put(KEY_RESULT, history.getResult());
        values.put(KEY_CREATED_AT, getDateTime());

        // updating row
        return db.update(TABLE_HISTORY, values, KEY_ID + " = ?",
                new String[] { String.valueOf(history.getId()) });
    }

    /*
    * Deleting a history entry
    */
    public void deleteHistory(long history_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORY, KEY_ID + " = ?",
                new String[] { String.valueOf(history_id) });
    }

    /*
     * Create Profile
     */
    public long createProfile(DbProfile profile) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERID, profile.getUserID());
        values.put(KEY_PLAYER_NAME, profile.getUserName());
        values.put(KEY_PLAYER_LEVEL,profile.getPlayerLevel());
        values.put(KEY_EP,profile.getEp());
        values.put(KEY_STRENGTH,profile.getArmyStrength());
        values.put(KEY_MAX_LEVEL, profile.getMaxSoldierLevel());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long profile_id = db.insert(TABLE_PROFILE, null, values);

        return profile_id;
    }

    /*
    * Updating a profile
    */
    public int updateProfile(DbProfile profile) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_NAME, profile.getUserName());
        values.put(KEY_PLAYER_LEVEL,profile.getPlayerLevel());
        values.put(KEY_EP,profile.getEp());
        values.put(KEY_STRENGTH,profile.getArmyStrength());
        values.put(KEY_MAX_LEVEL, profile.getMaxSoldierLevel());
        values.put(KEY_CREATED_AT, getDateTime());

        // updating row
        return db.update(TABLE_PROFILE, values, KEY_USERID + " = ?",
                new String[] { String.valueOf(profile.getUserID()) });
    }

    /*
    * SELECT * FROM profile LIMIT 1;
    */
    public DbProfile getProfile(int userID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PROFILE + " WHERE "
                + KEY_USERID + " = " + userID;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        DbProfile profile = new DbProfile();
        profile.setUserID((c.getInt(c.getColumnIndex(KEY_USERID))));
        profile.setUserName((c.getString(c.getColumnIndex(KEY_PLAYER_NAME))));
        profile.setPlayerLevel(c.getInt(c.getColumnIndex(KEY_PLAYER_LEVEL)));
        profile.setEp(c.getInt(c.getColumnIndex(KEY_EP)));
        profile.setArmyStrength(c.getInt(c.getColumnIndex(KEY_STRENGTH)));
        profile.setMaxSoldierLevel(c.getInt(c.getColumnIndex(KEY_MAX_LEVEL)));
        profile.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return profile;
    }

    /*
    * Deleting a profile
    */
    public void deleteProfile(int userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILE, KEY_USERID + " = ?",
                new String[] { String.valueOf(userID) });
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private Long getUnix() {
        long currentUnix = System.currentTimeMillis()/1000L;
        return currentUnix;
    }
}