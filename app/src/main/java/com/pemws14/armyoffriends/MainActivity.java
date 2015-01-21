package com.pemws14.armyoffriends;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.pemws14.armyoffriends.database.DbFight;
import com.pemws14.armyoffriends.database.DbHelper;
import com.pemws14.armyoffriends.database.DbProfile;
import com.pemws14.armyoffriends.database.DbSoldier;
import com.pemws14.armyoffriends.database.ParseDb;
import com.pemws14.armyoffriends.fight.FightActivity;
import com.pemws14.armyoffriends.history.HistoryActivity;

import java.util.List;


public class MainActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener,LocationListener  {

    DbHelper db;
    ParseDb parseDb;
    DbProfile dbProfile;

    public static Context mainContext;

    Button locButton;
    ImageView armyButton;
    ImageView fightButton;
    ImageView historyButton;
    ImageView profileButton;
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

    public static PendingIntent pintent;
    public static AlarmManager alarm;

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

        MainActivity.mainContext = getApplicationContext();
        db = new DbHelper(mainContext);
        parseDb = new ParseDb();

        createDailyChallengeEntry();
        initDB();

        // findViewbyIDs
        locButton = (Button)findViewById(R.id.LocationButton);
        armyButton = (ImageView)findViewById(R.id.main_army);
        fightButton = (ImageView)findViewById(R.id.main_fight);
        historyButton = (ImageView)findViewById(R.id.main_latest);
        profileButton = (ImageView)findViewById(R.id.main_profile);
      //  loginButton = (Button)findViewById(R.id.start_login_button);
      //  logoutButton = (Button)findViewById(R.id.Button_LogoutUsername);
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

        locButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  getTheLastLocation();
                if (locButton.getText().equals("start Location Service")) {
                    startLocationIntent();
                    locButton.setText("stop Location Service");
                } else {
                    stopLocationIntent();
                    locButton.setText("start Location Service");
                }


            }
        });

        armyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ArmyActivity.class);
                startActivity(intent);
            }
        });


        fightButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FightActivity.class);
                startActivity(intent);
            }
        });


        historyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);
            }
        });


        profileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
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

        /*
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserLogout();
                System.out.println("User wurde ausgeloggt: ");
                System.out.println("User ist: "+currentUser);
            }
        });
        */
        //createSoldiers();
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

    private void createSoldiers(){
        for(int i=0; i<200;i++) {
            db.createSoldier(new DbSoldier("Prename Surname #"+i, i));
        }
        List<DbSoldier> solis = db.getAllSoldiers();
        for(DbSoldier s : solis ){
            Log.i("Soldiers", ""+s.getId()+", "+s.getName()+", " +s.getLevel()+", "+s.getRank()+", "+s.getCreated_at());
        }

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
                intent = new Intent(getApplicationContext(), DispatchActivity.class);
            }else {
                intent = new Intent(getApplicationContext(), Test.class);
            }
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

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
        //
        if(parseDb.getPlayerLevel()>0){
            System.out.println("parseDb.getPlayerLevel()>0 TRUE: "+parseDb.getPlayerLevel());   // wird ausgelöst, wenn es einen Eintrag gibt


        }else {
            System.out.println("parseDb.getPlayerLevel()>0 FALSE: "+parseDb.getPlayerLevel()); // wird ausgelöst, wenn es keinen Eintrag gibt
            parseDb.updateArmy(0,0,1,0);
        }
        System.out.println("USER ID: "+ParseUser.getCurrentUser().getObjectId());
        List<DbProfile> profiles = db.getAllProfiles();
        if(profiles.size()==0){
            dbProfile = new DbProfile(parseDb.getUserID(),parseDb.getCurrentUserName(),parseDb.getPlayerLevel(),parseDb.getEP(),parseDb.getArmyStrength(),parseDb.getMaxLevel());
            db.createProfile(dbProfile);
        }else{
            for (DbProfile oneProfile: profiles) {
                if (!(oneProfile.getServerID().equals(ParseUser.getCurrentUser().getObjectId()))) {
                    dbProfile = new DbProfile(parseDb.getUserID(), parseDb.getCurrentUserName(), parseDb.getPlayerLevel(), parseDb.getEP(), parseDb.getArmyStrength(), parseDb.getMaxLevel());
                    db.createProfile(dbProfile);
                } else {
                    dbProfile = db.getProfile(ParseUser.getCurrentUser().getObjectId());
                    Log.i("MainActivity.initDB", "Profile already existing");
                }
            }
        }
        System.out.println("### DB PROFIL: "+dbProfile);
        System.out.println("#############################");




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
        locationrequest = LocationRequest.create();
        locationrequest.setInterval(200); // von 100 auf 200 geändert
        locationclient.requestLocationUpdates(locationrequest, mPendingIntent);
       // getTheLastLocation();

    }

    public void stopLocationIntent(){
        System.out.println("MainActivity - stopLocationIntent ");
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

