package com.final_mad.datingapp.datingapp.Matched;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.final_mad.datingapp.datingapp.databinding.ItemContainerReceiveMessageBinding;
import com.final_mad.datingapp.datingapp.databinding.ItemContainerSentMessageBinding;
import com.final_mad.datingapp.datingapp.models.ChatMessage;

import java.util.List;

public class ChatAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Bitmap receiverProfileImage;
    private final List<ChatMessage> chatMessageList;
    private final String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public void setReceiverProfileImage(Bitmap bitmap) {
        receiverProfileImage = bitmap;
    }

    public ChatAdapter( List<ChatMessage> chatMessageList,Bitmap receiverProfileImage, String senderId) {
        this.receiverProfileImage = receiverProfileImage;
        this.chatMessageList = chatMessageList;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(ItemContainerSentMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new ReceivedMessageViewHolder(ItemContainerReceiveMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setData(chatMessageList.get(position));
        } else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessageList.get(position), receiverProfileImage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessageList.get(position).getSenderId().equals((senderId))) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerSentMessageBinding binding;


        public SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding)   {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(ChatMessage chatMessage) {
            binding.tvMessage.setText(chatMessage.getMessage());
            binding.tvDateTime.setText(chatMessage.getDateTime());
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerReceiveMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceiveMessageBinding itemContainerReceiveMessageBinding) {
            super(itemContainerReceiveMessageBinding.getRoot());
            binding = itemContainerReceiveMessageBinding;
        }

        void setData(ChatMessage chatMessage, Bitmap receiverProfileImage) {
            binding.tvMessage.setText(chatMessage.getMessage());
            binding.tvDateTime.setText(chatMessage.getDateTime());
            if (receiverProfileImage != null) {
                binding.ivProfile.setImageBitmap(receiverProfileImage);
            }
        }
    }
}
