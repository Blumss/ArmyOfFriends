package com.pemws14.armyoffriends;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pemws14.armyoffriends.army.ArmyActivity;
import com.pemws14.armyoffriends.fight.FightActivity;
import com.pemws14.armyoffriends.profile_achievements.ProfileActivity;


public class CreateNotificationActivity extends Activity {

    String longText = "You met someone!";
    String shortText = "You met someone!";
    String notiTitle = "Army of Friends";
    String fightAction = "Fight!";
    String armyAction = "Your Army";
    String yourProfile = "Your Profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_notification, menu);
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

    public void createNotification(View view){

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
