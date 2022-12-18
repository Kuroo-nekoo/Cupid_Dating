package com.final_mad.datingapp.datingapp.Matched;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.final_mad.datingapp.datingapp.Listeners.UserListener;
import com.final_mad.datingapp.datingapp.R;
import com.final_mad.datingapp.datingapp.Utils.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class MatchUserAdapter extends RecyclerView.Adapter<MatchUserAdapter.MyViewHolder> {
    List<User> usersList;
    Context context;
    private final UserListener userListener;

    public MatchUserAdapter(List<User> usersList, Context context, UserListener userListener) {
        this.usersList = usersList;
        this.context = context;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public MatchUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.matched_user_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchUserAdapter.MyViewHolder holder, int position) {
        User user = usersList.get(position);
        holder.name.setText(user.getUsername());
        holder.profession.setText(user.getUsername());
        holder.imageView.setImageBitmap(getUserImage(user.getProfileImage()));
        holder.llMatchUserItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 userListener.onUserClicked(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView name, profession;
        LinearLayout llMatchUserItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mui_image);
            name = itemView.findViewById(R.id.mui_name);
            profession = itemView.findViewById(R.id.mui_profession);
            llMatchUserItem = itemView.findViewById(R.id.llMatchedUserItem);
        }
    }

    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
