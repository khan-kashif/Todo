package com.github.khan_kashif.todomanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoItem {

    public TodoItem(int id, String title, Date reminder, boolean hasReminder) {
        this.ID = id;
        this.Title = title;
        this.Reminder = reminder;
        this.HasReminder = hasReminder;
        this.Notified = false;

        MarkForDelete = false;
    }

    public int ID;
    public Date Reminder;
    public String Title;
    public boolean HasReminder;
    public boolean Notified;

    public boolean MarkForDelete;

    public void SetReminderString(String dateString, String timeString) {
        try {
            Reminder = new SimpleDateFormat(DataManager.DateTimeFormat).parse(dateString + " " + timeString);
        } catch (ParseException e) {
        }
    }

    public String GetReminderString(){
        return new SimpleDateFormat(DataManager.DateTimeFormat).format(Reminder);
    }

    public String GetReminderString(String format){
        return new SimpleDateFormat(format).format(Reminder);
    }

    public String GetDateString(){
        return DataManager.GetDateString(Reminder);
    }

    public String GetTimeString(){
        return DataManager.GetTimeString(Reminder);
    }
}
