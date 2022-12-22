package com.final_mad.datingapp.datingapp.Matched;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.final_mad.datingapp.datingapp.Listeners.UserListener;
import com.final_mad.datingapp.datingapp.Utils.Constants;
import com.final_mad.datingapp.datingapp.Utils.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.final_mad.datingapp.datingapp.R;
import com.final_mad.datingapp.datingapp.Utils.TopNavigationViewHelper;
import com.final_mad.datingapp.datingapp.Utils.User;

import java.util.ArrayList;
import java.util.List;


public class Matched_Activity extends BaseActivity implements UserListener {

    private static final String TAG = "Matched_Activity";
    private static final int ACTIVITY_NUM = 3;
    List<Users> matchList = new ArrayList<>();
    List<User> copyList = new ArrayList<>();
    private Context mContext = Matched_Activity.this;
    private String userId, userSex, lookforSex;
    private EditText search;
    private List<Users> usersList = new ArrayList<>();
    private RecyclerView recyclerView, mRecyclerView;
    private ActiveUserAdapter adapter;
    private MatchUserAdapter mAdapter;
    private ProgressBar progressBar;
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);
        preferenceManager = new PreferenceManager(getApplicationContext());
        progressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.matche_recycler_view);

        getUsers();
        setupTopNavigationView();
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

    private void loading(Boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void getUsers() {
        loading(true);
        try {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection(Constants.KEY_COLLECTION_USERS)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            loading(false);
                            String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                            try {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    List<User> users = new ArrayList<>();
                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                        if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                            continue;
                                        }
                                        try {
                                            User user = new User();
                                            user.setSex(queryDocumentSnapshot.getString(Constants.KEY_USER_SEX));
                                            user.setDateOfBirth(queryDocumentSnapshot.getString(Constants.KEY_USER_DATA_OF_BIRTH));
                                            user.setUsername(queryDocumentSnapshot.getString(Constants.KEY_USER_NAME));
                                            user.setEmail(queryDocumentSnapshot.getString(Constants.KEY_USER_EMAIL));
                                            user.setProfileImage(queryDocumentSnapshot.getString(Constants.KEY_USER_PROFILE_IMAGE));
                                            user.setToken(queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN));
                                            user.setUser_id(queryDocumentSnapshot.getId());
                                            users.add(user);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    if (users.size() > 0) {
                                        MatchUserAdapter matchUserAdapter = new MatchUserAdapter(users, getApplicationContext(), Matched_Activity.this);
                                        mRecyclerView.setAdapter(matchUserAdapter);
                                        mRecyclerView.setVisibility(View.VISIBLE);
                                        matchUserAdapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}
