package com.pemws14.armyoffriends.background;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.location.LocationClient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pemws14.armyoffriends.fight.FightActivity;
import com.pemws14.armyoffriends.GameMechanics;
import com.pemws14.armyoffriends.MainActivity;
import com.pemws14.armyoffriends.ProfileActivity;
import com.pemws14.armyoffriends.R;
import com.pemws14.armyoffriends.UserProfile;
import com.pemws14.armyoffriends.army.ArmyActivity;
import com.pemws14.armyoffriends.database.DbHelper;
import com.pemws14.armyoffriends.database.DbSoldier;
import com.pemws14.armyoffriends.database.ParseDb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ben on 25.11.2014.
 */
public class BackgroundService extends Service {

    String longText = "You met someone!";
    String shortText = "You met someone!";
    String notiTitle = "Army of Friends";
    String fightAction = "Fight!";
    String armyAction = "Your Army";
    String yourProfile = "Your Profile";

    DbHelper dbHelper;
    GameMechanics gameMechanics;
    UserProfile userProfile;
    List<DbSoldier> dbSoldiers;
    ParseDb parseDb;



    Calendar cur_cal = Calendar.getInstance();
    public static int counter = 0; // zählt wie oft der Service schon gestartet wurde
    public static final String LOCATION = "location";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String BROADCAST_ACTION = "com.pemws14.armyoffriends";
    public static final String USER_COUNT = "usercount";


    public ParseObject TestUser;
    public ParseGeoPoint UserLocation, TestUserLocation;
    public ArrayList<ParseGeoPoint> UserLocList;
    public int numberUsers;
    public  ParseUser currentUser;

    public ParseQuery<ParseObject> armyStrengthQuery;

    // save armyStuff
    public ParseObject ArmyStrength ;
    int armyStrength;
    int defaultPlayerLevel = 1;
    int defaultEP = 0;
    boolean armyExist;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        currentUser = ParseUser.getCurrentUser();

        System.out.println("BackgroundService - onStartCommand");
        counter++;
        Toast.makeText(this, " First Service Started" + "  " + counter, Toast.LENGTH_SHORT).show();

        Location location = intent.getParcelableExtra(LocationClient.KEY_LOCATION_CHANGED);
        if(location !=null){
            System.out.println("BackgroundService - onHandleIntent - Location available");
            System.out.println("BackgroundService - onHandleIntent - Location available: "+location);
            System.out.println("BackgroundService - onHandleIntent - Location available - Latitude: "+location.getLatitude());
            System.out.println("BackgroundService - onHandleIntent - Location available - Longitude: "+location.getLongitude());
            Toast.makeText(this, "new Location!", Toast.LENGTH_SHORT).show();

            parseDb.setCurrentLocation(location);
            getNearbyUsers(parseDb.getCurrentLocation(), location);
            saveArmyStuff();
           // sendLocResult(location);
        }

        //TODO do something useful
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("BackgroundService - IBinder");
        //TODO for communication return IBinder implementation
        return null;
    }

    public void onCreate() {

        userProfile = new UserProfile();
        dbHelper = new DbHelper(getApplicationContext());
        gameMechanics = new GameMechanics();
        parseDb = new ParseDb();

        // Parse Test
      //  User = new ParseObject("TestObject");
        currentUser = ParseUser.getCurrentUser();
        ArmyStrength = new ParseObject("ArmyyStrength");

   //     currentUser.put("location",TestUserLocation);

      //  TestUser = new ParseObject("TestObject");
     //   TestUserLocation = new ParseGeoPoint(48.1529363,11.5681098);
      //  TestUser.put("location",TestUserLocation);

      //  User.saveInBackground();
      //  TestUser.saveInBackground();

        System.out.println("BackgroundService - onCreate");
        Toast.makeText(this, "First Service was Created", Toast.LENGTH_SHORT).show();
        // TODO Auto-generated method stub
        super.onCreate();
        //
        /*
        Intent intent = new Intent(this, BackgroundService.class);
        PendingIntent pintent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        cur_cal.setTimeInMillis(System.currentTimeMillis());
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cur_cal.getTimeInMillis(), 60 * 1000*1, pintent); // repeat alle 1 Minute
        */
    }
//    @Override
//    public void onStart(Intent intent, int startId) {
//        counter++;
//        Toast.makeText(this, " First Service Started" + "  " + counter, Toast.LENGTH_SHORT).show();
//        System.out.println("BackgroundService - onStart");
//        // TODO Auto-generated method stub
//        super.onStart(intent, startId);
//        // your code for background process
//    }
    @Override
    public void onDestroy() {
        System.out.println("BackgroundService - onDestroy");
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    }
    public void onTaskRemoved (Intent rootIntent){

        MainActivity.alarm.cancel(MainActivity.pintent);
        this.stopSelf();
    }

    public void sendLocResult(Location location, int UserNum){
        Intent intent = new Intent("com.pemws14.armyoffriends");
        intent.putExtra(LOCATION, location);
        intent.putExtra(LATITUDE, location.getLatitude());
        intent.putExtra(LONGITUDE, location.getLongitude());
        intent.putExtra(USER_COUNT, UserNum);
        sendBroadcast(intent);
    }

    public void getNearbyUsers(ParseGeoPoint geoPoint, Location location){

        UserLocation = geoPoint;
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();

        userQuery.whereWithinKilometers("location",UserLocation,0.05);
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    numberUsers =  parseUsers.toArray().length;
                    System.out.println("Background Service - saveUserLocParse - query.findinBackground");
                    System.out.println("Success! Number Users: "+numberUsers);
                    System.out.println("Success! Retrieved: "+parseUsers);
                    if(numberUsers>=2){
                        ParseObject pO = parseUsers.get(1);
                        System.out.println("ID: "+pO.getObjectId());
                        displayNotification();
                    }
                    for(int i=0;i<numberUsers;i++){
                        ParseUser pu = parseUsers.get(i);
                        currentUser.add("metPeopleToday", pu);
                        currentUser.saveInBackground();
                    }

                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    System.out.println("Background Service - saveUserLocParse - query.findinBackground");
                    System.out.println("Error: "+e.getMessage());
                }
            }
        });


        sendLocResult(location, numberUsers);
    }
    public void saveArmyStuff(){

        dbSoldiers = dbHelper.getAllSoldiers();
        armyStrength = gameMechanics.getArmyStrength(dbSoldiers);
        int maxLevel = dbHelper.getMaxLevel();

        parseDb.setArmyStrength(armyStrength);
        parseDb.setMaxLevel(maxLevel);
        parseDb.setPlayerLevel(defaultPlayerLevel);
        parseDb.setEP(defaultEP);
    }

    protected void displayNotification() {

        Intent MainAIntent = new Intent(this, MainActivity.class);
        Intent FightAIntent = new Intent(this, FightActivity.class);
        Intent ArmyAIntent = new Intent(this, ArmyActivity.class);
        Intent ProfileAIntent = new Intent(this, ProfileActivity.class);

        PendingIntent MainApIntent = PendingIntent.getActivity(this, 0, MainAIntent, 0);
        PendingIntent FightApIntent = PendingIntent.getActivity(this, 0, FightAIntent, 0);
        PendingIntent ArmyApIntent = PendingIntent.getActivity(this, 0, ArmyAIntent, 0);
        PendingIntent ProfileApIntent = PendingIntent.getActivity(this, 0, ProfileAIntent, 0);


        Notification noti = new Notification.Builder(this)
                .setContentTitle(notiTitle)
                .setContentText(shortText)
                .setStyle(new Notification.BigTextStyle().bigText(longText))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(MainApIntent)
                .addAction(R.drawable.ic_menu_fight, fightAction, FightApIntent)
                .addAction(R.drawable.ic_menu_army, armyAction, ArmyApIntent)
                .addAction(R.drawable.ic_menu_profile, yourProfile, ProfileApIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

    }
}

