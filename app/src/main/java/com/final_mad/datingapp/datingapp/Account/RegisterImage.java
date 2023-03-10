package com.final_mad.datingapp.datingapp.Account;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.final_mad.datingapp.datingapp.R;
import com.final_mad.datingapp.datingapp.Utils.User;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class RegisterImage extends AppCompatActivity {
    private ImageView ivAvatar;
    private Button btContinue;
    private User user;
    private String encodedImage;
    private TextView tvLinkLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_image);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("classUser");

        ivAvatar = findViewById(R.id.ivAvatar);
        btContinue = findViewById(R.id.btContinue);
        tvLinkLogin = findViewById(R.id.tvLinkLogin);

        tvLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterImage.this, Login.class));
            }
        });

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
            }
        });

        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(RegisterImage.this, RegisterEmailPassword.class);
                    user.setProfileImage(encodedImage);
                    intent.putExtra("classUser", user);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            encodedImage = encodeImage(bitmap);
                            ivAvatar.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
}