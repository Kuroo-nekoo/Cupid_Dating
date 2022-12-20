package com.final_mad.datingapp.datingapp.Matched;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.final_mad.datingapp.datingapp.Utils.Constants;
import com.final_mad.datingapp.datingapp.Utils.GPS;
import com.final_mad.datingapp.datingapp.Utils.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BaseActivity extends AppCompatActivity {
    private DocumentReference documentReference;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            preferenceManager = new PreferenceManager(getApplicationContext());
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            documentReference = firestore.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
            GPS gps = new GPS(getApplicationContext());
            Location location = gps.getLocation();

        } catch (Exception e) {
            Log.d("test", e.getMessage());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            documentReference.update(Constants.KEY_AVAILABILITY, 0);
            GPS gps = new GPS(getApplicationContext());
            Location location = gps.getLocation();
            documentReference.update(Constants.KEY_USER_LATITUDE, location.getLatitude());
            documentReference.update(Constants.KEY_USER_LONGITUDE, location.getLongitude());
        } catch (Exception e) {
            Log.d("onPause: ", e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        documentReference.update(Constants.KEY_AVAILABILITY, 1);
        GPS gps = new GPS(getApplicationContext());
        Location location = gps.getLocation();
        documentReference.update(Constants.KEY_USER_LATITUDE, location.getLatitude());
        documentReference.update(Constants.KEY_USER_LONGITUDE, location.getLongitude());

    }
}
