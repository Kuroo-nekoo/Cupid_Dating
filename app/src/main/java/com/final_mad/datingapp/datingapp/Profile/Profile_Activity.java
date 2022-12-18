package com.final_mad.datingapp.datingapp.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.final_mad.datingapp.datingapp.Utils.Constants;
import com.final_mad.datingapp.datingapp.Utils.PreferenceManager;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.final_mad.datingapp.datingapp.R;
import com.final_mad.datingapp.datingapp.Utils.PulsatorLayout;
import com.final_mad.datingapp.datingapp.Utils.TopNavigationViewHelper;

public class Profile_Activity extends AppCompatActivity {
    private static final String TAG = "Profile_Activity";
    private static final int ACTIVITY_NUM = 0;
    static boolean active = false;
    private PreferenceManager preferenceManager;

    private Context mContext = Profile_Activity.this;
    private ImageView imagePerson;
    private TextView tvName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: create the page");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        preferenceManager = new PreferenceManager(getApplicationContext());

        PulsatorLayout mPulsator = findViewById(R.id.pulsator);
        mPulsator.start();

        setupTopNavigationView();

        imagePerson = findViewById(R.id.circle_profile_image);
        tvName = findViewById(R.id.tvName);
        tvName.setText(preferenceManager.getString(Constants.KEY_USER_NAME));


        ImageButton edit_btn = findViewById(R.id.edit_profile);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        ImageButton settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: resume to the page");

    }


    private void setupTopNavigationView() {
        Log.d(TAG, "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationViewEx tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }



}
