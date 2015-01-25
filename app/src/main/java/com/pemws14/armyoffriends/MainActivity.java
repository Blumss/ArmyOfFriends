package com.pemws14.armyoffriends;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.parse.ParseUser;
import com.pemws14.armyoffriends.army.ArmyActivity;
import com.pemws14.armyoffriends.background.BackgroundReceiver;
import com.pemws14.armyoffriends.background.BackgroundService;
import com.pemws14.armyoffriends.database.DbAchievement;
import com.pemws14.armyoffriends.database.DbFight;
import com.pemws14.armyoffriends.database.DbHelper;
import com.pemws14.armyoffriends.database.DbProfile;
import com.pemws14.armyoffriends.database.DbSoldier;
import com.pemws14.armyoffriends.database.ParseDb;
import com.pemws14.armyoffriends.fight.FightActivity;
import com.pemws14.armyoffriends.history.HistoryActivity;
import com.pemws14.armyoffriends.profile_achievements.ProfileActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener,LocationListener  {

    DbHelper db;
    ParseDb parseDb;
    DbProfile dbProfile;

    public static Context mainContext;

    ImageView locationIcon;
    ImageView saveImageIcon;
    ImageView profileView;
    LinearLayout armyButton;
    LinearLayout fightButton;
    LinearLayout historyButton;
    LinearLayout profileButton;
    ImageView logoutButton;
  //  Button loginButton;
  //  Button logoutButton;
    TextView locTextView;
    TextView longitudeText;
    TextView latitudeText;
    TextView userNumberText;
    LocationListener locationListener;
    LocationManager locationManager;
    String provider;
    Criteria criteria;
    Location location;

    private ParseUser currentUser;

    private static final int RESULT_LOAD_IMAGE = 111;

    public static PendingIntent pintent;
    public static AlarmManager alarm;
    public static boolean serviceOn;

    // private TextView txtConnectionStatus;
    private TextView txtLastKnownLoc;
    private EditText etLocationInterval;
    private TextView txtLocationRequest;

    private LocationClient locationclient;
    private LocationRequest locationrequest;
    private Intent mIntentService;
    private PendingIntent mPendingIntent;
    Location loc;

    BackgroundReceiver backgroundReceiver;

    double longi;
    double lati;
    int numberUsers;

    // Breite = latitude (steht vorne), Länge = Longitude (steht hinten)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("MainActivity - onCreate ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();

        MainActivity.mainContext = getApplicationContext();
        db = DbHelper.getInstance(mainContext);
        parseDb = new ParseDb();

        LayoutInflater inflatter =(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // getting access to laytou inflatter

        createDailyChallengeEntry();
        createAchievements();
        initDB();

      //  View v = new ImageView()

        // findViewbyIDs
        locationIcon = (ImageView)findViewById(R.id.LocationButton);
        armyButton = (LinearLayout)findViewById(R.id.main_army);
        fightButton = (LinearLayout)findViewById(R.id.main_fight);
        historyButton = (LinearLayout)findViewById(R.id.main_latest);
        profileButton = (LinearLayout)findViewById(R.id.main_profile);
        logoutButton = (ImageView)findViewById(R.id.logout_button);
        profileView = (ImageView)findViewById(R.id.profile_user_image);
      //  loginButton = (Button)findViewById(R.id.start_login_button);
        locTextView = (TextView)findViewById(R.id.LocationText);
        longitudeText = (TextView)findViewById(R.id.LongitudeText);
        latitudeText = (TextView)findViewById(R.id.LatitudeText);
        userNumberText = (TextView)findViewById(R.id.CountNearUsersText);

        backgroundReceiver = new BackgroundReceiver();
        mIntentService = new Intent(this,BackgroundService.class);
        mPendingIntent = PendingIntent.getService(this, 1, mIntentService, 0);
        int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if(resp == ConnectionResult.SUCCESS){
            System.out.println("MainActivity - onCreate - GooglePlayServiceConnection: SUCCESS ");
            locationclient = new LocationClient(this,this,this);
            locationclient.connect();
        }else{
            System.out.println("MainActivity - onCreate - GooglePlayServiceConnection: FAILURE ");
            Toast.makeText(this, "Google Play Service Error " + resp, Toast.LENGTH_LONG).show();
        }

      //  ParseObject testObject = new ParseObject("TestObject");
      //  testObject.put("foo", "bar");
      //  testObject.saveInBackground();

        locationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  getTheLastLocation();
                if (!serviceOn) {
                    startLocationIntent();
                    locationIcon.setImageResource(R.drawable.actionbar_location);
                } else {
                    stopLocationIntent();
                    locationIcon.setImageResource(R.drawable.actionbar_location_off);
                }
            }
        });
/*
        saveImageIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
              //  Intent i = new Intent(
             //   Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
             //   startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });*/



        armyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {startActivity(ArmyActivity.class);
            }
        });


        fightButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {startActivity(FightActivity.class);
            }
        });


        historyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {startActivity(HistoryActivity.class);
            }
        });


        profileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {startActivity(ProfileActivity.class);
            }
        });

        /*
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Test.class);
                startActivity(intent);
            }
        });
        */

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopLocationIntent();

                Intent intent;
                currentUser = ParseUser.getCurrentUser();
                ParseUser.logOut();
                intent = new Intent(getApplicationContext(), DispatchActivity.class);
                startActivity(intent);
            }
        });

        //createSoldiers();
    }

    /*
    onClickHandler for Activities
     */
    public void startActivity(Class activity){
        Intent intent;
        currentUser = ParseUser.getCurrentUser();
        if (currentUser==null){
            Toast.makeText(getApplicationContext(), "You need to login first!", Toast.LENGTH_SHORT).show();
            intent = new Intent(getApplicationContext(), DispatchActivity.class);
        }else{
            intent = new Intent(getApplicationContext(), activity);
        }
        startActivity(intent);
    }


    /*
    Saves the first spot in DbFight for Daily Challenge
     */
    private void createDailyChallengeEntry() {
        if (db.getAllFights().isEmpty()){
            Log.i("MainActivity.createDailyChallengeEntry","Creating initial Daily Challenge entry");
            DbFight dailyChallenge = new DbFight(1, "Daily Challenge", 1, 0, 0, "0", 0);
            db.createFight(dailyChallenge);
        }else if (db.getAllFights().get(0).getId()==1){
            Log.i("MainActivity.createDailyChallengeEntry","Daily Challenge already existing!");
        }
    }

    /*
    * Creates all possible achievements if not already done
     */
    private void createAchievements() {
        if (db.getAllAchievements().isEmpty()){
            Log.i("MainActivity.createAchievements","Creating all Achievements");
            List<DbAchievement> achievements = setRequirements();
            for(DbAchievement achievement: achievements){
                db.createAchievement(achievement);
                Log.i("MainActivity.createAchievements", "Creating #" + achievement.getId() + ": " + achievement.getTitle());
            }
        }else{
            Log.i("MainActivity.createAchievements","Achievements already existing!");
        }
    }

    private List<DbAchievement> setRequirements(){
        List<DbAchievement> achievements = new ArrayList<DbAchievement>();
        achievements.add(new DbAchievement("Fire!","Start a fight", 1, 0));
        achievements.add(new DbAchievement("Just starting...","Win a fight", 1, 0));
        achievements.add(new DbAchievement("Getting into it","Win 10 fights", 10, 0));
        achievements.add(new DbAchievement("I'm lovin' it","Win 25 fights", 25, 0));
        achievements.add(new DbAchievement("Nice fighting","Win 50 fights", 50, 0));
        achievements.add(new DbAchievement("Fearless!","Win 100 fights", 100, 0));
        achievements.add(new DbAchievement("Dominator!","Win 250 fights", 250, 0));
        achievements.add(new DbAchievement("Fearless!","Win 500 fights", 500, 0));
        achievements.add(new DbAchievement("Just a little setback","Lose 1 fight", 1, 0));
        achievements.add(new DbAchievement("Lucky you!","Win 10 close fights", 10, 0));
        achievements.add(new DbAchievement("Need a lucky charm?","Lose 10 close fights", 10, 0));
        achievements.add(new DbAchievement("Time to strike","First soldier added", 1, 0));
        achievements.add(new DbAchievement("MOAR soldiers","10 soldiers added", 10, 0));
        achievements.add(new DbAchievement("Army!","100 soldiers added", 100, 0));
        achievements.add(new DbAchievement("Private party","Have 25 privates in your army", 25, 0));
        achievements.add(new DbAchievement("Major upgrade","Get a soldier to rank Major", 1, 0));
        achievements.add(new DbAchievement("General upgrade","Get a soldier to rank General ", 1, 0));
        achievements.add(new DbAchievement("Monster hunter","Win a daily challenge", 1, 0));
        achievements.add(new DbAchievement("Monster smasher","Win 30 daily challenges", 30, 0));
        achievements.add(new DbAchievement("Growing stronger","ArmyStrength over 500", 500, 0));
        achievements.add(new DbAchievement("Strong enough","ArmyStrength over 2500", 2500, 0));
        achievements.add(new DbAchievement("Great power - Great responsibility","ArmyStrength over 5000", 5000, 0));
        achievements.add(new DbAchievement("Overpowered","ArmyStrength over 10000", 10000, 0));
        achievements.add(new DbAchievement("Getting an upgrade","Get an EP-level-up", 2, 0));
        return achievements;
    }

    private void createSoldiers(){
//        for(int i=0; i<20;i++) {
//            db.createSoldier(new DbSoldier("Prename Surname #"+i, BitmapFactory.decodeResource(getResources(),R.drawable.userpic_placeholder), i));
//        }
//        List<DbSoldier> solis = db.getAllSoldiers();
//        for(DbSoldier s : solis ){
//            Log.i("Soldiers", ""+s.getId()+", "+s.getName()+", " +s.getLevel()+", "+s.getRank()+", "+s.getCreated_at());
//        }
        db.createSoldier(new DbSoldier("Prename_Surname", BitmapFactory.decodeResource(getResources(),R.drawable.userpic_placeholder), 1));
        System.out.println(db.getSoldier("Prename_Surname").getName());
    }

    public static Context getAppContext() {
        return MainActivity.mainContext;
    }

/*
    @Override
    protected void onStart() {
        super.onStart();

        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            //showProfileLoggedIn();
            System.out.println("User ist eingeloggt, Username: "+currentUser.getUsername());
        } else {
            //showProfileLoggedOut();
            loginProcess();
            System.out.println("User ist NICHT eingeloggt");

        }
    }
*/
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                lati = bundle.getDouble(BackgroundService.LATITUDE);
                longi = bundle.getDouble(BackgroundService.LONGITUDE);
                numberUsers = bundle.getInt(BackgroundService.USER_COUNT) - 1;
                // String string = bundle.getString(BackgroundService.LOCATION);
                latitudeText.setText(String.valueOf(lati));
                longitudeText.setText(String.valueOf(longi));
                userNumberText.setText(String.valueOf(numberUsers));

            }

            System.out.println("BackgroundReceiver - onReceive");
            if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
                System.out.println("BackgroundReceiver - onReceive - if Schleife drin - Intent wird gestartet");
                Intent pushIntent = new Intent(context, BackgroundService.class);
                context.startService(pushIntent);
            }


        }
    };
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        currentUser = ParseUser.getCurrentUser();
        MenuItem actionBarUser = menu.findItem(R.id.action_login);
        if (currentUser != null) {
            actionBarUser.setTitle("Logout");
        }else{
            actionBarUser.setTitle("Login");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //TODO: Start Settings-Intent
            return true;
        }

        if (id == R.id.action_login) {
            Intent intent;
            if(item.getTitle().equals("Logout")){
                currentUser = ParseUser.getCurrentUser();
                ParseUser.logOut();
            }
            intent = new Intent(getApplicationContext(), DispatchActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationclient!=null)
            locationclient.disconnect();
    }

    @Override
    public void onResume(){
        super.onResume();
        invalidateOptionsMenu();
        registerReceiver(broadcastReceiver, new IntentFilter(BackgroundService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        System.out.println("MainActivity - onConnected - Connection Status : Connected ");
    }

    @Override
    public void onDisconnected() {
        System.out.println("MainActivity - onDisconnected - Connection Status : Disconnected ");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        System.out.println("MainActivity - onConnectionFailed - Connection Status : Fail ");
    }

    @Override
    public void onLocationChanged(Location location) {

        if(location!=null){
            System.out.println("MainActivity - onLocationChanged - Location : available ");

          //  latitudeText.setText(""+location.getLatitude());
          //  longitudeText.setText(""+ location.getLongitude());
        } else {
            System.out.println("MainActivity - onLocationChanged - Location : not available ");
        }

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }



    public void initDB(){
        if(parseDb.getPlayerLevel()>0){
            System.out.println("parseDb.getPlayerLevel()>0 TRUE: "+parseDb.getPlayerLevel());   // wird ausgelöst, wenn es einen Eintrag gibt
        }else {
            System.out.println("parseDb.getPlayerLevel()>0 FALSE: "+parseDb.getPlayerLevel()); // wird ausgelöst, wenn es keinen Eintrag gibt
            parseDb.updateArmy(0,0,1,0);
        }
        String parseUserID= ParseUser.getCurrentUser().getObjectId();
        List<DbProfile> profiles = db.getAllProfiles();
        if(profiles.size()==0){
            Log.i("MainActivity.initDB", "No profiles found on device! Creating profile with ID " + parseUserID + ".");
            //TODO das profilbild sollte aus Parse noch geladen werden können - Funktioniert iwie nicht, da Nullpointer Exception...
            //dbProfile = new DbProfile(parseDb.getUserID(),parseDb.getCurrentUserName(),parseDb.existImage() ? parseDb.getImage(): BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.profile_placeholder),parseDb.getPlayerLevel(),parseDb.getEP(),parseDb.getArmyStrength(),parseDb.getMaxLevel());
            dbProfile = new DbProfile(parseDb.getUserID(),parseDb.getCurrentUserName(),BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.profile_placeholder),parseDb.getPlayerLevel(),parseDb.getEP(),parseDb.getArmyStrength(),parseDb.getMaxLevel());
            db.createProfile(dbProfile);
            if(parseDb.existImage()){
                System.out.println("getimage != null -> bild wird aus db genommen");
                dbProfile.setImg(parseDb.getImage());
            }

        }else{
            for (DbProfile oneProfile: profiles) {
                if (!(oneProfile.getServerID().equals(parseUserID))) {
                    Log.i("MainActivity.initDB", "Profile with ID " + parseUserID + " not under existing profiles. Creating it!");
                    dbProfile = new DbProfile(parseDb.getUserID(), parseDb.getCurrentUserName(), BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.profile_placeholder),parseDb.getPlayerLevel(), parseDb.getEP(), parseDb.getArmyStrength(), parseDb.getMaxLevel());
                    db.createProfile(dbProfile);
                    if(parseDb.getImage()!=null){
                        System.out.println("getimage != null -> bild wird aus db genommen");
                        dbProfile.setImg(parseDb.getImage());
                    }

                } else {
                    dbProfile = db.getProfile(parseUserID);
                    if(parseDb.existImage()){
                        System.out.println("getimage != null -> bild wird aus db genommen");
                        dbProfile.setImg(parseDb.getImage());

                    }

                    Log.i("MainActivity.initDB", "Profile with ID " + parseUserID + " already existing");
                }
            }
        }
        System.out.println("################# Profile logged #################");




        //    System.out.println("Gibt es ein Profil: "+db.getProfile(parseDb.getUserID()));
/*
        if(db.getProfile(parseDb.getUserID())!=null){
            System.out.println("#############################");
            System.out.println("### Profil vorhanden ###");
            System.out.println("#############################");
            db.updateProfile(dbProfile);
        }else{
            System.out.println("#############################");
            System.out.println("### kein Profil vorhanden ###");
            System.out.println("#############################");
            db.createProfile(dbProfile);
        }*/
    }

    public void getTheLastLocation(){
        System.out.println("MainActivity - getTheLastLocation ");
        if(locationclient!=null && locationclient.isConnected()){
            System.out.println("MainActivity - getTheLastLocation - Last Known Location available");
            Location loc = locationclient.getLastLocation();
            locTextView.setText("Last Known Location: ");

            System.out.println("MainActivity - getTheLastLocation - Loc: "+loc);
            System.out.println("MainActivity - getTheLastLocation - Latitude: "+loc.getLatitude());
            System.out.println("MainActivity - getTheLastLocation - Longitude: "+loc.getLongitude());
            setLocText(loc);
        }
    }

    /*
    public void startLocRequest(){
        System.out.println("MainActivity - startLocRequest ");
        if(locationclient!=null && locationclient.isConnected()) {
            locationrequest = LocationRequest.create();
            locationrequest.setInterval(Long.parseLong(etLocationInterval.getText().toString()));
            locationclient.requestLocationUpdates(locationrequest, this);
        }
    } */
    /*
    public void stopLocRequest(){
        locationclient.removeLocationUpdates(this);
    }
    */
    public void startLocationIntent(){
        System.out.println("MainActivity - startLocationIntent ");
        serviceOn = true;
        locationrequest = LocationRequest.create();
        locationrequest.setInterval(200); // von 100 auf 200 geändert
        locationclient.requestLocationUpdates(locationrequest, mPendingIntent);
       // getTheLastLocation();

    }

    public void stopLocationIntent(){
        System.out.println("MainActivity - stopLocationIntent ");
        serviceOn = false;
        locationclient.removeLocationUpdates(mPendingIntent);
    }

    public void setLocText(Location location){
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        latitudeText.setText(String.valueOf(lat));
        longitudeText.setText(String.valueOf(lng));
    }

    public void setLocWithReceiver(){
        double lat = backgroundReceiver.getLati();
        double lng = backgroundReceiver.getLongi();
        latitudeText.setText(String.valueOf(lat));
        longitudeText.setText(String.valueOf(lng));
    }
/*
    public void loginProcess(){

        ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
        Intent parseLoginIntent = builder
                .setAppLogo(R.drawable.ic_launcher)
                .setParseLoginEnabled(true)
                .setParseLoginButtonText("Go")
                .setParseSignupButtonText("Register")
                .setParseLoginHelpText("Forgot password?")
                .setParseLoginInvalidCredentialsToastText("You email and/or password is not correct")
                        //      .setParseLoginEmailAsUsername(true)
                .setParseSignupSubmitButtonText("Submit registration")
                        //     .setFacebookLoginEnabled(true)
                        //     .setFacebookLoginButtonText("Facebook")
                        //      .setFacebookLoginPermissions(Arrays.asList("public_profile", "user_friends"))
                        //     .setTwitterLoginEnabled(true)
                        //      .setTwitterLoginButtontext("Twitter")
                .setParseSignupMinPasswordLength(6)

                .build();
        startActivityForResult(parseLoginIntent, 0);

    }*/
/*
    public void UserLogout(){
        currentUser = ParseUser.getCurrentUser();
        Parse
        User.logOut();
    }
    */
}

