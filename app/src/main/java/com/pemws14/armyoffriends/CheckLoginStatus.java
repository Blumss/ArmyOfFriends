package com.pemws14.armyoffriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;
import com.pemws14.armyoffriends.login.ParseLoginBuilder;


public class CheckLoginStatus extends ActionBarActivity {

    TextView UsernameTextView;
    public ParseUser currentUser;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_login_status);


        UsernameTextView = (TextView)findViewById(R.id.Text_LoginUsername);
        logoutButton = (Button)findViewById(R.id.Button_LogoutUsername);

      //  UsernameTextView.setText(""+currentUser.getUsername());

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserLogout();
                Log.i("onCreate","User wurde ausgeloggt: " +  currentUser);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check_login_status, menu);
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

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            //showProfileLoggedIn();
            Log.i("onStart","User ist eingeloggt, Username: "+currentUser.getUsername());
            UsernameTextView.setText(""+currentUser.getUsername());
        } else {
            //showProfileLoggedOut();
            loginProcess();
            Log.i("onStart","User ist NICHT eingeloggt");

        }
    }

    public void loginProcess(){

        ParseLoginBuilder builder = new ParseLoginBuilder(CheckLoginStatus.this);
        Intent parseLoginIntent = builder
                .setAppLogo(R.drawable.ic_launcher)
                .setParseLoginEnabled(true)
                .setParseLoginButtonText("Go")
                .setParseSignupButtonText("Register")
                .setParseLoginHelpText("Forgot password?")
                .setParseLoginInvalidCredentialsToastText("You email and/or password is not correct")
                        //      .setParseLoginEmailAsUsername(true)
                .setParseSignupSubmitButtonText("Submit registration")
                        //     .setFacebookLoginEnabled(true)
                        //     .setFacebookLoginButtonText("Facebook")
                        //      .setFacebookLoginPermissions(Arrays.asList("public_profile", "user_friends"))
                        //     .setTwitterLoginEnabled(true)
                        //      .setTwitterLoginButtontext("Twitter")
                .setParseSignupMinPasswordLength(6)

                .build();
        startActivityForResult(parseLoginIntent, 0);

    }

    public void UserLogout(){
        currentUser = ParseUser.getCurrentUser();
        ParseUser.logOut();
    }
}
