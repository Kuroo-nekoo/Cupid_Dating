package com.final_mad.datingapp.datingapp.Account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.final_mad.datingapp.datingapp.R;
import com.final_mad.datingapp.datingapp.Utils.User;


public class RegisterHobby extends AppCompatActivity {
    private static final String TAG = "RegisterHobby";

    //User Info
    User user;

    private Context mContext;
    private Button hobbiesContinueButton;
    private Button sportsSelectionButton;
    private Button travelSelectionButton;
    private Button musicSelectionButton;
    private Button fishingSelectionButton;


    private String append = "";
    private TextView tvLinkLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_hobby);
        mContext = RegisterHobby.this;
        tvLinkLogin = findViewById(R.id.tvLinkLogin);

        tvLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterHobby.this, Login.class));
            }
        });

        Log.d(TAG, "onCreate: started");

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("classUser");

        initWidgets();

        init();
    }

    private void initWidgets() {
        sportsSelectionButton = findViewById(R.id.sportsSelectionButton);
        travelSelectionButton = findViewById(R.id.travelSelectionButton);
        musicSelectionButton = findViewById(R.id.musicSelectionButton);
        fishingSelectionButton = findViewById(R.id.fishingSelectionButton);
        hobbiesContinueButton = findViewById(R.id.hobbiesContinueButton);

        // Initially all the buttons needs to be grayed out so this code is added, on selection we will enable it later
        sportsSelectionButton.setAlpha(.5f);
        sportsSelectionButton.setBackgroundColor(Color.GRAY);

        travelSelectionButton.setAlpha(.5f);
        travelSelectionButton.setBackgroundColor(Color.GRAY);

        musicSelectionButton.setAlpha(.5f);
        musicSelectionButton.setBackgroundColor(Color.GRAY);

        fishingSelectionButton.setAlpha(.5f);
        fishingSelectionButton.setBackgroundColor(Color.GRAY);


        sportsSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sportsButtonClicked();
            }
        });

        travelSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                travelButtonClicked();
            }
        });

        musicSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicButtonClicked();
            }
        });

        fishingSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fishingButtonClicked();
            }
        });


    }

    public void sportsButtonClicked() {
        // this is to toggle between selection and non selection of button
        if (sportsSelectionButton.getAlpha() == 1.0f) {
            sportsSelectionButton.setAlpha(.5f);
            sportsSelectionButton.setBackgroundColor(Color.GRAY);
            user.setSports(false);
        } else {
            sportsSelectionButton.setBackgroundColor(Color.parseColor("#FF4081"));
            sportsSelectionButton.setAlpha(1.0f);
            user.setSports(true);
        }
    }

    public void travelButtonClicked() {
        // this is to toggle between selection and non selection of button
        if (travelSelectionButton.getAlpha() == 1.0f) {
            travelSelectionButton.setAlpha(.5f);
            travelSelectionButton.setBackgroundColor(Color.GRAY);
            user.setTravel(false);
        } else {
            travelSelectionButton.setBackgroundColor(Color.parseColor("#FF4081"));
            travelSelectionButton.setAlpha(1.0f);
            user.setTravel(true);

        }

    }

    public void musicButtonClicked() {
        // this is to toggle between selection and non selection of button
        if (musicSelectionButton.getAlpha() == 1.0f) {
            musicSelectionButton.setAlpha(.5f);
            musicSelectionButton.setBackgroundColor(Color.GRAY);
            user.setMusic(false);
        } else {
            musicSelectionButton.setBackgroundColor(Color.parseColor("#FF4081"));
            musicSelectionButton.setAlpha(1.0f);
            user.setMusic(true);

        }

    }

    public void fishingButtonClicked() {
        // this is to toggle between selection and non selection of button
        if (fishingSelectionButton.getAlpha() == 1.0f) {
            fishingSelectionButton.setAlpha(.5f);
            fishingSelectionButton.setBackgroundColor(Color.GRAY);
            user.setFishing(false);
        } else {
            fishingSelectionButton.setBackgroundColor(Color.parseColor("#FF4081"));
            fishingSelectionButton.setAlpha(1.0f);
            user.setFishing(true);

        }

    }

    public void init() {
        hobbiesContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterHobby.this, RegisterImage.class);
                intent.putExtra("classUser", user);
                startActivity(intent);
                finish();
            }
        });
    }


    //----------------------------------------Firebase----------------------------------------


}
