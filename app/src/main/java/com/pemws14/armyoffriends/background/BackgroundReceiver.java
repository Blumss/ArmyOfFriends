package com.pemws14.armyoffriends.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.pemws14.armyoffriends.database.ParseDb;

/**
 * Created by Ben on 25.11.2014.
 */
public class BackgroundReceiver extends BroadcastReceiver{
    ParseDb parseDb;
    private final String SOMEACTION = "com.pemws14.armyoffriends.ACTION"; //packagename is com.whatever.www
    private final String BOOT = "android.intent.action.BOOT_COMPLETED";
    double longi;
    double lati;



    @Override
    public void onReceive(Context context, Intent intent) {
        parseDb = new ParseDb();
      //  String category = intent.getCategories().toString();
        Log.i("onReceive", "Action: " + intent.getAction());
        Log.i("onReceive","SOMEACTION:: "+SOMEACTION);
        Log.i("onReceive","BackgroundReceiver - onReceive, Intent Action: "+intent.getAction()+" Intent: "+intent+"  Intent Categorie:"+ intent.getCategories());
        if (BOOT.equals(intent.getAction())) {
            Log.i("onReceive","BackgroundReceiver - onReceive - if Schleife drin - Intent wird gestartet");
            Intent pushIntent = new Intent(context, BackgroundService.class);
            context.startService(pushIntent);
        }

        if(SOMEACTION.equals(intent.getAction())){
      //  else{
            Log.i("onReceive","BackgroundReceiver - ALARM!!");
            parseDb.deleteMetPeople();
        }



        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            lati = bundle.getDouble(BackgroundService.LATITUDE);
            longi = bundle.getDouble(BackgroundService.LONGITUDE);
            // String string = bundle.getString(BackgroundService.LOCATION);

        }

    }
    public double getLongi(){
        return longi;
    }
    public double getLati(){
        return lati;
    }
}
