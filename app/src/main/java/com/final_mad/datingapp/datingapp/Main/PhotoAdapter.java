package com.final_mad.datingapp.datingapp.Main;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.final_mad.datingapp.datingapp.R;
import com.final_mad.datingapp.datingapp.Utils.CalculateAge;
import com.final_mad.datingapp.datingapp.Utils.User;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;



public class PhotoAdapter extends ArrayAdapter<User> {
    Context mContext;
    private ArrayList<User> userArrayList;

    public PhotoAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> objects) {
        super(context, resource, objects);
        this.mContext = context;
        userArrayList = objects;
    }

    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        if (encodedImage != null) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return userArrayList.size();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            final User user = getItem(position);
            CalculateAge calculateAge = new CalculateAge(user.getDateOfBirth());

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
            }

            TextView name = convertView.findViewById(R.id.name);
            ImageView image = convertView.findViewById(R.id.image);
            ImageButton btnInfo = convertView.findViewById(R.id.checkInfoBeforeMatched);
            if (!user.isNotShowAge()) {
                name.setText(user.getUsername() + ", " + Integer.toString(calculateAge.getAge()));
            } else {
                name.setText(user.getUsername());
            }
            btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProfileCheckinMain.class);
                    CalculateAge calculateAge = new CalculateAge(user.getDateOfBirth());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        intent.putExtra("name", user.getUsername() + ", " + Integer.toString(calculateAge.getAge()));
                    }
                    intent.putExtra("photo", user.getProfileImage());
                    intent.putExtra("bio", user.getUsername());
                    intent.putExtra("interest", user.getUsername());
                    intent.putExtra("distance", user.getDistance());
                    mContext.startActivity(intent);
                }
            });

            Glide.with(getContext()).asBitmap().load(getBitmapFromEncodedString(user.getProfileImage())).into(image);

            return convertView;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
