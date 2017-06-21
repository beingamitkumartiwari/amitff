package com.amtee.friendsfinder.gcmhandler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.amtee.friendsfinder.NotificationList_Activity;
import com.amtee.friendsfinder.R;

/**
 * Created by Amit Kumar Tiwar on 02/08/16.
 */
public class GCM_NotificationAlert  {

    public static final int notifyID = 9001;
    public static void sendNotification(Context context, String msg){
        if(msg.contains("Tittle")){
            System.out.println("notifi  notifrangmnt ");
            String tittle=msg.substring(msg.indexOf("Tittle") + 6, msg.indexOf("Subtittle"));
            String subtittle=msg.substring(msg.indexOf("Subtittle") + 9, msg.indexOf("Message"));
            String message=msg.substring(msg.indexOf("Message") + 7);

            Intent resultIntent = new Intent(context, NotificationList_Activity.class);
            resultIntent.putExtra("msg", msg);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0,
                    resultIntent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder mNotifyBuilder;
            NotificationManager mNotificationManager;

            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mNotifyBuilder = new NotificationCompat.Builder(context)
                        .setContentTitle(tittle)
                        .setContentText(subtittle)
                        .setSubText(message)
                        .setSmallIcon(R.mipmap.splash);
                System.out.println("Amit 1  sub "+subtittle);
            } else {
                mNotifyBuilder = new NotificationCompat.Builder(context)
                        .setContentTitle(tittle)
                        .setContentText(subtittle)
                        .setSubText(message)
                        .setSmallIcon(R.mipmap.splash);
                // .setColor(context.getApplicationContext().getResources().getColor(R.color.accent_colour));
                System.out.println("Amit 2  sub "+subtittle);
            }
            mNotifyBuilder.setContentIntent(resultPendingIntent);

            // Set Vibrate, Sound and Light
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;

            mNotifyBuilder.setDefaults(defaults);
            mNotifyBuilder.setContentText(subtittle);
            mNotifyBuilder.setAutoCancel(true);
            mNotificationManager.notify(notifyID, mNotifyBuilder.build());

        }else{

            System.out.println("pushnotification notification  ");
            Intent resultIntent = new Intent(context, NotificationList_Activity.class);
            resultIntent.putExtra("msg", msg);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0,
                    resultIntent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder mNotifyBuilder;
            NotificationManager mNotificationManager;

            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mNotifyBuilder = new NotificationCompat.Builder(context)
                        .setContentTitle("Friend Finder")
                        .setContentText(msg)
                        .setSmallIcon(R.drawable.friendlist);
            } else {
                mNotifyBuilder = new NotificationCompat.Builder(context)
                        .setContentTitle("Friend Finder")
                        .setContentText(msg)
                        .setSmallIcon(R.drawable.friendlist);
                // .setColor(context.getApplicationContext().getResources().getColor(R.color.accent_colour));
            }
            mNotifyBuilder.setContentIntent(resultPendingIntent);

            // Set Vibrate, Sound and Light
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;

            mNotifyBuilder.setDefaults(defaults);
            mNotifyBuilder.setContentText(msg);
            mNotifyBuilder.setAutoCancel(true);
            mNotificationManager.notify(notifyID, mNotifyBuilder.build());
        }


    }
}
