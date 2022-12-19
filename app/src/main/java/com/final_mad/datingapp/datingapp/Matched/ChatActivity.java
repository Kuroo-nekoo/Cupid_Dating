package com.final_mad.datingapp.datingapp.Matched;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.final_mad.datingapp.datingapp.Utils.Constants;
import com.final_mad.datingapp.datingapp.Utils.PreferenceManager;
import com.final_mad.datingapp.datingapp.Utils.User;
import com.final_mad.datingapp.datingapp.databinding.ActivityChatBinding;
import com.final_mad.datingapp.datingapp.models.ChatMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChatActivity extends BaseActivity {
    private ActivityChatBinding binding;
    private User receiveUser;
    private List<ChatMessage> chatMessageList;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore firestore;
    private String conversationId = null;
    private Boolean isReceiverAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
        loadReceiverDetails();
        init();
        listenMessages();
    }

    private void listenAvailabilityOfReceiver() {
        firestore.collection(Constants.KEY_COLLECTION_USERS).document(receiveUser.getUser_id())
                .addSnapshotListener(ChatActivity.this, ((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (value != null) {
                        if (value.getLong(Constants.KEY_AVAILABILITY) != null) {
                            int availability = Objects.requireNonNull(
                                    value.getLong(Constants.KEY_AVAILABILITY)
                            ).intValue();
                            isReceiverAvailable = availability == 1;
                        }
                        receiveUser.setToken(value.getString(Constants.KEY_FCM_TOKEN));
                    }
                    if (isReceiverAvailable) {
                        binding.tvAvailability.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvAvailability.setVisibility(View.GONE);
                    }
                }));
    }

    private void loadReceiverDetails() {
        receiveUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.tvName.setText(receiveUser.getUsername());
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void init () {
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessageList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rcvChat.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(
                chatMessageList,
                getBitmapFromEncodedString(receiveUser.getProfileImage()),
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        binding.rcvChat.setAdapter(chatAdapter);
        firestore = FirebaseFirestore.getInstance();
    }

    private void sendMessage() {
        try {
            HashMap<String, Object> message = new HashMap<>();
            message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
            message.put(Constants.KEY_RECEIVE_ID, receiveUser.getUser_id());
            message.put(Constants.KEY_MESSAGE, binding.etInputMessage.getText().toString());
            message.put(Constants.KEY_TIMESTAMP, new Date());
            firestore.collection(Constants.KEY_COLLECTION_CHAT).add(message);
            if (conversationId != null) {
                updateConversation(binding.etInputMessage.getText().toString());
            } else {
                HashMap<String, Object> conversation = new HashMap<>();
                conversation.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                conversation.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_SENDER_NAME));
                conversation.put(Constants.KEY_SENDER_IMAGE, preferenceManager.getString(Constants.KEY_USER_PROFILE_IMAGE));
                conversation.put(Constants.KEY_RECEIVE_ID, receiveUser.getUser_id());
                conversation.put(Constants.KEY_RECEIVER_NAME, receiveUser.getUsername());
                conversation.put(Constants.KEY_RECEIVER_IMAGE, receiveUser.getProfileImage());
                conversation.put(Constants.KEY_LAST_MESSAGE, binding.etInputMessage.getText().toString());
                conversation.put(Constants.KEY_TIMESTAMP, new Date());
                addConversation(conversation );
            }
            binding.etInputMessage.setText(null);
        } catch (Exception e) {
            Log.d("Send Messge", e.getMessage());
        }

    }

    private void listenMessages() {
        firestore.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVE_ID, receiveUser.getUser_id())
                .addSnapshotListener(eventListener);
        firestore.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiveUser.getUser_id())
                .whereEqualTo(Constants.KEY_RECEIVE_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void setListener() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.lySend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null) {
            return;
        }
        if(value != null) {
            int count = chatMessageList.size();
            for(DocumentChange documentChange : value.getDocumentChanges()) {
                if(documentChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setSenderId(documentChange.getDocument().getString(Constants.KEY_SENDER_ID));
                    chatMessage.setReceiverId(documentChange.getDocument().getString(Constants.KEY_RECEIVE_ID));
                    chatMessage.setMessage(documentChange.getDocument().getString(Constants.KEY_MESSAGE));
                    chatMessage.setDateTime(getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP)));
                    chatMessage.setDateObject(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessageList.add(chatMessage);
                }
            }
            Collections.sort(chatMessageList, (obj1, obj2) -> obj1.getDateObject().compareTo(obj2.getDateObject()));
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyDataSetChanged();
                chatAdapter.notifyItemRangeInserted(chatMessageList.size(), chatMessageList.size());
                binding.rcvChat.smoothScrollToPosition(chatMessageList.size() - 1);
            }
            binding.rcvChat.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
        if (conversationId == null) {
            checkForConversation();
        }
    };

    private void checkForConversation() {
        if(chatMessageList.size() != 0) {
            checkForConverationRemotely(
                    preferenceManager.getString(Constants.KEY_USER_ID),
                    receiveUser.getUser_id()
            );
            checkForConverationRemotely(
                    receiveUser.getUser_id(),
                    preferenceManager.getString(Constants.KEY_USER_ID)
            );
        }
    }

    private void addConversation(HashMap<String, Object> conversation) {
        firestore.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .add(conversation)
                .addOnSuccessListener(documentReference -> conversationId = documentReference.getId());
    }

    private void updateConversation(String message) {
        DocumentReference documentReference = firestore.collection(Constants.KEY_COLLECTION_CONVERSATIONS).document(conversationId);
        documentReference.update(Constants.KEY_LAST_MESSAGE, message,
                Constants.KEY_TIMESTAMP, new Date());
    }

    private void checkForConverationRemotely(String senderId, String receiverId) {
        firestore.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVE_ID, receiverId)
                .get()
                .addOnCompleteListener(conversationOnCompleteLister);
    }

    private final OnCompleteListener<QuerySnapshot> conversationOnCompleteLister = task -> {
        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversationId = documentSnapshot.getId();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        listenAvailabilityOfReceiver();
    }
}