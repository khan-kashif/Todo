package com.github.khan_kashif.todomanager;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by Kashif on 06/06/2016.
 */
public class NotificationBroadcaster extends WakefulBroadcastReceiver
{
    public void onReceive(Context context, Intent intent) {

        Intent service = new Intent(context, WakefulService.class);
        service.putExtra("idTodoItem", intent.getIntExtra("idTodoItem", -1));

        startWakefulService(context, service);

    }
}
