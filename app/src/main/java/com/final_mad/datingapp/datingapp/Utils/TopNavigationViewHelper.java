package com.final_mad.datingapp.datingapp.Utils;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.final_mad.datingapp.datingapp.Matched.RecentConversationActivity;
import com.final_mad.datingapp.datingapp.Nearby.NearbyActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.final_mad.datingapp.datingapp.Main.MainActivity;
import com.final_mad.datingapp.datingapp.Matched.Matched_Activity;
import com.final_mad.datingapp.datingapp.Profile.Profile_Activity;
import com.final_mad.datingapp.datingapp.R;



public class TopNavigationViewHelper {

    private static final String TAG = "TopNavigationViewHelper";

    public static void setupTopNavigationView(BottomNavigationViewEx tv) {
        Log.d(TAG, "setupTopNavigationView: setting up navigationview");


    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_profile:
                        Intent intent2 = new Intent(context, Profile_Activity.class);
                        context.startActivity(intent2);
                        break;

                    case R.id.ic_main:
                        Intent intent1 = new Intent(context, MainActivity.class);
                        context.startActivity(intent1);
                        break;

                    case R.id.ic_matched:
                        Intent intent3 = new Intent(context, Matched_Activity.class);
                        context.startActivity(intent3);
                        break;
                    case R.id.ic_recent:
                        Intent intent4 = new Intent(context, RecentConversationActivity.class);
                        context.startActivity(intent4);
                        break;

                    case R.id.ic_nearby:
                        Intent intent5 = new Intent(context, NearbyActivity.class);
                        context.startActivity(intent5);
                        break;
                }

                return false;
            }
        });
    }
}
