package com.final_mad.datingapp.datingapp.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.final_mad.datingapp.datingapp.R;
import com.final_mad.datingapp.datingapp.Utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ForgotPassword extends AppCompatActivity {
    private static final String TAG = "ForgotPasswordActivity";
    private Button btnRecover;
    private EditText edEmail;
    private TextView tvLinkRegister;
    private Context mContext;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    private TextView tvError;
    private FirebaseFirestore firestore;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        initWidgets();
        tvError.setText("");



        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading(true);
                String email = edEmail.getText().toString();
                if (validate(email)) {
                    try {
                        firestore.collection(Constants.KEY_COLLECTION_USERS).whereEqualTo(Constants.KEY_USER_EMAIL, edEmail.getText().toString())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocumentChanges().size() > 0) {
                                            Intent intent = new Intent(ForgotPassword.this, ResetPasswordActivity.class);
                                            intent.putExtra(Constants.KEY_USER_EMAIL, edEmail.getText().toString());
                                            intent.putExtra("documentId", task.getResult().getDocuments().get(0).getId());
                                            startActivity(intent);
                                        } else {
                                            tvError.setText("Email haven't registered, Please enter the different Email");
                                        }
                                        loading(false);
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
        progressBar = findViewById(R.id.progressBar);
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

    private void loading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnRecover.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            btnRecover.setVisibility(View.VISIBLE);
        }
    }
}