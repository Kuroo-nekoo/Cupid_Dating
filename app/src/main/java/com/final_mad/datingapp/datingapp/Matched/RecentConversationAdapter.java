package com.final_mad.datingapp.datingapp.Matched;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.final_mad.datingapp.datingapp.Utils.Constants;
import com.final_mad.datingapp.datingapp.Utils.User;
import com.final_mad.datingapp.datingapp.databinding.ItemContainerRecentConversationBinding;
import com.final_mad.datingapp.datingapp.models.ChatMessage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RecentConversationAdapter extends RecyclerView.Adapter<RecentConversationAdapter.ConversationViewHolder>{
    private final List<ChatMessage> chatMessageList;
    private Context mContext;
    private FirebaseFirestore firestore;

    public RecentConversationAdapter(List<ChatMessage> chatMessageList, Context mContext) {
        this.chatMessageList = chatMessageList;
        this.mContext = mContext;
        this.firestore = FirebaseFirestore.getInstance();
    }

    private Bitmap getConversationImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationViewHolder(
                ItemContainerRecentConversationBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        holder.setData(chatMessageList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        ItemContainerRecentConversationBinding binding;

        ConversationViewHolder(ItemContainerRecentConversationBinding itemContainerRecentConversationBinding) {
            super(itemContainerRecentConversationBinding.getRoot());
            binding = itemContainerRecentConversationBinding;
        }

        void setData (ChatMessage chatMessage) {
            binding.muiImage.setImageBitmap(getConversationImage(chatMessage.getConversationImage()));
            binding.tvName.setText(chatMessage.getConversationName());
            binding.tvRecentMessage.setText(chatMessage.getMessage());
            firestore.collection(Constants.KEY_COLLECTION_USERS).document(chatMessage.getConversationId()).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    User user = new User();
                                    user.setUsername(documentSnapshot.getString(Constants.KEY_USER_NAME));
                                    user.setEmail(documentSnapshot.getString(Constants.KEY_USER_EMAIL));
                                    user.setProfileImage(documentSnapshot.getString(Constants.KEY_USER_PROFILE_IMAGE));
                                    user.setToken(documentSnapshot.getString(Constants.KEY_FCM_TOKEN));
                                    double latitude = documentSnapshot.getDouble(Constants.KEY_USER_LATITUDE);
                                    double longitude = documentSnapshot.getDouble(Constants.KEY_USER_LONGITUDE);
                                    user.setLatitude(latitude);
                                    user.setLongitude(longitude);
                                    user.setAvailable(documentSnapshot.getLong(Constants.KEY_AVAILABILITY) == 1);
                                    user.setUser_id(documentSnapshot.getId());
                                    binding.getRoot().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                Intent intent = new Intent(mContext, ChatActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.putExtra(Constants.KEY_USER, user);
                                                mContext.startActivity(intent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                }
                            });
        }
    }
}
