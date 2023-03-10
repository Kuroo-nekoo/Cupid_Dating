package com.final_mad.datingapp.datingapp.Profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.final_mad.datingapp.datingapp.Introduction.IntroductionMain;
import com.final_mad.datingapp.datingapp.Account.Login;
import com.final_mad.datingapp.datingapp.R;
import com.final_mad.datingapp.datingapp.Utils.Constants;
import com.final_mad.datingapp.datingapp.Utils.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";
    private SeekBar distance;
    private SwitchCompat man, woman;
    private RangeSeekBar rangeSeekBar;
    private TextView gender, distance_text, age_rnge, tvLogout, tvApply;
    private FirebaseFirestore firestore;
    private PreferenceManager preferenceManager;
    private int distanceValue;
    private int minAge, maxAge;
    private String preferSex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        TextView toolbar = findViewById(R.id.toolbartag);
        toolbar.setText("Profile");
        firestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());
        ImageButton back = findViewById(R.id.back);
        distance = findViewById(R.id.distance);
        man = findViewById(R.id.switch_man);
        woman = findViewById(R.id.switch_woman);
        distance_text = findViewById(R.id.distance_text);
        age_rnge = findViewById(R.id.age_range);
        rangeSeekBar = findViewById(R.id.rangeSeekbar);
        tvLogout = findViewById(R.id.tvLogout);
        tvApply = findViewById(R.id.tvApply);
        gender = findViewById(R.id.gender_text);

        minAge = preferenceManager.getInt(Constants.KEY_USER_MIN_AGE);
        maxAge = preferenceManager.getInt(Constants.KEY_USER_MAX_AGE);
        gender.setText(preferenceManager.getString(Constants.KEY_USER_PREFER_SEX).equals("male") ? "Men" : "Women");
        distance.setProgress(preferenceManager.getInt(Constants.KEY_USER_MAX_DISTANCE));
        rangeSeekBar.setSelectedMinValue(preferenceManager.getInt(Constants.KEY_USER_MIN_AGE));
        distance_text.setText(Integer.toString(preferenceManager.getInt(Constants.KEY_USER_MAX_DISTANCE)) + " Km");
        rangeSeekBar.setSelectedMaxValue(preferenceManager.getInt(Constants.KEY_USER_MAX_AGE));
        woman.setChecked(preferenceManager.getString(Constants.KEY_USER_PREFER_SEX).equals("female"));
        man.setChecked(preferenceManager.getString(Constants.KEY_USER_PREFER_SEX).equals("male"));
        age_rnge.setText(Integer.toString(preferenceManager.getInt(Constants.KEY_USER_MIN_AGE)) + "-" + Integer.toString(preferenceManager.getInt(Constants.KEY_USER_MAX_AGE)));

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });


        distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance_text.setText(progress + " Km");
                distanceValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                firestore.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID))
                        .update(Constants.KEY_USER_MAX_DISTANCE, distanceValue);
                preferenceManager.putInt(Constants.KEY_USER_MAX_DISTANCE, distanceValue);
            }
        });

        man.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    man.setChecked(true);
                    woman.setChecked(false);
                    gender.setText("Men");
                    firestore.collection(Constants.KEY_USER).document(preferenceManager.getString(Constants.KEY_USER_ID))
                            .update(Constants.KEY_USER_PREFER_SEX, "male");
                    preferenceManager.putString(Constants.KEY_USER_PREFER_SEX, "male");
                }
            }
        });
        woman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    woman.setChecked(true);
                    man.setChecked(false);
                    gender.setText("Women");
                    firestore.collection(Constants.KEY_USER).document(preferenceManager.getString(Constants.KEY_USER_ID))
                            .update(Constants.KEY_USER_PREFER_SEX, "female");
                    preferenceManager.putString(Constants.KEY_USER_PREFER_SEX, "female");
                }
            }
        });
        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                age_rnge.setText(minValue + "-" + maxValue);
                minAge = (int) minValue;
                maxAge = (int) maxValue;
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID))
                        .update("minAge", minAge);
                firestore.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID))
                        .update("maxAge", maxAge);
                preferenceManager.putInt(Constants.KEY_USER_MIN_AGE, minAge);
                preferenceManager.putInt(Constants.KEY_USER_MAX_AGE, maxAge);

            }
        });
    }

    private void ShowToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    private void signOut() {
        ShowToast("Signing out...");
        DocumentReference documentReference = firestore.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        preferenceManager.clear();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ShowToast("Unable to sign out");
                    }
                });
    }
}
