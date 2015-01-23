package com.pemws14.armyoffriends;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pemws14.armyoffriends.database.DbHelper;
import com.pemws14.armyoffriends.database.DbProfile;
import com.pemws14.armyoffriends.database.DbSoldier;
import com.pemws14.armyoffriends.database.ParseDb;
import com.pemws14.armyoffriends.drawer.BaseActivity;

import java.util.List;

public class ProfileActivity extends BaseActivity {
    private View view;
    private ImageView profileUserImage;

    private TextView profileUsername;
    private TextView profileCurrentLevel;
    private ProgressBar profileEpBar;
    private TextView profileNextLevel;
    private TextView profileActualArmyStrength;
    private TextView profileMaxArmyStrength;
    private TextView profileActiveSoldiers;
    private TextView profileTotalSoldiers;
    private TextView profileEpToNextLevel;

    private static final int RESULT_LOAD_IMAGE = 111;

    private DbHelper db;
    private DbProfile profile;
    private ParseDb parseDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //<-- IN EVERY ACTIVITY WITH DRAWER
        super.set();
        // catches Frame, where to insert actual ActivityView
        ViewGroup parent = (ViewGroup) findViewById(R.id.content_frame);
        //View of new activity
        view = LayoutInflater.from(this).inflate(R.layout.activity_profile, parent, false);
        parent.addView(view);
        //--> IN EVERY ACTIVITY WITH DRAWER
        setupDB();


        // TODO connect userImg to DB
        profileUserImage = (ImageView) findViewById(R.id.profile_user_image);
        profileUserImage.setImageBitmap(profile.getImg());
        System.out.println("profil bild: "+profile.getImg());

        profileUsername = (TextView) findViewById(R.id.profile_username);
        profileUsername.setText(profile.getUserName());

        profileCurrentLevel = (TextView) findViewById(R.id.profile_current_level);
        Integer level = new Integer(GameMechanics.getPlayerLevelForEp(profile.getEp()));
        profileCurrentLevel.setText(level.toString());
        profileNextLevel = (TextView) findViewById(R.id.profile_next_level);

        level = level+1;
        profileNextLevel.setText(level.toString());
        profileEpBar = (ProgressBar) findViewById(R.id.profile_ep_bar);
        int progress = (int)(100*GameMechanics.getPlayerLevelProgress(profile.getEp()));
        profileEpBar.setProgress(progress);

        profileEpToNextLevel = (TextView) findViewById(R.id.profile_ep_next_level);
        profileEpToNextLevel.setText(""+profile.getEp()+"/"+GameMechanics.getEpForPlayerLevelUp(profile.getPlayerLevel()));

        profileActualArmyStrength = (TextView) findViewById(R.id.profile_actual_army_strength);
        List<DbSoldier> limitedSoldiers = db.getLimitedSoldiers(GameMechanics.getMaxArmySize(level));
        Integer ownStrength = GameMechanics.getArmyStrength(limitedSoldiers);
        profileActualArmyStrength.setText(ownStrength.toString());

        profileMaxArmyStrength = (TextView) findViewById(R.id.profile_max_army_strength);
        Integer maxStrength = GameMechanics.getArmyStrength(db.getAllSoldiers());
        profileMaxArmyStrength.setText(maxStrength.toString());

        profileActiveSoldiers = (TextView) findViewById(R.id.profile_active_soldiers_number);
        profileActiveSoldiers.setText(((Integer)limitedSoldiers.size()).toString());

        profileTotalSoldiers = (TextView) findViewById(R.id.profile_total_soldiers_number);
        profileTotalSoldiers.setText(((Integer)db.getAllSoldiers().size()).toString());

        profileUserImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

    }

    private void setupDB() {
        parseDb = new ParseDb();
        db = new DbHelper(getApplicationContext());
        profile = db.getProfile(parseDb.getUserID());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE  && resultCode == Activity.RESULT_OK) {
            //  MainActivity activity = (MainActivity)getActivity();
            Bitmap bitmap = getBitmapFromCameraData(data, this);
            parseDb.saveImageInParse(bitmap);
            profile.setImg(bitmap);
            db.updateProfile(profile);
            profileUserImage.setImageBitmap(bitmap);
        }
    }

    private void setFullImageFromFilePath(String imagePath) {
        // Get the dimensions of the View
        int targetW = profileUserImage.getWidth();
        int targetH = profileUserImage.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        // bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        profileUserImage.setImageBitmap(bitmap);
    }

    public static Bitmap getBitmapFromCameraData(Intent data, Context context){
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath,options);
        options.inSampleSize = calculateInSampleSize(options, 200,200);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picturePath, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
