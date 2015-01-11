package com.pemws14.armyoffriends.database;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Ben on 10.01.2015.
 */
public class ParseDb {

    public ParseDb(){}

    ParseObject army = new ParseObject("ArmyyStrength"); // no typo, there are really two y in the name
    ParseQuery<ParseObject> query = ParseQuery.getQuery("ArmyyStrength");

    ParseUser CURRENT_USER;
    int MAX_LEVEL=0;
    int PLAYER_LEVEL=1;
    int ARMY_STRENGTH=0;

    int listSize;

/************ Get Methoden *************/

    public String getArmyID(){

        String objectID = army.getObjectId();
        return objectID;
    }
    public String getUserName(){
        String userName = army.getString("ref_username");
        return userName;
    }
    public String getUserID(){
        String UserID = army.getString("UserID");
        return UserID;
    }
    public ParseUser getCurrentParseUser(){
        CURRENT_USER = ParseUser.getCurrentUser();
        return CURRENT_USER;
    }
    public int getArmyStrength(){
        int armyStrength = army.getInt("army_strength");
        return armyStrength;
    }
    public int getMaxLevel(){
        int maxLevel = army.getInt("maxLevel");
        return maxLevel;
    }
    public int getPlayerLevel(){
        int playerLevel = army.getInt("player_Level");
        return playerLevel;
    }
    public String getCreateAt(){
        String createdAt = (army.getCreatedAt()).toString();
        return createdAt;
    }
    public String getUpdateAt(){
        String updatedAt = (army.getUpdatedAt()).toString();
        return updatedAt;
    }

/************ Set Methoden *************/

    public void setMaxLevel(int maxLevel){
        army.put("maxLevel",maxLevel);
        army.saveInBackground();
    }
    public void setArmyStrength(int armyStrength){
        army.put("army_strength",armyStrength);
        army.saveInBackground();
    }
    public void setPlayerLevel(int playerLevel){
        army.put("army_strength",playerLevel);
        army.saveInBackground();
    }

/************ Methoden *************/

    public void refreshParseDbArmy(){
        System.out.println("refreshParseDbArmy");
        army.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    // Success!
                    System.out.println("refreshParseDbArmy - Success!");
                } else {
                    // Failure!
                    System.out.println("refreshParseDbArmy - Failure!");
                }
            }
        });
    }
    public void createArmy(ParseUser currentUser,int armyStrength, int maxLevel, int playerLevel){
        System.out.println("createArmy");
        this.ARMY_STRENGTH = armyStrength;
        this.MAX_LEVEL = maxLevel;
        this.PLAYER_LEVEL = playerLevel;

        army.put("UserID", currentUser);                        // UserID
        army.put("refkey_ID", currentUser);                     // refkey_ID
        army.put("ref_username", currentUser.getUsername());    // ref_username
        army.put("army_strength", armyStrength);                // army_strength
        army.put("maxLevel", maxLevel);                         // maxLevel
        army.put("player_level", playerLevel);                  //  Player Level
        army.saveInBackground();
    }
    public void updateArmy(int armyStrength, int maxLevel, int playerLevel){
        System.out.println("updateArmy");
        this.ARMY_STRENGTH = armyStrength;
        this.MAX_LEVEL = maxLevel;
        this.PLAYER_LEVEL = playerLevel;

        query.getInBackground(getArmyID(),new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    // object will be your game score
                    System.out.println("updateArmy - Success!");

                    parseObject.put("army_strength", ARMY_STRENGTH);                // army_strength
                    parseObject.put("maxLevel", MAX_LEVEL);                         // maxLevel
                    parseObject.put("player_level", PLAYER_LEVEL);                  //  Player Level
                    parseObject.saveInBackground();
                } else {
                    // something went wrong
                    System.out.println("updateArmy - Exception: " + e.getMessage());
                }
            }

        });
    }
    public void newArmy(int armyStrength, int maxLevel, int playerLevel){
        System.out.println(" newArmy()");
        this.ARMY_STRENGTH = armyStrength;
        this.MAX_LEVEL = maxLevel;
        this.PLAYER_LEVEL = playerLevel;
        query.whereEqualTo("ref_username",getCurrentParseUser().getUsername());
        System.out.println("User: "+getCurrentParseUser().getUsername());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + parseObjects.size() + " scores");
                    listSize = parseObjects.size();
                    if(listSize ==1){
                        updateArmy(ARMY_STRENGTH,MAX_LEVEL,PLAYER_LEVEL);
                    }
                    if(listSize ==1){
                        createArmy(CURRENT_USER,ARMY_STRENGTH,MAX_LEVEL,PLAYER_LEVEL);
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

    }
}
