package com.pemws14.armyoffriends.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pemws14.armyoffriends.GameMechanics;

import java.io.ByteArrayOutputStream;
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
    Bitmap bmp;

    int listSize;
    public DbFight dbFight;
    public GameMechanics gameMechanics;

/************ Get Methoden *************/


    public String getCurrentUserName(){
        String userName = CURRENT_USER.getUsername();
        return userName;
    }
    public String getUserID(){

        String UserID = CURRENT_USER.getObjectId();
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
    public int getArmyStrength(ParseUser user){
        int armyStrength = user.getInt("army_strength");
        return armyStrength;
    }
    public int getMaxLevel(){
        int maxLevel = CURRENT_USER.getInt("maxLevel");
        return maxLevel;
    }
    public int getMaxLevel(ParseUser user){
        int maxLevel = user.getInt("maxLevel");
        return maxLevel;
    }
    public int getPlayerLevel(){
        int playerLevel = CURRENT_USER.getInt("player_level");
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
    public void getImage(final DbHelper dbHelper, final DbProfile dbProfile){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        Log.d("getImage","getImage - ParseQuery: " + (query!=null));
        Log.d("getImage", "User-ID: "+CURRENT_USER.getObjectId());
        query.getInBackground(CURRENT_USER.getObjectId(), new GetCallback() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("getImage", "The object was not found...");
                } else {
                    Log.d("getImage", "Retrieved the object.");
                    ParseFile fileObject = (ParseFile) object.get("ImageFile");
                    fileObject.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                Log.d("getImage", "We've got data in data.");
                                // data has the bytes for the image
                                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                dbProfile.setImg(bmp);
                                dbHelper.updateProfile(dbProfile);
                            } else {
                                Log.d("getImage", "There was a problem downloading the data.");
                            }
                        }
                    });
                }
            }
        });
    }

    public void getFightImage(ParseUser mParseUser, final DbHelper dbHelper, final DbSoldier dbSoldier, final DbFight mDbFight){
        ParseQuery<ParseUser> query = mParseUser.getQuery();
        Log.d("getFightImage", "User-ID: "+ mParseUser.getObjectId() + " UserName: "+ mParseUser.getUsername());
        query.getInBackground(mParseUser.getObjectId(), new GetCallback() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("getFightImage", "The object was not found...");
                } else {
                    Log.d("getFightImage", "Retrieved the object.");
                    ParseFile fileObject = (ParseFile) object.get("ImageFile");
                    fileObject.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                Log.d("getFightImage", "We've got data in data.");
                                // data has the bytes for the image
                                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);

                                dbSoldier.setImg(bmp);
                                dbHelper.updateSoldier(dbSoldier);
                                mDbFight.setImg(bmp);
                                dbHelper.updateFight(mDbFight);

                            } else {
                                Log.d("getFightImage", "There was a problem downloading the data.");
                            }
                        }
                    });
                }
            }
        });
    }


    public boolean existImage(){
        ParseFile imageFile = (ParseFile)CURRENT_USER.get("ImageFile");
        if(imageFile!=null){
            Log.d("existImage","true");
            return true;
        }else  {
            Log.d("existImage","false");
            return false;
        }
    }

    public boolean existFightImage(ParseUser parseUser){
        ParseFile imageFile = (ParseFile)parseUser.get("ImageFile");
        if(imageFile!=null){
            Log.d("existFightImage","true");
            return true;
        }else  {
            Log.d("existFightImage","false");
            return false;
        }
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
       // ParseGeoPoint currentLocation = CURRENT_USER.getParseGeoPoint("location");

        UserLocation = new ParseGeoPoint(location.getLatitude(),location.getLongitude());
     //   UserLocList = new ArrayList<ParseGeoPoint>();
      //  UserLocList.add(UserLocation);
        Log.d("setCurrentLocation","CURRENT_USER: "+CURRENT_USER.getUsername());
        CURRENT_USER.put("location", UserLocation);
      //  CURRENT_USER.add("locationList", UserLocList);

        CURRENT_USER.saveInBackground();
    }

/************ Methoden *************/

    public void addPeople(){

    }

    public void deleteMetPeople(){
        List<ParseUser> parseUsers = getMetPeopleToday();
        if(parseUsers!=null){
            CURRENT_USER.removeAll("metPeopleToday",parseUsers);
            CURRENT_USER.saveInBackground();
        }

    }

    public void deleteLocation(){
        ParseGeoPoint currentLocation = CURRENT_USER.getParseGeoPoint("location");
        if(currentLocation!=null){
            CURRENT_USER.remove("location");
        }
    }

    public void refreshParseUser(){
        CURRENT_USER.fetchInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    // Success!
                    Log.d("refreshParseUser","refreshParseDbArmy - Success!");
                } else {
                    // Failure!
                    Log.d("refreshParseUser","refreshParseDbArmy - Failure!");
                }
            }
        });
    }

    public void updateArmy(int armyStrength, int maxLevel, int playerLevel, int ep){
        Log.d("updateArmy","newArmy");

        CURRENT_USER.put("army_strength", armyStrength);    // army_strength
        CURRENT_USER.put("maxLevel", maxLevel);             // maxLevel
        CURRENT_USER.put("player_level", playerLevel);      //  Player Level
        CURRENT_USER.put("ep",ep);                          // EP
        CURRENT_USER.saveInBackground();
    }

    public void saveImageInParse(Bitmap bitmap){

        // Locate the image in res > drawable-hdpi
      //  Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.androidbegin);

        // Convert it to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.PNG, 1, stream);
        byte[] image = stream.toByteArray();

        // Create the ParseFile
        ParseFile file = new ParseFile(CURRENT_USER.getUsername()+".png", image);
        // Upload the image into Parse Cloud
        file.saveInBackground();

        // Create a New Class called "ImageUpload" in Parse
      //  ParseObject imgupload = new ParseObject("ImageUpload");

        // Create a column named "ImageName" and set the string
     //   CURRENT_USER.put("ImageName", "AndroidBegin Logo");

        // Create a column named "ImageFile" and insert the image
        CURRENT_USER.put("ImageFile", file);

        // Create the class and the columns
        CURRENT_USER.saveInBackground();


    }
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
