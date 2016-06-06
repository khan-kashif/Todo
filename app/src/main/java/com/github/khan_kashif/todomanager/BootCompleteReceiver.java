package com.github.khan_kashif.todomanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Kashif on 06/06/2016.
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        DataManager.Setup(context);

        for(int i: DataManager.TodoItems.keySet())
            ReminderManager.SetupReminder(DataManager.TodoItems.get(i), context);
    }

}