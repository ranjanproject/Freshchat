package com.pratik.chatactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.freshchat.consumer.sdk.ConversationOptions;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatCallbackStatus;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.freshchat.consumer.sdk.FreshchatUser;
import com.freshchat.consumer.sdk.UnreadCountCallback;
import com.freshchat.consumer.sdk.exception.MethodNotAllowedException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //configuring freshchat
        FreshchatConfig freshchatConfig = new FreshchatConfig("70d9aedc-435d-46f5-b504-081c72ae5df2", "cdac2ad1-4aaa-433c-8ea0-60b557f202c0");
        Freshchat.getInstance(getApplicationContext()).init(freshchatConfig);

        //setting user

        //Get the user object for the current installation
        FreshchatUser freshUser=Freshchat.getInstance(getApplicationContext()).getUser();

        freshUser.setFirstName("Pratik");
        freshUser.setLastName("Ranjan");
        freshUser.setEmail("pratik.ranjan@testbook.com");
        freshUser.setPhone("+91", "9790987495");

        //Call setUser so that the user information is synced with Freshchat's servers
        try {
            Freshchat.getInstance(getApplicationContext()).setUser(freshUser);
        } catch (MethodNotAllowedException e) {
            e.printStackTrace();
        }

        //setting tag
        List<String> tags = new ArrayList<>();
        tags.add("user");

        final ConversationOptions convOptions = new ConversationOptions()
                .filterByTags(tags, "Message Us");




        //clickable image to launch Freshchat activity
        ImageView imageView = (ImageView) findViewById(R.id.chatImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Freshchat.showConversations(getApplicationContext(), convOptions);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(Freshchat.FRESHCHAT_UNREAD_MESSAGE_COUNT_CHANGED);
        getLocalBroadcastManager(getApplicationContext()).registerReceiver(unreadCountChangeReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
//        getLocalBroadcastManager(getApplicationContext()).unregisterReceiver(unreadCountChangeReceiver);
    }

    BroadcastReceiver unreadCountChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Freshchat.getInstance(getApplicationContext()).getUnreadCountAsync(new UnreadCountCallback() {
                @Override
                public void onResult(FreshchatCallbackStatus freshchatCallbackStatus, int unreadCount) {
                    //Assuming "badgeTextView" is a text view to show the count on
//                    badgeTextView.setText(Integer.toString(unreadCount));
                    Toast.makeText(getApplicationContext(),Integer.toString(unreadCount),Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    public static LocalBroadcastManager getLocalBroadcastManager(@NonNull Context context) {
        return LocalBroadcastManager.getInstance(context.getApplicationContext());
    }
}
