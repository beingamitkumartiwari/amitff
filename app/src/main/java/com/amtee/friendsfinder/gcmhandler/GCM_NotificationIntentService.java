package com.amtee.friendsfinder.gcmhandler;


import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.amtee.friendsfinder.datastorage.FriendsFinderDataBase;
import com.google.android.gms.gcm.GoogleCloudMessaging;


/**
 * Created by Amit Kumar Tiwar on 02/08/16.
 */
public class GCM_NotificationIntentService extends IntentService {
    private FriendsFinderDataBase friendsFinderDataBase;

    public GCM_NotificationIntentService() {
        super("GcmIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        friendsFinderDataBase=new FriendsFinderDataBase(GCM_NotificationIntentService.this);
        String messageType = gcm.getMessageType(intent);

//        System.out.println("pushnotification service " + messageType);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                GCM_NotificationAlert.sendNotification(getApplicationContext(),"Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                GCM_NotificationAlert.sendNotification(getApplicationContext(),"Deleted messages on server: "
                        + extras.toString());

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                GCM_NotificationAlert.sendNotification(getApplicationContext(),extras.getString("message"));
                if(extras.getString("message").contains("Tittle")){
                    String message=extras.getString("message").substring(extras.getString("message").indexOf("Message") + 7);
                    if(friendsFinderDataBase.getMessageCount()<100){
                        friendsFinderDataBase.insertNewMessage(message,System.currentTimeMillis(),"unread");
                    }else {
                        friendsFinderDataBase.updateMessage(message, System.currentTimeMillis(), "unread");
                    }
                }else{
                    if(friendsFinderDataBase.getMessageCount()<100){
                        friendsFinderDataBase.insertNewMessage(extras.getString("message"),System.currentTimeMillis(),"unread");
                    }else {
                        friendsFinderDataBase.updateMessage(extras.getString("message"), System.currentTimeMillis(), "unread");
                    }
                }
            }
        }
        GCM_BroadcastReciever.completeWakefulIntent(intent);
    }
}
