package com.pemws14.armyoffriends;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.security.Provider;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ben on 24.11.2014.
 */
public class UserLocation {
    Timer timer1;
    LocationManager locationManager;
    LocationResult locationResult;
    boolean GPS_enabled=false;
    boolean NETWORK_enabled=false;
    boolean PASSIVE_enabled=false;
    Provider GPSprovider, NETProvider;
    Criteria GPSCriteria, NETCriteria;


    public boolean getLocation(Context context, LocationResult result)
    {

        /* ohne Criteria noch
            // Define the criteria how to select the location provider -> use default
            GPSCriteria = new Criteria();
            //   criteria.setAccuracy(Criteria.ACCURACY_HIGH);
            GPSCriteria.setPowerRequirement(Criteria.POWER_LOW);
            provider = locationManager.getBestProvider(criteria, true);
            location = locationManager.getLastKnownLocation(provider);

         */

        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult=result;

        if(locationManager==null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        //exceptions will be thrown if provider is not permitted.
        try{
            GPS_enabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception ex){
            System.out.println("GPS Exception: " + ex);
        }
        try{
            NETWORK_enabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){
            System.out.println("NETWORK Exception: "+ex);
        }
        try{
            PASSIVE_enabled=locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
        }catch(Exception ex){
            System.out.println("PASSIVE Exception: "+ex);
        }


        if(!GPS_enabled){
            System.out.println("GPS disabled ");
        }
        if(!NETWORK_enabled){
            System.out.println("Network disabled ");
        }
        if(!PASSIVE_enabled){
            System.out.println("PASSIVE disabled ");
        }
        //don't start listeners if no provider is enabled
        if(!GPS_enabled && !NETWORK_enabled) {

            System.out.println("GPS und Network sind aus");
            return false;
        }
        if(GPS_enabled){
            System.out.println("GPS enabled: ");
          //  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps); // original
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, locationListenerGps); // verändert
        }
        if(NETWORK_enabled){
            System.out.println("NETWORK enabled: ");
         //   locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork); // original
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1, locationListenerNetwork); // verändert
        }
        if(PASSIVE_enabled){
            System.out.println("Passive enabled: ");
            //   locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListenerNetwork); // original
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 400, 1, locationListenerNetwork); // verändert
        }
        timer1=new Timer();
        timer1.schedule(new GetLastLocation(), 30000);
        return true;
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerNetwork);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerGps);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            locationManager.removeUpdates(locationListenerGps);
            locationManager.removeUpdates(locationListenerNetwork);

            Location net_loc=null, gps_loc=null;
            if(GPS_enabled) {
                gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if(NETWORK_enabled) {
                net_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            //if there are both values use the latest one
            if(gps_loc!=null && net_loc!=null){
                if(gps_loc.getTime()>net_loc.getTime())
                    locationResult.gotLocation(gps_loc);
                else
                    locationResult.gotLocation(net_loc);
                return;
            }

            if(gps_loc!=null){
                locationResult.gotLocation(gps_loc);
                return;
            }
            if(net_loc!=null){
                locationResult.gotLocation(net_loc);
                return;
            }
            locationResult.gotLocation(null);
        }
    }

    public static abstract class LocationResult{
        public abstract void gotLocation(Location location);
    }

}
