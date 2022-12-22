package com.final_mad.datingapp.datingapp.Account;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.final_mad.datingapp.datingapp.Listeners.CheckEmailListener;
import com.final_mad.datingapp.datingapp.R;
import com.final_mad.datingapp.datingapp.Utils.Constants;
import com.final_mad.datingapp.datingapp.Utils.GPS;
import com.final_mad.datingapp.datingapp.Utils.PreferenceManager;
import com.final_mad.datingapp.datingapp.Utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class RegisterEmailPassword extends AppCompatActivity implements CheckEmailListener {
    private static final String TAG = "RegisterActivity";
    GPS gps;
    private Context mContext;
    private String email, password, confirmPassword;
    private EditText mEmail, mPassword, mConfirmPassword;
    private TextView loadingPleaseWait, tvError;
    private Button btnRegister;
    private String append = "";
    private FirebaseAuth mAuth;
    private User user;
    private FirebaseFirestore firestore;
    private PreferenceManager preferenceManager;
    private ProgressBar progressBar;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register_email_password);
            mContext = RegisterEmailPassword.this;
            preferenceManager = new PreferenceManager(getApplicationContext());
            Log.d(TAG, "onCreate: started");
            gps = new GPS(getApplicationContext());
            initWidgets();
            init();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("classUser");
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        tvError.setText("");
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading(true);
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();
                confirmPassword = mConfirmPassword.getText().toString();
                Location location = gps.getLocation();
                firestore.collection(Constants.KEY_COLLECTION_USERS).whereEqualTo("email", email).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocumentChanges().size() > 0) {
                                    loading(false);
                                    tvError.setText("Already use email");
                                    return;
                                }
                                if (checkInputs(email, password, confirmPassword)) {
                                    email = mEmail.getText().toString();
                                    password = mPassword.getText().toString();


                                    double latitude = 10.518650;
                                    double longitude = 105.232930;
                                    if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                    }

                                    user.setEmail(email);
                                    user.setPassword(password);
                                    user.setLatitude(latitude);
                                    user.setLongitude(longitude);
                                    user.setNotShowAge(false);
                                    user.setNotShowDistance(false);
                                    user.setMinAge(16);
                                    user.setMaxAge(50);
                                    user.setMaxDistance(20);

                                    firestore.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            loading(false);
                                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                            preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                                            preferenceManager.putString(Constants.KEY_USER_NAME, user.getUsername().toString());
                                            String image2 = user.getProfileImage();
                                            preferenceManager.putString(Constants.KEY_USER_PROFILE_IMAGE, user.getProfileImage());
                                            preferenceManager.putString(Constants.KEY_USER_SEX, user.getSex());
                                            preferenceManager.putString(Constants.KEY_USER_PREFER_SEX, user.getPreferSex());
                                            String image = preferenceManager.getString(Constants.KEY_USER_PROFILE_IMAGE);
                                            preferenceManager.putInt(Constants.KEY_USER_MIN_AGE, user.getMinAge());
                                            preferenceManager.putInt(Constants.KEY_USER_MAX_AGE, user.getMaxAge());
                                            preferenceManager.putInt(Constants.KEY_USER_MAX_DISTANCE, user.getMaxDistance());
                                            Intent intent = new Intent(RegisterEmailPassword.this, Login.class);
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            loading(false);
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                                else {
                                    loading(false);
                                }
                            }
                        });
            }
        });

    }






    private boolean checkInputs(String email, String password, String confirmPassword) {

        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if (email.equals("")) {
            tvError.setText("Email must be provide");
            return false;
        }

        if (password.equals("")) {
            tvError.setText("Password must be provide");
            return false;
        }

        if (confirmPassword.equals("")) {
            tvError.setText("Confirm password must be provide");
        }

        if (!email.matches(emailPattern)) {
            tvError.setText("Invalid email address, please enter valid email");
            return false;
        }

        if (!password.toString().equals(confirmPassword.toString())) {
            tvError.setText("Password and confirm password must be the same");
            return false;
        }

        if (password.toString().length() <= 6) {
            tvError.setText("Password must be longer then 6 character");
            return false;
        }

        return true;
    }

    private void initWidgets() {
        Log.d(TAG, "initWidgets: initializing widgets");
        mEmail = findViewById(R.id.input_email);
        btnRegister = findViewById(R.id.btn_register);
        mPassword = findViewById(R.id.input_password);
        mConfirmPassword = findViewById(R.id.input_password_confirm);
        mContext = RegisterEmailPassword.this;
        tvError = findViewById(R.id.tvError);
        progressBar = findViewById(R.id.progressBar);
    }

    public void onLoginClicked(View view) {
        startActivity(new Intent(getApplicationContext(), Login.class));

    }

    private void loading(Boolean isLoading) {
        if(isLoading) {
            btnRegister.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            btnRegister.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean onCheckEmail(boolean isAlreadyUse) {
        return false;
    }
}
