package com.pemws14.armyoffriends.database;

import android.location.Location;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 10.01.2015.
 */
public class ParseDb {

    public ParseDb(){this.CURRENT_USER = ParseUser.getCurrentUser();}


   // ParseObject army = new ParseObject("ArmyyStrength"); // no typo, there are really two y in the name
    public ParseQuery<ParseObject> query = ParseQuery.getQuery("ArmyyStrength");
    public ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
    public ParseGeoPoint UserLocation;
    public ArrayList<ParseGeoPoint> UserLocList;
    public ParseUser CURRENT_USER = ParseUser.getCurrentUser();
    int MAX_LEVEL=0;
    int PLAYER_LEVEL=1;
    int ARMY_STRENGTH=0;

    int listSize;

/************ Get Methoden *************/


    public String getCurrentUserName(){
        String userName = CURRENT_USER.getUsername();
        return userName;
    }
    public String getUserID(){
        String UserID = CURRENT_USER.getString("objectId");
        return UserID;
    }
    public ParseUser getCurrentParseUser(){
        CURRENT_USER = ParseUser.getCurrentUser();
        return CURRENT_USER;
    }
    public int getArmyStrength(){
        int armyStrength = CURRENT_USER.getInt("army_strength");
        return armyStrength;
    }
    public int getMaxLevel(){
        int maxLevel = CURRENT_USER.getInt("maxLevel");
        return maxLevel;
    }
    public int getPlayerLevel(){
        int playerLevel = CURRENT_USER.getInt("player_Level");
        return playerLevel;
    }
    public List<ParseUser> getMetPeopleToday(){
        List<ParseUser> list = CURRENT_USER.getList("metPeopleToday");
        return list;
    }
    public ParseGeoPoint getCurrentLocation(){
        ParseGeoPoint currentLocation = CURRENT_USER.getParseGeoPoint("location");
        return currentLocation;
    }
    public String getCreateAt(){
        String createdAt = (CURRENT_USER.getCreatedAt()).toString();
        return createdAt;
    }
    public String getUpdateAt(){
        String updatedAt = (CURRENT_USER.getUpdatedAt()).toString();
        return updatedAt;
    }
    public int getEP(){
        int ep = CURRENT_USER.getInt("ep");
        return ep;
    }

/************ Set Methoden *************/

    public void setMaxLevel(int maxLevel){
        CURRENT_USER.put("maxLevel",maxLevel);
        CURRENT_USER.saveInBackground();
    }
    public void setArmyStrength(int armyStrength){
        CURRENT_USER.put("army_strength",armyStrength);
        CURRENT_USER.saveInBackground();
    }
    public void setPlayerLevel(int playerLevel){
        CURRENT_USER.put("player_level",playerLevel);
        CURRENT_USER.saveInBackground();
    }
    public void setEP(int ep){
        CURRENT_USER.put("ep",ep);
        CURRENT_USER.saveInBackground();
    }
    public void setCurrentLocation(Location location){
        ParseGeoPoint currentLocation = CURRENT_USER.getParseGeoPoint("location");

        UserLocation = new ParseGeoPoint(location.getLatitude(),location.getLongitude());
        UserLocList = new ArrayList<ParseGeoPoint>();
        UserLocList.add(UserLocation);

        CURRENT_USER.put("location", UserLocation);
        CURRENT_USER.add("locationList", UserLocList);

        CURRENT_USER.saveInBackground();
    }

/************ Methoden *************/

    public void refreshParseUser(){
        System.out.println("refreshParseDbArmy");
        CURRENT_USER.fetchInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
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
    /*
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
    }*/
    /*
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
    }*/
    /*
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

    }*/
}
