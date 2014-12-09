package com.pemws14.armyoffriends;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;
import com.pemws14.armyoffriends.Login.ParseLoginBuilder;


public class Test extends ActionBarActivity {

    TextView UsernameTextView;
    private ParseUser currentUser;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        UsernameTextView = (TextView)findViewById(R.id.Text_LoginUsername);
        logoutButton = (Button)findViewById(R.id.Button_LogoutUsername);

     //   UsernameTextView.setText(""+currentUser.getUsername());

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserLogout();
                System.out.println("User wurde ausgeloggt: ");
                System.out.println("User ist: "+currentUser);
              //  Intent intent = new Intent(getApplicationContext(), MainActivity.class);
              //  startActivity(intent);
                loginProcess();

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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
            System.out.println("User ist eingeloggt, Username: "+currentUser.getUsername());
            UsernameTextView.setText("Username: "+currentUser.getUsername());
        } else {
            //showProfileLoggedOut();
            loginProcess();
            System.out.println("User ist NICHT eingeloggt");

        }
    }

    public void loginProcess(){

        ParseLoginBuilder builder = new ParseLoginBuilder(Test.this);
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
