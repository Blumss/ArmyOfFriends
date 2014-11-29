package com.pemws14.armyoffriends;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.location.LocationClient;

import java.util.Calendar;

/**
 * Created by Ben on 25.11.2014.
 */
public class BackgroundService extends Service {
    Calendar cur_cal = Calendar.getInstance();
    public static int counter = 0; // zählt wie oft der Service schon gestartet wurde
    public static final String LOCATION = "location";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String BROADCAST_ACTION = "com.pemws14.armyoffriends";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
            sendLocResult(location);
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
        System.out.println("BackgroundService - onCreate");
        Toast.makeText(this, "First Service was Created", Toast.LENGTH_SHORT).show();
        // TODO Auto-generated method stub
        super.onCreate();
        Intent intent = new Intent(this, BackgroundService.class);
        PendingIntent pintent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        cur_cal.setTimeInMillis(System.currentTimeMillis());
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cur_cal.getTimeInMillis(), 60 * 1000*1, pintent); // repeat alle 1 Minute
    }
    @Override
    public void onStart(Intent intent, int startId) {
        counter++;
        Toast.makeText(this, " First Service Started" + "  " + counter, Toast.LENGTH_SHORT).show();
        System.out.println("BackgroundService - onStart");
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        // your code for background process
    }
    @Override
    public void onDestroy() {
        System.out.println("BackgroundService - onDestroy");
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    }
    public void onTaskRemoved (Intent rootIntent){

        MainActivity.alarm.cancel(MainActivity.pintent);
        this.stopSelf();
    }

    public void sendLocResult(Location location){
        Intent intent = new Intent("com.pemws14.armyoffriends");
        intent.putExtra(LOCATION, location);
        intent.putExtra(LATITUDE, location.getLatitude());
        intent.putExtra(LONGITUDE, location.getLongitude());
        sendBroadcast(intent);
    }
}


/*
public class BackgroundService extends Service {
    Calendar cur_cal = Calendar.getInstance();
    public static int counter = 0; // zählt wie oft der Service schon gestartet wurde

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        counter++;
        Toast.makeText(this, " First Service Started" + "  " + counter, Toast.LENGTH_SHORT).show();
        System.out.println("BackgroundService - onStartCommand");
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
        System.out.println("BackgroundService - onCreate");
        Toast.makeText(this, "First Service was Created", Toast.LENGTH_SHORT).show();
        // TODO Auto-generated method stub
        super.onCreate();
        Intent intent = new Intent(this, BackgroundService.class);
        PendingIntent pintent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        cur_cal.setTimeInMillis(System.currentTimeMillis());
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cur_cal.getTimeInMillis(), 60 * 1000*1, pintent); // repeat alle 1 Minute
    }
    @Override
    public void onStart(Intent intent, int startId) {
        counter++;
        Toast.makeText(this, " First Service Started" + "  " + counter, Toast.LENGTH_SHORT).show();
        System.out.println("BackgroundService - onStart");
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        // your code for background process
    }
    @Override
    public void onDestroy() {
        System.out.println("BackgroundService - onDestroy");
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    }
    public void onTaskRemoved (Intent rootIntent){

        MainActivity.alarm.cancel(MainActivity.pintent);
        this.stopSelf();
    }
}
 */

/*

public class BackgroundService extends IntentService {

    Location backgroundLoc;

    public BackgroundService() {
        super("Fused Location");
    }

    public BackgroundService(String name) {
        super("Fused Location");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("BackgroundService - onHandleIntent ");
        Location location = intent.getParcelableExtra(LocationClient.KEY_LOCATION_CHANGED);
        backgroundLoc = location;
        if(location !=null){
            System.out.println("BackgroundService - onHandleIntent - Location available");

            Toast.makeText(this, "new Location!", Toast.LENGTH_SHORT).show();

        }

    }
}

 */