package com.github.khan_kashif.todomanager;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Kashif on 09/06/2016.
 */
public class WakefulService extends IntentService {

    public WakefulService(){
        super("WakefulService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Wakeup.acquire(getApplicationContext());

        int id = intent.getIntExtra("idTodoItem", -1);

        if(id == -1)
            return;

        if(!DataManager.IsDataReady)
            DataManager.Setup(getApplicationContext());

        TodoItem todoItem = DataManager.TodoItems.get(id);
        ReminderManager.SetupNotification(todoItem, getApplicationContext());

        NotificationBroadcaster.completeWakefulIntent(intent);

        Wakeup.release();
    }
}
