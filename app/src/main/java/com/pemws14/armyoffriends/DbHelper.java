package com.pemws14.armyoffriends;

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

public class DbHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "AoFDatabase";

    // Table Names
    private static final String TABLE_SOLDIER = "soldier";
    private static final String TABLE_FIGHT = "fight";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_NAME = "name";

    // soldier Table - column names
    private static final String KEY_LEVEL = "level";
    private static final String KEY_RANK = "rank";

    // fight Table - column names
    private static final String KEY_STRENGTH = "strength";
    private static final String KEY_MAX_LEVEL = "maxLevel";


    // Table Create Statements
    // soldier table create statement
    private static final String CREATE_TABLE_SOLDIER = "CREATE TABLE "
            + TABLE_SOLDIER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME
            + " TEXT," + KEY_LEVEL + " INTEGER," + KEY_RANK + " INTEGER," + KEY_CREATED_AT
            + " DATETIME" + ")";

    // fight table create statement
    private static final String CREATE_TABLE_FIGHT = "CREATE TABLE "
            + TABLE_FIGHT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME
            + " TEXT," + KEY_STRENGTH + " INTEGER," + KEY_MAX_LEVEL + " INTEGER," + KEY_CREATED_AT
            + " DATETIME" + ")";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_SOLDIER);
        db.execSQL(CREATE_TABLE_FIGHT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOLDIER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIGHT);
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
    }

    /*
    * Creating a soldier
    */
    public long createSoldier(DbSoldier soldier) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, soldier.getName());
        values.put(KEY_LEVEL, soldier.getLevel());
        values.put(KEY_RANK, soldier.getRank());
        values.put(KEY_CREATED_AT, getDateTime());

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
        soldier.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        soldier.setLevel(c.getInt(c.getColumnIndex(KEY_LEVEL)));
        soldier.setRank(c.getInt(c.getColumnIndex(KEY_RANK)));
        soldier.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return soldier;
    }

    /*
    * SELECT MAX(level) FROM soldier;
    */
    public int getMaxLevel(int level) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT MAX(" + KEY_LEVEL + ") AS maxLevel FROM " + TABLE_SOLDIER ;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        return c.getInt(c.getColumnIndex("maxLevel"));
    }

    /*
    * SELECT * FROM soldier ORDER BY level DESC;
    * */
    public List<DbSoldier> getAllSoldiers() {
        List<DbSoldier> soldiers = new ArrayList<DbSoldier>();
        String selectQuery = "SELECT  * FROM " + TABLE_SOLDIER + " ORDER BY " + KEY_LEVEL + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                DbSoldier soldier = new DbSoldier();
                soldier.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                soldier.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                soldier.setLevel(c.getInt(c.getColumnIndex(KEY_LEVEL)));
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
        values.put(KEY_NAME, soldier.getName());
        values.put(KEY_LEVEL, soldier.getLevel());
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
        values.put(KEY_NAME, fight.getName());
        values.put(KEY_LEVEL,fight.getStrength());
        values.put(KEY_RANK, fight.getMaxLevel());
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
        fight.setName((c.getString(c.getColumnIndex(KEY_NAME))));
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
                fight.setName((c.getString(c.getColumnIndex(KEY_NAME))));
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
        values.put(KEY_NAME, fight.getName());
        values.put(KEY_LEVEL,fight.getStrength());
        values.put(KEY_RANK, fight.getMaxLevel());
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

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}