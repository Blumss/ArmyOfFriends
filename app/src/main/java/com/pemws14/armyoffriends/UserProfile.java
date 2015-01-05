package com.pemws14.armyoffriends;

/**
 * Created by Ben on 05.01.2015.
 */
public class UserProfile {
    public UserProfile(){}
    public UserProfile(String userName, String userID, int armyStrength, int maxLevel){
        this.USER_NAME=userName;
        this.USER_ID=userID;
        this.ARMY_STRENGTH=armyStrength;
        this.MAX_LEVEL=maxLevel;
    }

    public String USER_NAME;
    public String USER_ID;
    public int ARMY_STRENGTH;
    public int MAX_LEVEL;

    public String getUserName(){ return USER_NAME; }
    public String getUserID(){ return USER_ID; }
    public int getArmyStrength(){ return ARMY_STRENGTH; }
    public int getMaxLevel(){ return MAX_LEVEL; }

    public void setUserName(String UserName){USER_NAME = UserName;};
    public void setUserID(String UserID){USER_ID = UserID;};
    public void setArmyStrength(int ArmyStrength){ARMY_STRENGTH = ArmyStrength;};
    public void setMaxLevel(int MaxLevel){MAX_LEVEL = MaxLevel;};


}
