package com.pemws14.armyoffriends;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Ben on 08.12.2014.
 */
public class ApplicationDetails extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Required - Initialize the Parse SDK
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));

     //   Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Optional - If you don't want to allow Twitter login, you can
        // remove this line (and other related ParseTwitterUtils calls)
      //  ParseTwitterUtils.initialize(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret));
    }
}
