package com.github.khan_kashif.todomanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoItem {

    public TodoItem(String title, Date reminder) {
        this.Title = title;
        this.Reminder = reminder;
        MarkForDelete = false;
    }

    public Date Reminder;
    public String Title;
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
        return new SimpleDateFormat(DataManager.DateFormat).format(Reminder);
    }

    public String GetTimeString(){
        return new SimpleDateFormat(DataManager.TimeFormat).format(Reminder);
    }
}
