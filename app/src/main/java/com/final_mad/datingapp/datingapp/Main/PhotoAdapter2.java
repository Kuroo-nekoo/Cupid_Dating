package com.final_mad.datingapp.datingapp.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.final_mad.datingapp.datingapp.Utils.User;
import com.final_mad.datingapp.datingapp.databinding.ItemBinding;

import java.lang.reflect.GenericSignatureFormatError;
import java.util.List;


public class PhotoAdapter2 extends RecyclerView.Adapter<PhotoAdapter2.PhotoViewHolder> {
    private Context mContext;
    private List<User> userList;

    public PhotoAdapter2(Context mContext, List<User> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        if (encodedImage != null) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        ItemBinding binding;
        public PhotoViewHolder(ItemBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }

        public void setData(User user) {
            binding.name.setText(user.getUsername());
            binding.checkInfoBeforeMatched.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProfileCheckinMain.class);
                    intent.putExtra("name", user.getUsername() + ", " + user.getDateOfBirth());
                    intent.putExtra("photo", user.getProfileImage());
                    intent.putExtra("bio", user.getUsername());
                    intent.putExtra("interest", user.getUsername());
                    intent.putExtra("distance", user.getDistance());
                    mContext.startActivity(intent);
                    Glide.with(mContext).asBitmap().load(getBitmapFromEncodedString(user.getProfileImage())).into(binding.image);
                }
            });
        }


    }
}
