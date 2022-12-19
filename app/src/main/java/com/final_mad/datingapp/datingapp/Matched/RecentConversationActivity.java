package com.final_mad.datingapp.datingapp.Matched;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.final_mad.datingapp.datingapp.R;
import com.final_mad.datingapp.datingapp.Utils.Constants;
import com.final_mad.datingapp.datingapp.Utils.PreferenceManager;
import com.final_mad.datingapp.datingapp.Utils.TopNavigationViewHelper;
import com.final_mad.datingapp.datingapp.databinding.ActivityRecentConversationBinding;
import com.final_mad.datingapp.datingapp.models.ChatMessage;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecentConversationActivity extends BaseActivity {
    private PreferenceManager preferenceManager;
    private List<ChatMessage> conversationList;
    private RecentConversationAdapter recentConversationAdapter;
    private FirebaseFirestore firestore;
    private ActivityRecentConversationBinding binding;
    private static final String TAG = "RecentActivity";
    private static final int ACTIVITY_NUM = 3;
    private final Context mContext = RecentConversationActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecentConversationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        listenConversations();
        setupTopNavigationView();
    }

    private void listenConversations() {
        firestore.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        firestore.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVE_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private void init () {
        preferenceManager = new PreferenceManager(getApplicationContext());
        conversationList = new ArrayList<>();
        recentConversationAdapter = new RecentConversationAdapter(conversationList);
        binding.rcvRecentConversation.setAdapter(recentConversationAdapter);
        firestore = FirebaseFirestore.getInstance();
    }

    private void setupTopNavigationView() {
        Log.d(TAG, "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationViewEx tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if(documentChange.getType() == DocumentChange.Type.ADDED) {
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVE_ID);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setSenderId(senderId);
                    chatMessage.setReceiverId(receiverId);
                    if (preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)) {
                        chatMessage.setConversationImage(documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE));
                        chatMessage.setConversationId(documentChange.getDocument().getString(Constants.KEY_RECEIVE_ID));
                        chatMessage.setConversationName(documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME));
                    } else {
                        chatMessage.setConversationImage(documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE));
                        chatMessage.setConversationId(documentChange.getDocument().getString(Constants.KEY_SENDER_ID));
                        chatMessage.setConversationName(documentChange.getDocument().getString(Constants.KEY_SENDER_NAME));
                    }
                    chatMessage.setMessage(documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE));
                    chatMessage.setDateObject(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    conversationList.add(chatMessage);
                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    for (int i = 0; i < conversationList.size(); i++) {
                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVE_ID);
                        if (conversationList.get(i).getSenderId().equals(senderId) && conversationList.get(i).getReceiverId().equals(receiverId)) {
                            conversationList.get(i).setMessage(documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE));
                            conversationList.get(i).setDateObject(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                            break;
                        }
                    }
                }
            }
            Collections.sort(conversationList, (obj1, obj2) -> obj2.getDateObject().compareTo(obj1.getDateObject()));
            recentConversationAdapter.notifyDataSetChanged();

        }
    };
}