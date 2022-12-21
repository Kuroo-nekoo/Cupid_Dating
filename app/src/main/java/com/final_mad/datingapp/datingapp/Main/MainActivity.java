package com.final_mad.datingapp.datingapp.Main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.final_mad.datingapp.datingapp.Matched.ChatActivity;
import com.final_mad.datingapp.datingapp.Matched.MatchUserAdapter;
import com.final_mad.datingapp.datingapp.Matched.Matched_Activity;
import com.final_mad.datingapp.datingapp.Nearby.NearbyAdapter;
import com.final_mad.datingapp.datingapp.Utils.Constants;
import com.final_mad.datingapp.datingapp.Utils.PreferenceManager;
import com.final_mad.datingapp.datingapp.Utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.final_mad.datingapp.datingapp.R;
import com.final_mad.datingapp.datingapp.Utils.PulsatorLayout;
import com.final_mad.datingapp.datingapp.Utils.TopNavigationViewHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final int ACTIVITY_NUM = 2;
    final private int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    ListView listView;
    List<Cards> rowItems;
    FrameLayout cardFrame, moreFrame;
    private Context mContext = MainActivity.this;
    private NotificationHelper mNotificationHelper;
    private Cards cards_data[];
    private PhotoAdapter arrayAdapter;
    private FirebaseFirestore firestore;
    private PreferenceManager preferenceManager;
    private ArrayList<User> userList;
    private PhotoAdapter2 photoAdapter;
    private SwipeFlingAdapterView flingContainer;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            userList = new ArrayList<>();
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
            firestore = FirebaseFirestore.getInstance();
            preferenceManager = new PreferenceManager(getApplicationContext());
            cardFrame = findViewById(R.id.card_frame);
            moreFrame = findViewById(R.id.more_frame);
            progressBar = findViewById(R.id.progressBar);
            PulsatorLayout mPulsator = findViewById(R.id.pulsator);
            mPulsator.start();
            mNotificationHelper = new NotificationHelper(this);
            loading(true);
            setupTopNavigationView();
            getToken();

            // start pulsator







        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkRowItem() {
        if (userList.isEmpty()) {
            moreFrame.setVisibility(View.VISIBLE);
            cardFrame.setVisibility(View.GONE);
        }
    }

    private void updateLocation() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        updateLocation();
                    } else {
                        Toast.makeText(MainActivity.this, "Location Permission Denied. You have to give permission inorder to know the user range ", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void updateSwipeCard() {
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                userList.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                User obj = (User) dataObject;
                HashMap<String, String> test = new HashMap<>();
                test.put("Test", "test");
                checkRowItem();

            }

            @Override
            public void onRightCardExit(Object dataObject) {
                User obj = (User) dataObject;
                String userId = obj.getUser_id();

                //check matches
                checkRowItem();
                DocumentReference documentReference = firestore.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
                documentReference.collection("matchedUser").whereEqualTo("user_id", userId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocumentChanges().size() == 0) {
                                    documentReference.collection("matchedUser").document(userId).set(obj);
                                }
                            }
                        });
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here


            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void sendNotification() {
        NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification(mContext.getString(R.string.app_name), mContext.getString(R.string.match_notification));

        mNotificationHelper.getManager().notify(1, nb.build());
    }


    public void DislikeBtn(View v) {
        if (userList.size() != 0) {
            User user = userList.get(0);

            String userId = user.getUser_id();

            userList.remove(0);
            arrayAdapter.notifyDataSetChanged();


        }
    }

    public void LikeBtn(View v) {
        if (userList.size() != 0) {
            User user = userList.get(0);

            String userId = user.getUser_id();

            //check matches

            userList.remove(0);
            arrayAdapter.notifyDataSetChanged();

            Intent btnClick = new Intent(mContext, BtnLikeActivity.class);
            btnClick.putExtra("url", user.getProfileImage());
            startActivity(btnClick);
        }
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


    @Override
    public void onBackPressed() {

    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        DocumentReference documentReference = firestore.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        showToast("Token updated successfully");
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
                                                user.setDateOfBirth(queryDocumentSnapshot.getString(Constants.KEY_USER_DATA_OF_BIRTH));
                                                user.setUser_id(queryDocumentSnapshot.getId());
                                                userList.add(user);
                                            }
                                            arrayAdapter = new PhotoAdapter(MainActivity.this, R.layout.item, userList);
                                            flingContainer = findViewById(R.id.frame);
                                            flingContainer.setAdapter(arrayAdapter);
                                            arrayAdapter.notifyDataSetChanged();
                                            loading(false);

                                            checkRowItem();
                                            updateSwipeCard();
                                        }
                                    }
                                });
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Unable to update token");
                    }
                });
    }

    private void loading(boolean isLoading) {
        if(isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            cardFrame.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            cardFrame.setVisibility(View.VISIBLE);
        }
    }


}
