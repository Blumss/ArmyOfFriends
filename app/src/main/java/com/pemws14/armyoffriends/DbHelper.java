package com.pemws14.armyoffriends;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // soldier Table - column names
    private static final String KEY_NAME = "name";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_RANK = "rank";


    // Table Create Statements
    // soldier table create statement
    private static final String CREATE_TABLE_SOLDIER = "CREATE TABLE "
            + TABLE_SOLDIER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME
            + " TEXT," + KEY_LEVEL + " INTEGER," + KEY_RANK + " INTEGER," + KEY_CREATED_AT
            + " DATETIME" + ")";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_SOLDIER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOLDIER);
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
    * SELECT * FROM soldier WHERE id = 1;
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
    * SELECT * FROM soldier;
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

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "HH:mm:ss dd.MM.yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}