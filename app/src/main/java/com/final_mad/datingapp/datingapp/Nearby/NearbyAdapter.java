package com.final_mad.datingapp.datingapp.Nearby;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.final_mad.datingapp.datingapp.Utils.User;
import com.final_mad.datingapp.datingapp.databinding.ItemContainerNearyUserBinding;

import java.util.List;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.NearbyViewHolder> {
    private List<User> userList;

    public NearbyAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public NearbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NearbyViewHolder(ItemContainerNearyUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyViewHolder holder, int position) {
        holder.setData(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class NearbyViewHolder extends RecyclerView.ViewHolder {
        private ItemContainerNearyUserBinding binding;

        public NearbyViewHolder(ItemContainerNearyUserBinding itemContainerNearyUserBinding) {
            super(itemContainerNearyUserBinding.getRoot());
            binding = itemContainerNearyUserBinding;
        }

        void setData(User user) {
            if (user.getProfileImage() != null) {
                binding.imAvatar.setImageBitmap(getBitmapFromEncodedString(user.getProfileImage()));
            }
            binding.tvDistance.setText(Double.toString(user.getDistance()) + " km");
            binding.tvName.setText(user.getUsername());
        }
    }

    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        if (encodedImage != null) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }
}
