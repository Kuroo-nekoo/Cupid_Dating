package com.final_mad.datingapp.datingapp.Nearby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.final_mad.datingapp.datingapp.R;
import com.final_mad.datingapp.datingapp.Utils.Constants;
import com.final_mad.datingapp.datingapp.Utils.GPS;
import com.final_mad.datingapp.datingapp.Utils.PreferenceManager;
import com.final_mad.datingapp.datingapp.Utils.TopNavigationViewHelper;
import com.final_mad.datingapp.datingapp.Utils.User;
import com.final_mad.datingapp.datingapp.databinding.ActivityNearbyBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class NearbyActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private PreferenceManager preferenceManager;
    private List<User> userList;
    private Context mContext = NearbyActivity.this;
    private int ACTIVITY_NUM = 1;
    private Double currLatitude, currLongitude;
    private GPS gps;
    private ActivityNearbyBinding binding;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNearbyBinding.inflate(getLayoutInflater());
        loading(true);
        setContentView(binding.getRoot());
        init();
        setupTopNavigationView();
    }

    private void getAllUser() {
        firestore.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                    continue;
                                }
                                User user = new User();
                                user.setUsername(queryDocumentSnapshot.getString(Constants.KEY_USER_NAME));
                                user.setEmail(queryDocumentSnapshot.getString(Constants.KEY_USER_EMAIL));
                                user.setProfileImage(queryDocumentSnapshot.getString(Constants.KEY_USER_PROFILE_IMAGE));
                                user.setToken(queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN));
                                double latitude = queryDocumentSnapshot.getDouble(Constants.KEY_USER_LATITUDE);
                                double longitude = queryDocumentSnapshot.getDouble(Constants.KEY_USER_LONGITUDE);
                                user.setLatitude(latitude);
                                user.setLongitude(longitude);
                                user.setAvailable(queryDocumentSnapshot.getBoolean(Constants.KEY_AVAILABILITY));
                                user.setNotShowAge(queryDocumentSnapshot.getBoolean("notShowAge"));
                                user.setNotShowDistance(queryDocumentSnapshot.getBoolean("notShowDistance"));
                                Toast.makeText(getApplicationContext(), "distance" +  Double.toString(gps.calculateDistance(currLatitude, currLongitude,latitude, longitude)), Toast.LENGTH_LONG).show();

                                if(gps.calculateDistance(currLatitude, currLongitude,latitude, longitude) < 10 && gps.calculateDistance(currLatitude, currLongitude,latitude, longitude) > 1) {
                                    try {
                                        user.setDistance(gps.calculateDistance(currLatitude, currLongitude,latitude, longitude));
                                        user.setUser_id(queryDocumentSnapshot.getId());
                                        userList.add(user);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            NearbyAdapter nearbyAdapter = new NearbyAdapter(userList);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
                            binding.rcvNearbyUser.setLayoutManager(gridLayoutManager);
                            binding.rcvNearbyUser.setAdapter(nearbyAdapter);
                            nearbyAdapter.notifyDataSetChanged();
                            loading(false);
                        }
                    }
                });
    }

    private void init () {
        firestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());
        userList = new ArrayList<>();
        gps = new GPS(getApplicationContext());
        firestore.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID)).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        currLatitude = documentSnapshot.getDouble(Constants.KEY_USER_LATITUDE);
                        currLongitude= documentSnapshot.getDouble(Constants.KEY_USER_LONGITUDE);
                        getAllUser();

                    }
                });

    }

    private void setupTopNavigationView() {
        BottomNavigationViewEx tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void loading(boolean isLoading) {
        if(isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.rcvNearbyUser.setVisibility(View.INVISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.rcvNearbyUser.setVisibility(View.VISIBLE);
        }
    }

}