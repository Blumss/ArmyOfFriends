package com.pemws14.armyoffriends;

import com.pemws14.armyoffriends.login.ParseLoginDispatchActivity;

/**
 * Created by Ben on 08.12.2014.
 */
public class DispatchActivity extends ParseLoginDispatchActivity {


    @Override
    protected Class<?> getTargetClass() {

        // zu testzwecken:
       // return Test.class;

        // wenn die Mainactivity geschützt wird:
        return MainActivity.class;
    }


}