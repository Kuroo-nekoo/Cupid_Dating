package com.final_mad.datingapp.datingapp.Login;

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

import com.final_mad.datingapp.datingapp.R;
import com.final_mad.datingapp.datingapp.Utils.Constants;
import com.final_mad.datingapp.datingapp.Utils.GPS;
import com.final_mad.datingapp.datingapp.Utils.PreferenceManager;
import com.final_mad.datingapp.datingapp.Utils.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterEmailPassword extends AppCompatActivity {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email_password);
        mContext = RegisterEmailPassword.this;
        preferenceManager = new PreferenceManager(getApplicationContext());
        Log.d(TAG, "onCreate: started");

        gps = new GPS(getApplicationContext());

        initWidgets();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("classUser");
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading(true);
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();
                confirmPassword = mConfirmPassword.getText().toString();
                Location location = gps.getLocation();


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

                    firestore.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            loading(false);
                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                            preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                            preferenceManager.putString(Constants.KEY_USER_NAME, user.getUsername().toString());
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
            }
        });


//                    Log.d("Location==>", longtitude + "   " + latitude);
//
//
//                    Intent intent = new Intent(RegisterBasicInfo.this, RegisterGender.class);
//                    User user = new User("", "", "", "", email, "username", false, false, false, false, "", "", "", latitude, longtitude);
//                    intent.putExtra("password", password);
//                    intent.putExtra("classUser", user);
//                    startActivity(intent);

    }

    private boolean checkInputs(String email, String password, String confirmPassword) {
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if (email.equals("") || confirmPassword.equals("") || password.equals("")) {
            Toast.makeText(mContext, "All fields must be filed out.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Below code checks if the email id is valid or not.
        if (!email.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(), "Invalid email address, enter valid email id and click on Continue", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.toString().equals(confirmPassword.toString())) {
            Toast.makeText(getApplicationContext(), "Password and confirm password mus be the same", Toast.LENGTH_SHORT).show();
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
}
