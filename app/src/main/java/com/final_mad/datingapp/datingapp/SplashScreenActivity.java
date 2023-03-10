package com.final_mad.datingapp.datingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.final_mad.datingapp.datingapp.Account.Login;
import com.final_mad.datingapp.datingapp.Utils.PreferenceManager;

import java.util.Timer;
import java.util.TimerTask;

import static com.final_mad.datingapp.datingapp.Utils.TransparentStatusBar.setWindowFlag;


public class SplashScreenActivity extends AppCompatActivity {
    ImageView imageView;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferenceManager = new PreferenceManager(getApplicationContext());
        preferenceManager.clear();
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_splash_screen);

        ScaleAnimation fade_in =  new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(3000);     // animation duration in milliseconds
        fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
        findViewById(R.id.imageView).startAnimation(fade_in);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                   startActivity(new Intent(SplashScreenActivity.this, Login.class));
                } catch (Exception e) {
                    Log.e("test", e.getMessage());
                }
            }
        }, 1000);


    }

}