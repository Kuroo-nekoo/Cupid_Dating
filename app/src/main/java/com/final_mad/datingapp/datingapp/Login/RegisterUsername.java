package com.final_mad.datingapp.datingapp.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.final_mad.datingapp.datingapp.R;
import com.final_mad.datingapp.datingapp.Utils.User;

public class RegisterUsername extends AppCompatActivity {
    private EditText edUname;
    private Button btContinue;
    private TextView tvError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_username);
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("classUser");
        initWidgets();
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    String username = edUname.getText().toString();
                    user.setUsername(username);
                    Intent intent = new Intent(RegisterUsername.this, RegisterAge.class);
                    intent.putExtra("classUser", user);
                    startActivity(intent);
                }
            }
        });
    }

    private void initWidgets() {
        edUname = findViewById(R.id.edUname);
        btContinue = findViewById(R.id.btContinue);
        tvError = findViewById(R.id.tvError);
    }

    private boolean validate() {
        String Uname = edUname.getText().toString();
        if (Uname.equals("")) {
            tvError.setText("Please enter your username");
            return false;
        }
        return true;
    }
}