package com.pemws14.armyoffriends.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.pemws14.armyoffriends.MainActivity;

import com.pemws14.armyoffriends.GameMechanics;

import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DbHelper extends SQLiteOpenHelper {
    private static DbHelper sInstance;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "AoFDatabase";

    // Table Names
    private static final String TABLE_SOLDIER = "soldier";
    private static final String TABLE_FIGHT = "fight";
    private static final String TABLE_HISTORY = "history";
    private static final String TABLE_PROFILE = "profile";
    private static final String TABLE_ACHIEVEMENT = "achievement";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_CREATED_AT_UNIX = "created_at_unix";
    private static final String KEY_PLAYER_NAME = "name";
    private static final String KEY_PICTURE = "picture";
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
    private static final String KEY_SERVERID = "serverID";
    private static final String KEY_EP = "ep";

    //achievement Table - column names
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_REQUIRED = "required";
    private static final String KEY_ACHIEVED = "achieved";
    private static final String KEY_FULFILLED = "fulfilled";


    // Table Create Statements
    // soldier table create statement
    private static final String CREATE_TABLE_SOLDIER = "CREATE TABLE "
            + TABLE_SOLDIER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PLAYER_NAME
            + " TEXT," + KEY_PICTURE + " BLOB," + KEY_SOLDIER_LEVEL + " INTEGER," + KEY_RANK + " INTEGER," + KEY_CREATED_AT
            + " DATETIME," + KEY_CREATED_AT_UNIX + " LONG" + ")";

    // fight table create statement
    private static final String CREATE_TABLE_FIGHT = "CREATE TABLE "
            + TABLE_FIGHT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PLAYER_NAME
            + " TEXT," + KEY_PICTURE + " BLOB," + KEY_PLAYER_LEVEL + " INTEGER," + KEY_STRENGTH + " INTEGER," + KEY_MAX_LEVEL + " INTEGER," + KEY_CREATED_AT
            + " DATETIME," + KEY_CREATED_AT_UNIX + " LONG" + ")";

    //history table create statement
    private static final String CREATE_TABLE_HISTORY = "CREATE TABLE "
            + TABLE_HISTORY + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_OWN_PLAYER_LEVEL + " INTEGER,"
            + KEY_OWN_STRENGTH + " INTEGER," + KEY_OWN_MAX_LEVEL + " INTEGER,"  + KEY_ENEMY_NAME + " TEXT," + KEY_PICTURE + " BLOB,"
            + KEY_ENEMY_PLAYER_LEVEL + " INTEGER,"+ KEY_ENEMY_STRENGTH + " INTEGER," + KEY_ENEMY_MAX_LEVEL + " INTEGER,"
            + KEY_RESULT + " BOOLEAN," + KEY_CREATED_AT + " DATETIME," + KEY_CREATED_AT_UNIX + " LONG" + ")";

    //profile table create statement
    private static final String CREATE_TABLE_PROFILE = "CREATE TABLE "
            + TABLE_PROFILE + "(" + KEY_SERVERID + " TEXT PRIMARY KEY," + KEY_PLAYER_NAME + " TEXT," + KEY_PICTURE + " BLOB,"
            + KEY_PLAYER_LEVEL + " INTEGER," + KEY_EP + " INTEGER," +  KEY_STRENGTH + " INTEGER," + KEY_MAX_LEVEL + " INTEGER," + KEY_CREATED_AT
            + " DATETIME" + ")";

    // achievements table create statement
    private static final String CREATE_TABLE_ACHIEVEMENT = "CREATE TABLE "
            + TABLE_ACHIEVEMENT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE
            + " TEXT,"  + KEY_DESCRIPTION + " TEXT," + KEY_REQUIRED + " INTEGER," + KEY_ACHIEVED + " INTEGER," + KEY_FULFILLED + " INTEGER," + KEY_CREATED_AT
            + " DATETIME," + KEY_CREATED_AT_UNIX + " LONG" + ")";


    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DbHelper getInstance(Context context){
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null){
            sInstance = new DbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_SOLDIER);
        db.execSQL(CREATE_TABLE_FIGHT);
        db.execSQL(CREATE_TABLE_HISTORY);
        db.execSQL(CREATE_TABLE_PROFILE);
        db.execSQL(CREATE_TABLE_ACHIEVEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOLDIER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIGHT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACHIEVEMENT);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACHIEVEMENT);
    }

    /*
    * Creating a soldier
    */
    public long createSoldier(DbSoldier soldier) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_NAME, soldier.getName());
        values.put(KEY_PICTURE, bitmapToBArray(soldier.getImg()));
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
        byte[] byteArray = c.getBlob(c.getColumnIndex(KEY_PICTURE));
        soldier.setImg(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
        soldier.setLevel(c.getInt(c.getColumnIndex(KEY_SOLDIER_LEVEL)));
        soldier.setRank(c.getInt(c.getColumnIndex(KEY_RANK)));
        soldier.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        soldier.setCreated_at_Unix(c.getLong(c.getColumnIndex(KEY_CREATED_AT_UNIX)));

        return soldier;
    }

    /*
    * SELECT * FROM soldier WHERE name = soldierName;
    */
    public DbSoldier getSoldier(String soldierName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_SOLDIER + " WHERE "
                + KEY_PLAYER_NAME + " = '" + soldierName + "'";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        DbSoldier soldier = new DbSoldier();
        soldier.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        soldier.setName((c.getString(c.getColumnIndex(KEY_PLAYER_NAME))));
        byte[] byteArray = c.getBlob(c.getColumnIndex(KEY_PICTURE));
        soldier.setImg(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
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
                byte[] byteArray = c.getBlob(c.getColumnIndex(KEY_PICTURE));
                soldier.setImg(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
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
* SELECT * FROM soldier ORDER BY level DESC LIMIT maxArmySize;
* */
    public List<DbSoldier> getLimitedSoldiers(int maxArmySize) {
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
                byte[] byteArray = c.getBlob(c.getColumnIndex(KEY_PICTURE));
                soldier.setImg(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
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
                byte[] byteArray = c.getBlob(c.getColumnIndex(KEY_PICTURE));
                soldier.setImg(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
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
    * Updating a soldier
    */
    public int updateSoldier(DbSoldier soldier) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_NAME, soldier.getName());
        values.put(KEY_SOLDIER_LEVEL, soldier.getLevel());
        values.put(KEY_PICTURE, bitmapToBArray(soldier.getImg()));
        values.put(KEY_RANK, soldier.getRank());
        values.put(KEY_CREATED_AT, soldier.getCreated_at());
        values.put(KEY_CREATED_AT_UNIX, soldier.getCreated_at_Unix());

        // updating row
        return db.update(TABLE_SOLDIER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(soldier.getId()) });
    }

    /*
    * Updating a soldier
    */
    public int levelUpSoldier(DbSoldier soldier) {
        SQLiteDatabase db = this.getWritableDatabase();
        int increasedLevel = soldier.getLevel() + 1;

        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_NAME, soldier.getName());
        values.put(KEY_SOLDIER_LEVEL, increasedLevel);
        values.put(KEY_PICTURE, bitmapToBArray(soldier.getImg()));
        values.put(KEY_RANK, GameMechanics.getRankByLevel(increasedLevel));
        values.put(KEY_CREATED_AT, soldier.getCreated_at());
        values.put(KEY_CREATED_AT_UNIX, soldier.getCreated_at_Unix());

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
        values.put(KEY_PICTURE, bitmapToBArray(fight.getImg()));
        values.put(KEY_PLAYER_LEVEL,fight.getPlayerLevel());
        values.put(KEY_STRENGTH,fight.getStrength());
        values.put(KEY_MAX_LEVEL, fight.getMaxLevel());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_CREATED_AT_UNIX, getUnix());

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
        byte[] byteArray = c.getBlob(c.getColumnIndex(KEY_PICTURE));
        fight.setImg(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
        fight.setPlayerLevel(c.getInt(c.getColumnIndex(KEY_PLAYER_LEVEL)));
        fight.setStrength(c.getInt(c.getColumnIndex(KEY_STRENGTH)));
        fight.setMaxLevel(c.getInt(c.getColumnIndex(KEY_MAX_LEVEL)));
        fight.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        fight.setCreated_at_Unix(c.getLong(c.getColumnIndex(KEY_CREATED_AT_UNIX)));

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
                byte[] byteArray = c.getBlob(c.getColumnIndex(KEY_PICTURE));
                fight.setImg(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                fight.setPlayerLevel(c.getInt(c.getColumnIndex(KEY_PLAYER_LEVEL)));
                fight.setStrength(c.getInt(c.getColumnIndex(KEY_STRENGTH)));
                fight.setMaxLevel(c.getInt(c.getColumnIndex(KEY_MAX_LEVEL)));
                fight.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                fight.setCreated_at_Unix(c.getLong(c.getColumnIndex(KEY_CREATED_AT_UNIX)));

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
        values.put(KEY_PICTURE, bitmapToBArray(fight.getImg()));
        values.put(KEY_PLAYER_LEVEL,fight.getPlayerLevel());
        values.put(KEY_STRENGTH,fight.getStrength());
        values.put(KEY_MAX_LEVEL, fight.getMaxLevel());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_CREATED_AT_UNIX, getUnix());

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
        values.put(KEY_PICTURE, bitmapToBArray(history.getImg()));
        values.put(KEY_ENEMY_PLAYER_LEVEL, history.getEnemyPlayerLevel());
        values.put(KEY_ENEMY_STRENGTH, history.getEnemyStrength());
        values.put(KEY_ENEMY_MAX_LEVEL, history.getEnemyMaxLevel());
        values.put(KEY_RESULT, history.getResult());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_CREATED_AT_UNIX, getUnix());

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
        byte[] byteArray = c.getBlob(c.getColumnIndex(KEY_PICTURE));
        history.setImg(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
        history.setEnemyPlayerLevel(c.getInt(c.getColumnIndex(KEY_ENEMY_PLAYER_LEVEL)));
        history.setEnemyStrength(c.getInt(c.getColumnIndex(KEY_ENEMY_STRENGTH)));
        history.setEnemyMaxLevel(c.getInt(c.getColumnIndex(KEY_ENEMY_MAX_LEVEL)));
        history.setResult(c.getInt(c.getColumnIndex(KEY_RESULT)) > 0);
        history.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        history.setCreated_at_Unix(c.getLong(c.getColumnIndex(KEY_CREATED_AT_UNIX)));

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
                byte[] byteArray = c.getBlob(c.getColumnIndex(KEY_PICTURE));
                history.setImg(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                history.setEnemyPlayerLevel(c.getInt(c.getColumnIndex(KEY_ENEMY_PLAYER_LEVEL)));
                history.setEnemyStrength(c.getInt(c.getColumnIndex(KEY_ENEMY_STRENGTH)));
                history.setEnemyMaxLevel(c.getInt(c.getColumnIndex(KEY_ENEMY_MAX_LEVEL)));
                history.setResult(c.getInt(c.getColumnIndex(KEY_RESULT)) > 0);
                history.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                history.setCreated_at_Unix(c.getLong(c.getColumnIndex(KEY_CREATED_AT_UNIX)));

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
        values.put(KEY_PICTURE, bitmapToBArray(history.getImg()));
        values.put(KEY_ENEMY_PLAYER_LEVEL, history.getEnemyPlayerLevel());
        values.put(KEY_ENEMY_STRENGTH, history.getEnemyStrength());
        values.put(KEY_ENEMY_MAX_LEVEL, history.getEnemyMaxLevel());
        values.put(KEY_RESULT, history.getResult());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_CREATED_AT_UNIX, getUnix());

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
        values.put(KEY_SERVERID, profile.getServerID());
        values.put(KEY_PLAYER_NAME, profile.getUserName());
        values.put(KEY_PICTURE, bitmapToBArray(profile.getImg()));
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
        values.put(KEY_PICTURE, bitmapToBArray(profile.getImg()));
        values.put(KEY_PLAYER_LEVEL,profile.getPlayerLevel());
        values.put(KEY_EP,profile.getEp());
        values.put(KEY_STRENGTH,profile.getArmyStrength());
        values.put(KEY_MAX_LEVEL, profile.getMaxSoldierLevel());
        values.put(KEY_CREATED_AT, getDateTime());
        System.out.println("DBHelper: Updated! CurEPs: " + profile.getEp());
        // updating row
        return db.update(TABLE_PROFILE, values, KEY_SERVERID + " = ?",
                new String[] { profile.getServerID() });
    }

    /*
    * SELECT * FROM profile LIMIT 1;
    */
    public DbProfile getProfile(String userID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PROFILE + " WHERE "
                + KEY_SERVERID + " = '" + userID+"'";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        DbProfile profile = new DbProfile();
        profile.setServerID((c.getString(c.getColumnIndex(KEY_SERVERID))));
        profile.setUserName((c.getString(c.getColumnIndex(KEY_PLAYER_NAME))));
        byte[] byteArray = c.getBlob(c.getColumnIndex(KEY_PICTURE));
        profile.setImg(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
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
        db.delete(TABLE_PROFILE, KEY_SERVERID + " = ?",
                new String[] { String.valueOf(userID) });
    }

    /*
    * SELECT * FROM profile;
    */
    public List<DbProfile> getAllProfiles() {
        List<DbProfile> profileEntries = new ArrayList<DbProfile>();
        String selectQuery = "SELECT  * FROM " + TABLE_PROFILE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                DbProfile profile = new DbProfile();
                profile.setServerID(c.getString(c.getColumnIndex(KEY_SERVERID)));
                profile.setUserName(c.getString(c.getColumnIndex(KEY_PLAYER_NAME)));
                byte[] byteArray = c.getBlob(c.getColumnIndex(KEY_PICTURE));
                profile.setImg(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                profile.setPlayerLevel(c.getInt(c.getColumnIndex(KEY_PLAYER_LEVEL)));
                profile.setEp(c.getInt(c.getColumnIndex(KEY_EP)));
                profile.setArmyStrength(c.getInt(c.getColumnIndex(KEY_STRENGTH)));
                profile.setMaxSoldierLevel(c.getInt(c.getColumnIndex(KEY_MAX_LEVEL)));
                profile.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to fight list
                profileEntries.add(profile);
            } while (c.moveToNext());
        }

        return profileEntries;
    }

    /*
    * Creating an Achievement Entry
    */
    public long createAchievement(DbAchievement achievement) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, achievement.getTitle());
        values.put(KEY_DESCRIPTION, achievement.getDescription());
        values.put(KEY_REQUIRED, achievement.getRequired());
        values.put(KEY_ACHIEVED, achievement.getAchieved());
        values.put(KEY_FULFILLED, achievement.getFulfilled());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_CREATED_AT_UNIX, getUnix());

        // insert row
        long achievement_id = db.insert(TABLE_ACHIEVEMENT, null, values);

        return achievement_id;
    }

    /*
    * SELECT * FROM achievements WHERE id = i;
    */
    public DbAchievement getAchievement(long achievement_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_ACHIEVEMENT + " WHERE "
                + KEY_ID + " = " + achievement_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        DbAchievement achievement = new DbAchievement();
        achievement.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        achievement.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
        achievement.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
        achievement.setRequired(c.getInt(c.getColumnIndex(KEY_REQUIRED)));
        achievement.setAchieved(c.getInt(c.getColumnIndex(KEY_ACHIEVED)));
        achievement.setFulfilled(c.getInt(c.getColumnIndex(KEY_FULFILLED)));
        achievement.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        achievement.setCreated_at_Unix(c.getLong(c.getColumnIndex(KEY_CREATED_AT_UNIX)));

        return achievement;
    }

    /*
    * SELECT * FROM achievements;
    */
    public List<DbAchievement> getAllAchievements() {
        List<DbAchievement> achievementEntries = new ArrayList<DbAchievement>();
        String selectQuery = "SELECT  * FROM " + TABLE_ACHIEVEMENT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                DbAchievement achievement = new DbAchievement();
                achievement.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                achievement.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                achievement.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                achievement.setRequired(c.getInt(c.getColumnIndex(KEY_REQUIRED)));
                achievement.setAchieved(c.getInt(c.getColumnIndex(KEY_ACHIEVED)));
                achievement.setFulfilled(c.getInt(c.getColumnIndex(KEY_FULFILLED)));
                achievement.setCreated_at_Unix(c.getLong(c.getColumnIndex(KEY_CREATED_AT_UNIX)));

                // adding to fight list
                achievementEntries.add(achievement);
            } while (c.moveToNext());
        }

        return achievementEntries;
    }


    /*
    * Updating an achievement
    */
    public int updateAchievement(DbAchievement achievement) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, achievement.getTitle());
        values.put(KEY_DESCRIPTION, achievement.getDescription());
        values.put(KEY_REQUIRED, achievement.getRequired());
        values.put(KEY_ACHIEVED, achievement.getAchieved());
        values.put(KEY_FULFILLED, achievement.getFulfilled());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_CREATED_AT_UNIX, getUnix());

        // updating row
        return db.update(TABLE_ACHIEVEMENT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(achievement.getId()) });
    }

    /*
    * Deleting an achievement entry
    */
    public void deleteAchievement(long achievement_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACHIEVEMENT, KEY_ID + " = ?",
                new String[] { String.valueOf(achievement_id) });
    }

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public Long getUnix() {
        long currentUnix = System.currentTimeMillis()/1000L;
        return currentUnix;
    }

    public static byte[] bitmapToBArray(Bitmap bmp){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        return bArray;
    }

    /*
    * check if Achievement was unlocked
     */
    public int checkAchievementState(DbAchievement achievement){
        Log.i("DbHelper.checkAchievementState","checking achievement, called from "+achievement.getTitle());
        if (achievement.getRequired()<=achievement.getAchieved()){
            if(achievement.getFulfilled()!=1){
                Toast toast = Toast.makeText(MainActivity.getAppContext(),"Achievement unlocked:\n" + achievement.getTitle(), Toast.LENGTH_LONG);
                toast.show();
            }
            return 1;
        }else{
            return 0;
        }
    }
}