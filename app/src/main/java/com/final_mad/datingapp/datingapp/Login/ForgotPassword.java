package com.final_mad.datingapp.datingapp.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.final_mad.datingapp.datingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private static final String TAG = "ForgotPasswordActivity";
    private Button btnRecover;
    private EditText edEmail;
    private TextView tvLinkRegister;
    private Context mContext;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();
        initWidgets();



        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edEmail.getText().toString();
                if (validate(email)) {
                    try {
                        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    startActivity(new Intent(ForgotPassword.this, Login.class));
                                } else {
                                    tvError.setText("Email do not exist");
                                }
                            }
                        });
                    } catch (Exception e) {
                        Log.w("test", e.getMessage());
                    }
                }
            }
        });

        tvLinkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassword.this, Login.class));
            }
        });
    }

    private void initWidgets() {
        btnRecover = findViewById(R.id.btnRecover);
        edEmail = findViewById(R.id.edEmail);
        tvError = findViewById(R.id.tvError);
        tvLinkRegister = findViewById(R.id.tvLinkRegister);
    }

    private boolean validate(String email) {
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if (email.equals("")) {
            Toast.makeText(mContext, "All fields must be filed out.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Below code checks if the email id is valid or not.
        if (!email.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(), "Invalid email address, enter valid email id and click on Continue", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}