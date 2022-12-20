package com.final_mad.datingapp.datingapp.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.final_mad.datingapp.datingapp.Main.MainActivity;
import com.final_mad.datingapp.datingapp.R;
import com.final_mad.datingapp.datingapp.Utils.Constants;
import com.final_mad.datingapp.datingapp.Utils.PreferenceManager;
import com.final_mad.datingapp.datingapp.customfonts.ButtonSemiBold;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private Context mContext;
    private EditText mEmail, mPassword;
    private FirebaseAuth mAuth;
    private EditText user_email;
    private EditText user_password;
    private ButtonSemiBold btn_sign_in;
    private TextView sign_up;
    private TextView tvForgot;
    private ProgressBar progressBar;
    private PreferenceManager preferenceManager;
    FirebaseUser user;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        tvForgot = findViewById(R.id.tvForgotPassword);
        progressBar = findViewById(R.id.progressBar);
        btn_sign_in = findViewById(R.id.btn_login);
        mContext = Login.this;
        preferenceManager = new PreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        firestore = FirebaseFirestore.getInstance();
        init();
    }

    private boolean isStringNull(String string) {
        Log.d(TAG, "isStringNull: checking string if null.");

        return string.equals("");
    }

    //----------------------------------------Firebase----------------------------------------

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        //initialize the button for logging in

        btn_sign_in.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to log in");

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (isStringNull(email) || isStringNull(password)) {
                    Toast.makeText(mContext, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    firestore.collection(Constants.KEY_COLLECTION_USERS)
                            .whereEqualTo(Constants.KEY_USER_EMAIL, email)
                            .whereEqualTo(Constants.KEY_USER_PASSWORD, password)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    try {


                                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocumentChanges().size() > 0) {
                                            try {
                                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                                preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                                                preferenceManager.putString(Constants.KEY_USER_NAME, documentSnapshot.getString(Constants.KEY_USER_NAME));
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        } else {
                                            loading(false);
                                            Toast.makeText(getApplicationContext(), "Unable to sign in", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Login.this, ForgotPassword.class));
                } catch (Exception e) {
                    String test = e.getMessage().toString();
                }
            }
        });

        TextView linkSignUp = findViewById(R.id.link_signup);
        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to register screen");
                Intent intent = new Intent(Login.this, RegisterGender.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public void onBackPressed() {

    }

    private void loading (Boolean isLoading) {
        if(isLoading) {
            btn_sign_in.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            btn_sign_in.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

}
