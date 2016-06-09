package com.github.khan_kashif.todomanager;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by Kashif on 06/06/2016.
 */
public class ReminderManager {

    public static void CancelReminder(TodoItem todoItem, Context context) {

        Intent reminderIntent = new Intent(context, NotificationBroadcaster.class);
        reminderIntent.putExtra("idTodoItem", todoItem.ID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, todoItem.ID, reminderIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(pendingIntent != null)
            pendingIntent.cancel();
    }

    public static void SetupReminder(TodoItem todoItem, Context context)
    {
        if(!todoItem.HasReminder || todoItem.Notified)
            return;

        Intent reminderIntent = new Intent(context, NotificationBroadcaster.class);
        reminderIntent.putExtra("idTodoItem", todoItem.ID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, todoItem.ID, reminderIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = todoItem.Reminder.getTime();

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
    }

    public static void SetupNotification(TodoItem todoItem, Context context)
    {
        if(!todoItem.HasReminder)
            return;

        int requestCode = todoItem.ID;

        NotificationManager nm=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, ToDoEdit.class);
        intent.putExtra("idTodoItem", todoItem.ID);
        PendingIntent pending= PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(todoItem.Title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pending)
                .setAutoCancel(true)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        nm.notify(0, notification);

        todoItem.Notified = true;
        DataManager.SaveTodoItems(context);
    }
}
