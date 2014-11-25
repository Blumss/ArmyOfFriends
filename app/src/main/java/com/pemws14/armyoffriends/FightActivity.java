package com.pemws14.armyoffriends;

import android.os.Bundle;

import com.pemws14.armyoffriends.drawer.BaseActivity;


public class FightActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //<-- IN EVERY ACTIVITY WITH DRAWER
        super.set();
        //--> IN EVERY ACTIVITY WITH DRAWER
    }


}
