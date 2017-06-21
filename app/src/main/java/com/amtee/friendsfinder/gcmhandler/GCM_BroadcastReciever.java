package com.amtee.friendsfinder.gcmhandler;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Amit Kumar Tiwar on 02/08/16.
 */
public class GCM_BroadcastReciever extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName componentName = new ComponentName(context.getPackageName(), GCM_NotificationIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(componentName)));
        System.out.print("Reciver noticfy");

    }
}
