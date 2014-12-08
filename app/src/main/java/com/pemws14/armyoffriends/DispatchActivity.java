package com.pemws14.armyoffriends;

import com.pemws14.armyoffriends.Login.ParseLoginDispatchActivity;

/**
 * Created by Ben on 08.12.2014.
 */
public class DispatchActivity extends ParseLoginDispatchActivity {

    @Override
    protected Class<?> getTargetClass() {
        return MainActivity.class;
    }
}