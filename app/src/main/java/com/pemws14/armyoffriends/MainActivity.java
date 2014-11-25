package com.pemws14.armyoffriends;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;


public class MainActivity extends Activity implements LocationListener  {
    Button LocButton;
    Button fightButton;
    TextView LocTextView;
    TextView longitudeText;
    TextView latitudeText;
    LocationListener locationListener;
    LocationManager locationManager;
    String provider;
    Criteria criteria;
    Location location;
    UserLocation userLocation;
    public static PendingIntent pintent;
    public static AlarmManager alarm;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Parse initialisieren (auf Ben's Account)
        Parse.initialize(this, "kzHM5DllHK2rzcAXShk0nXxpYCSWSil0B8SiWvJs", "KGS6HUZLE4oWBigpefoJiHr4GzmGO8WYmL3qYf69");

        // findViewbyIDs
        LocButton = (Button)findViewById(R.id.LocationButton);
        fightButton = (Button)findViewById(R.id.main_fight_button);
        LocTextView = (TextView)findViewById(R.id.LocationText);
        longitudeText = (TextView)findViewById(R.id.LongitudeText);
        latitudeText = (TextView)findViewById(R.id.LatiuteText);

        // UserLocation
        userLocation = new UserLocation();


        // Location Manager Instanz
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // überprüft, ob GPS an ist
        boolean GPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean NETWORKenabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        // EInstellungen werden aufgerufen, damit der Nutzer es anmachen kann
        // Ein Toast oder sowas wäre auch nett
        if (!GPSenabled) {
            //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            //startActivity(intent);
            System.out.println("GPS inaktiv");
        } else{
            System.out.println("GPS aktiv");
        }
        if (!NETWORKenabled){
            System.out.println("Network inaktiv");
        } else {
            System.out.println("Network aktiv");
        }
        // Define the criteria how to select the location provider -> use default
        criteria = new Criteria();
     //   criteria.setAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        provider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latitudeText.setText("Location not available");
            longitudeText.setText("Location not available");
        }

        // Location Button onClicklistener
        LocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserLocation();
            }
        });

      //  ParseObject testObject = new ParseObject("TestObject");
      //  testObject.put("foo", "bar");
      //  testObject.saveInBackground();

        fightButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FightActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Request updates at startup
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    // Remove the locationlistener updates when Activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {

        double lat = location.getLatitude();
        double lng = location.getLongitude();
        latitudeText.setText(String.valueOf(lat));
        longitudeText.setText(String.valueOf(lng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

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


    public void getUserLocation(){

        userLocation.getLocation(this, locationResult);

        /*
        location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latitudeText.setText("Location not available");
            longitudeText.setText("Location not available");
        }
        */
        // in Parse speichern noch nicht vollständig implementiert
        ParseGeoPoint point = new ParseGeoPoint(40.0, -30.0);
       // ParseGeoPoint curLoc = new ParseGeoPoint();
        ParseObject userObject = new ParseObject("UserObject");

        userObject.put("location", point);

        ParseGeoPoint userLocation  = (ParseGeoPoint) userObject.get("location");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("PlaceObject");

        double longitude = userLocation.getLongitude();
        double latitude = userLocation.getLatitude();
        String longiNew = "" + longitude;
        String latiNew = "" + latitude;

       // LocTextView.setText("new Location: Latitute: "+latiNew+", Longtitude: "+longiNew);

    }

    UserLocation.LocationResult locationResult = new UserLocation.LocationResult(){
        @Override
        public void gotLocation(Location location){

            //Got the location!
            System.out.println("got Location in Main Activity ausgeführt");
            if (location != null) {
                System.out.println("Location vorhanden! ");
                startBackgroundService();
                onLocationChanged(location);
            } else {
                latitudeText.setText("Location not available");
                longitudeText.setText("Location not available");

                System.out.println("Location not available! ");
            }

        }
    };

    protected ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            System.out.println("MainActivity - ServiceConnection - onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            System.out.println("MainActivity - ServiceConnection - onServiceDisconnected");

        }
    };

    public void startBackgroundService(){
        /*
        // use this to start and trigger a service
        Context context = this.getApplicationContext();
        Intent i= new Intent(context, BackgroundService.class);
        // potentially add data to the intent
       // i.putExtra("KEY1", "Value to be used by the service");
        context.startService(i);
        */
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);

        Intent intent = new Intent(this, BackgroundService.class);
        pintent = PendingIntent.getService(this, 0, intent, 0);

        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000*60, pintent);
    }
}
