package com.github.khan_kashif.todomanager;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Kashif on 06/06/2016.
 */
public class NotificationBroadcaster extends BroadcastReceiver
{
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        int id = intent.getIntExtra("idTodoItem", -1);
        if(id == -1)
            return;

        if(!DataManager.IsDataReady)
            DataManager.Setup(context);

        TodoItem todoItem = DataManager.TodoItems.get(id);
        ReminderManager.SetupNotification(todoItem, context);
    }
}
