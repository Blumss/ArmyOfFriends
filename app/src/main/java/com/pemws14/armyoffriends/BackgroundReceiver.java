package com.pemws14.armyoffriends;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Ben on 25.11.2014.
 */
public class BackgroundReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("BackgroundReceiver - onReceive");
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            System.out.println("BackgroundReceiver - onReceive - if Schleife drin - Intent wird gestartet");
            Intent pushIntent = new Intent(context, BackgroundService.class);
            context.startService(pushIntent);
        }
    }
}
