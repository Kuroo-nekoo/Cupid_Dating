package com.final_mad.datingapp.datingapp.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.final_mad.datingapp.datingapp.R;


public class ProfileCheckinMain extends AppCompatActivity {

    private Context mContext;
    String profileImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_checkin_main);

        mContext = ProfileCheckinMain.this;

       /* ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
*/

        TextView profileName = findViewById(R.id.name_main);
        ImageView profileImage = findViewById(R.id.profileImage);
        TextView profileBio = findViewById(R.id.bio_beforematch);
        TextView profileInterest = findViewById(R.id.interests_beforematch);
        TextView profileDistance = findViewById(R.id.distance_main);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String bio = intent.getStringExtra("bio");
        String interest = intent.getStringExtra("interest");
        String profileImageString = intent.getStringExtra("photo");
        int distance = intent.getIntExtra("distance", 1);
        String append = (distance == 1) ? "mile away" : "miles away";

        profileDistance.setText(distance + " " + append);
        profileName.setText(name);
        profileBio.setText(bio);
        profileInterest.setText(interest);

        profileImage.setImageBitmap(getBitmapFromEncodedString(profileImageString));
    }

    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        if (encodedImage != null) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }


    public void DislikeBtn(View v) {

            Intent btnClick = new Intent(mContext, BtnDislikeActivity.class);
            btnClick.putExtra("url", profileImageUrl);
            startActivity(btnClick);

    }

    public void LikeBtn(View v) {
            Intent btnClick = new Intent(mContext, BtnLikeActivity.class);
            btnClick.putExtra("url", profileImageUrl);
            startActivity(btnClick);

    }

    public void onCloseClicked(View view) {
        Intent btnClick = new Intent(mContext, MainActivity.class);
        btnClick.putExtra("url", profileImageUrl);
        startActivity(btnClick);
    }
}
