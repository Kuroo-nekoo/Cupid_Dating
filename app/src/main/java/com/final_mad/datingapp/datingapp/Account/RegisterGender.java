package com.final_mad.datingapp.datingapp.Account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.final_mad.datingapp.datingapp.R;
import com.final_mad.datingapp.datingapp.Utils.User;

public class RegisterGender extends AppCompatActivity {

    String password;
    User user;
    boolean male = true;
    private Button genderContinueButton;
    private Button maleSelectionButton;
    private Button femaleSelectionButton;
    private TextView tvLinkLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_gender);

        maleSelectionButton = findViewById(R.id.maleSelectionButton);
        femaleSelectionButton = findViewById(R.id.femaleSelectionButton);
        genderContinueButton = findViewById(R.id.genderContinueButton);
        tvLinkLogin = findViewById(R.id.tvLinkLogin);
        //By default male has to be selected so below code is added

        femaleSelectionButton.setAlpha(.5f);
        femaleSelectionButton.setBackgroundColor(Color.GRAY);


        maleSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleButtonSelected();
            }
        });

        femaleSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleButtonSelected();
            }
        });

        genderContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreferenceEntryPage();
            }
        });

        tvLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkToLogin();
            }
        });
    }

    public void maleButtonSelected() {
        male = true;
        maleSelectionButton.setBackgroundColor(Color.parseColor("#FF4081"));
        maleSelectionButton.setAlpha(1.0f);
        femaleSelectionButton.setAlpha(.5f);
        femaleSelectionButton.setBackgroundColor(Color.GRAY);
    }

    public void femaleButtonSelected() {
        male = false;
        femaleSelectionButton.setBackgroundColor(Color.parseColor("#FF4081"));
        femaleSelectionButton.setAlpha(1.0f);
        maleSelectionButton.setAlpha(.5f);
        maleSelectionButton.setBackgroundColor(Color.GRAY);
    }

    public void openPreferenceEntryPage() {
        User user = new User();
        String ownSex = male ? "male" : "female";
        user.setSex(ownSex);
        //set default photo
        String defaultPhoto = male ? "defaultMale" : "defaultFemale";

        Intent intent = new Intent(this, RegisterGenderPrefection.class);
        intent.putExtra("classUser", user);
        startActivity(intent);
    }

    private void linkToLogin() {
        startActivity(new Intent(getApplicationContext(), Login.class));
    }
}
