package com.pemws14.armyoffriends.background;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.location.LocationClient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pemws14.armyoffriends.GameMechanics;
import com.pemws14.armyoffriends.MainActivity;
import com.pemws14.armyoffriends.R;
import com.pemws14.armyoffriends.UserProfile;
import com.pemws14.armyoffriends.army.ArmyActivity;
import com.pemws14.armyoffriends.database.DbFight;
import com.pemws14.armyoffriends.database.DbHelper;
import com.pemws14.armyoffriends.database.DbSoldier;
import com.pemws14.armyoffriends.database.ParseDb;
import com.pemws14.armyoffriends.fight.FightActivity;
import com.pemws14.armyoffriends.profile_achievements.ProfileActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class BackgroundService extends Service {

    String longText = "You met someone!";
    String shortText = "You met someone!";
    String notiTitle = "Army of Friends";
    String fightAction = "Fight!";
    String armyAction = "Your Army";
    String yourProfile = "Your Profile";

    private long currentTime;

    DbHelper dbHelper;
    DbFight dbFight;
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

    public ParseUser addSoldierUser;
    public ParseObject TestUser;
    public ParseGeoPoint userLocation, TestUserLocation;
    public ArrayList<ParseGeoPoint> UserLocList;
    public int numberUsers;
    public ParseUser currentUser;

    public ParseQuery<ParseObject> armyStrengthQuery;

    // save armyStuff
    public ParseObject ArmyStrength;
    int armyStrength;
    int defaultPlayerLevel = 1;
    int defaultEP = 0;
    boolean armyExist;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        currentUser = ParseUser.getCurrentUser();
     //   counter++;
     //   Toast.makeText(this, " First Service Started" + "  " + counter, Toast.LENGTH_SHORT).show();

        Location location = intent.getParcelableExtra(LocationClient.KEY_LOCATION_CHANGED);
        if (location != null) {
            Log.i("onStartCommand", "onHandleIntent - Location available: " + location + " - Latitude: " + location.getLatitude()+" - Longitude: " + location.getLongitude());
        //    Toast.makeText(this, "new Location!", Toast.LENGTH_SHORT).show();
            parseDb = new ParseDb();
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
        Log.i("onBind", "BackgroundService - IBinder");
        //TODO for communication return IBinder implementation
        return null;
    }

    public void onCreate() {

        userProfile = new UserProfile();
        dbHelper = DbHelper.getInstance(getApplicationContext());
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

        Log.i("onCreate", "BackgroundService");
      //  Toast.makeText(this, "First Service was Created", Toast.LENGTH_SHORT).show();
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
//        Log.i("onCreate","BackgroundService - onStart");
//        // TODO Auto-generated method stub
//        super.onStart(intent, startId);
//        // your code for background process
//    }
    @Override
    public void onDestroy() {
        Log.i("onDestroy", "BackgroundService - onDestroy");
       // Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    }



    public void onTaskRemoved(Intent rootIntent) {

        MainActivity.alarm.cancel(MainActivity.pintent);
        this.stopSelf();
    }

    public void sendLocResult(Location location, int UserNum) {
        Intent intent = new Intent("com.pemws14.armyoffriends");
        intent.putExtra(LOCATION, location);
        intent.putExtra(LATITUDE, location.getLatitude());
        intent.putExtra(LONGITUDE, location.getLongitude());
        intent.putExtra(USER_COUNT, UserNum);
        sendBroadcast(intent);
    }

    public void getNearbyUsers(ParseGeoPoint geoPoint, Location location) {
        parseDb = new ParseDb();
        currentUser = ParseUser.getCurrentUser();
        userLocation = geoPoint;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentUser.getUpdatedAt());
        calendar.add(Calendar.MINUTE, -1);

        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereWithinKilometers("location", userLocation, 0.05);
        userQuery.whereNotEqualTo("username", currentUser.getUsername());
        userQuery.whereGreaterThan("updatedAt", calendar.getTime());
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e == null) {
                    if (parseUsers.toArray().length > 0) {
                        Log.i("getNearbyUsers", "parseUsers: " + parseUsers);
                        meet(parseUsers);
                    } else {
                        Log.i("getNearbyUsers", "Niemanden getroffen");
                    }
                    // Hooray! Let them use the app now.

                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.i("getNearbyUsers", "Background Service - saveUserLocParse - query.findinBackground");
                    Log.i("getNearbyUsers", "Error: " + e.getMessage());
                }
            }
        });


        sendLocResult(location, numberUsers);
    }

    public void saveArmyStuff() {

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


    public void meet(List<ParseUser> parseUsers) {

        List<ParseUser> listParseUser = parseDb.getMetPeopleToday();
        Log.i("addSoldier", "List ParseUser, heute schon getroffene User: " + listParseUser);

        /* man hat jemanden getroffen */
        if (parseUsers != null) {
            Log.i("meet", "Meet Success! Retrieved parseUsers: " + parseUsers);
            numberUsers = parseUsers.toArray().length;
            Log.i("meet", "Success! Number Users: " + numberUsers);


            /* der currentUser hat noch niemanden getroffen */
            if (listParseUser == null || listParseUser.size() == 0) {
                for (ParseUser parseUser : parseUsers) {
                    Log.i("meet", "CurrentUser: " + currentUser + " hat User: " + parseUser + " heute zum ERSTEN MAL getroffen! (Hatte noch niemanden getroffen)");
                    displayNotification();

                    /* gegenseitig hinzufügen */
                    ParseUser pu = parseUser;
                 /*   if(pu!=currentUser){
                        Log.i("meet","User: "+pu+" hat currentUser: "+currentUser+" hinzugefügt!");
                        pu.add("metPeopleToday", currentUser);
                        pu.saveInBackground();
                    }*/
                    Log.i("meet", "currentUser: " + currentUser + " hat User: " + pu + " hinzugefügt!");
                    //   currentUser.add("metPeopleToday", pu);
                    //   currentUser.saveInBackground();
                    addSoldier(pu);
                }

            /* der currentUser hat schon jemanden getroffen */
            } else {
                int k = listParseUser.toArray().length;

                for (ParseUser parseUser : parseUsers) {
                    for (ParseUser parseUser2 : listParseUser) {
                        int loopCount =0;
                        if (parseUser.hasSameId(parseUser2)) {
                            loopCount =0;
                            Log.i("meet", "CurrentUser: " + currentUser + " hat User: " + parseUser + " heute leider schon getroffen!");
                            break;
                        } else {
                            loopCount++;
                            if(loopCount==k){
                                loopCount =0;

                                Log.i("meet", "CurrentUser: " + currentUser + " hat User: " + parseUser + " heute zum ERSTEN MAL getroffen! (hat aber schon andere getroffen)");
                                displayNotification();

                            /* gegenseitig hinzufügen */
                                ParseUser pu = parseUser;
                         /*   if(pu!=currentUser){
                                Log.i("meet","User: "+pu+" hat currentUser: "+currentUser+" hinzugefügt!");
                                pu.add("metPeopleToday", currentUser);
                                pu.saveInBackground();
                            }*/
                                Log.i("meet", "currentUser: " + currentUser + " hat User: " + pu + " hinzugefügt!");
                                //  currentUser.add("metPeopleToday", pu);
                                //   currentUser.saveInBackground();
                                addSoldier(pu);
                            }

                        }
                    }
                }
            }
        } else {
            Log.i("meet", "leider niemanden getroffen!");
        }

    }

    public void addSoldier(ParseUser parseUser) {
        addSoldierUser = parseUser;
        Log.i("addSoldier", "addSoldier: " + parseUser.getUsername());
        String userName = parseUser.getUsername();
        List<DbSoldier> listDbSoldiers = dbHelper.getAllSoldiers();
        int k = listDbSoldiers.toArray().length;
        currentUser.add("metPeopleToday", parseUser);
        currentUser.saveInBackground();
        int loopCount =0;
        Log.i("addSoldier", "listDbSoldiers.toArray().length: " + listDbSoldiers.toArray().length);
        if (listDbSoldiers.toArray().length > 0) {
            //Log.i("addSoldier","min ein Soldat drin");
            for (DbSoldier dbSoldier : listDbSoldiers) {

                if (dbSoldier.getName().equals(userName)) {
                    loopCount =0;
                    //Log.i("addSoldier","lvl up Soldier: ");
                    dbHelper.levelUpSoldier(dbSoldier);
                    createFightEntry(dbSoldier);
                    if(parseDb.existFightImage(parseUser)){
                        //wenn vorher eine Datenbank Updates erfahren hat, muss das Objekt neu aus der DB geholt werden (z.B. dbHelper.getSoldier(dbSoldier.getId()), dbHelper.getFight(dbFight.getId()), ...)
                        parseDb.getFightImage(parseUser, dbHelper, dbHelper.getSoldier(dbSoldier.getName()), dbHelper.getFight(dbFight.getName()));
                    }else {
                        break;
                    }
                } else {
                    loopCount++;
                    if(loopCount==k){
                        loopCount =0;
                        //Log.i("addSoldier","create Soldier in der forschleife: ");
                        DbSoldier dbSoldierr = new DbSoldier(userName, BitmapFactory.decodeResource(getResources(), R.drawable.userpic_placeholder), 1);
                        dbHelper.createSoldier(dbSoldierr);
                        createFightEntry(dbSoldierr);
                        if(parseDb.existFightImage(parseUser)){
                            //wenn vorher eine Datenbank Updates erfahren hat, muss das Objekt neu aus der DB geholt werden (z.B. dbHelper.getSoldier(dbSoldier.getId()), dbHelper.getFight(dbFight.getId()), ...)
                            parseDb.getFightImage(parseUser, dbHelper, dbHelper.getSoldier(dbSoldierr.getName()), dbHelper.getFight(dbFight.getName()));

                        }
                    }
                }
            }
        } else {
            Log.i("addSoldier", "create Soldier im else zweig: ");
            DbSoldier dbSoldier = new DbSoldier(userName, BitmapFactory.decodeResource(getResources(), R.drawable.userpic_placeholder), 1);
            dbHelper.createSoldier(dbSoldier);
            createFightEntry(dbSoldier);
            if(parseDb.existFightImage(parseUser)){
                //wenn vorher eine Datenbank Updates erfahren hat, muss das Objekt neu aus der DB geholt werden (z.B. dbHelper.getSoldier(dbSoldier.getId()), dbHelper.getFight(dbFight.getId()), ...)
                parseDb.getFightImage(parseUser, dbHelper, dbHelper.getSoldier(dbSoldier.getName()), dbHelper.getFight(dbFight.getName()));
            }
        }
    }

    public void createFightEntry(DbSoldier dbSoldier) {
        dbFight = new DbFight(dbSoldier.getName(), dbSoldier.getImg(), dbSoldier.getLevel(), parseDb.getArmyStrength(addSoldierUser), parseDb.getMaxLevel(addSoldierUser));
        dbHelper.createFight(dbFight);

    }
}

