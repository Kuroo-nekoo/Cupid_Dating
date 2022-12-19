package com.final_mad.datingapp.datingapp.Matched;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.final_mad.datingapp.datingapp.Utils.Constants;
import com.final_mad.datingapp.datingapp.Utils.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BaseActivity extends AppCompatActivity {
    private DocumentReference documentReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            documentReference = firestore.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
        } catch (Exception e) {
            Log.d("test", e.getMessage());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {

        documentReference.update(Constants.KEY_AVAILABILITY, 0);
        } catch (Exception e) {
            Log.d("onPause: ", e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        documentReference.update(Constants.KEY_AVAILABILITY, 1);
    }
}
