package com.github.khan_kashif.todomanager;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ToDoEdit extends AppCompatActivity {

    TodoApplication todoApplication;
    int idTodoItem;

    EditText titleEdit;
    Button dateEdit, timeEdit;
    CheckBox reminderCheck;

    TodoItem todoItem;

    LinearLayout viewReminder;

    Date dateToUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);

        todoApplication = (TodoApplication)getApplication();

        idTodoItem = getIntent().getIntExtra("idTodoItem", -1);
        if(idTodoItem == -1) {
            todoItem = new TodoItem(DataManager.GetNextID(), "", new Date(), false, false);
            idTodoItem = todoItem.ID;
        }
        else
            todoItem = DataManager.TodoItems.get(idTodoItem);

        if(todoItem == null) {
            finish();
            return;
        }

        dateToUse = new Date();
        if(todoItem.Reminder != null)
            dateToUse = todoItem.Reminder;

        reminderCheck = (CheckBox)findViewById(R.id.task_has_reminder);
        reminderCheck.setChecked(todoItem.HasReminder);
        reminderCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateReminderControl();
            }
        });

        titleEdit = (EditText)findViewById(R.id.title_edit);
        titleEdit.setText(todoItem.Title);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateToUse);

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        dateEdit = (Button)findViewById(R.id.date_edit);
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ToDoEdit.this, new DateSetListener(todoApplication, dateEdit), year, month, day).show();
            }
        });

        timeEdit = (Button)findViewById(R.id.time_edit);
        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ToDoEdit.this, new TimeSetListener(todoApplication, timeEdit), hour, minute, true).show();
            }
        });

        viewReminder = (LinearLayout)findViewById(R.id.view_reminder);
        UpdateReminderControl();
    }

    public void UpdateReminderControl(){
        if(reminderCheck.isChecked()) {
            viewReminder.setVisibility(View.VISIBLE);

            dateEdit.setText(DataManager.GetDateString(dateToUse));
            timeEdit.setText(DataManager.GetTimeString(dateToUse));
        }
        else
            viewReminder.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {

            SaveTodoItem();
            finish();

            return true;
        }

        if (id == R.id.action_delete) {

            DeleteTodoItem();
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void DeleteTodoItem(){
        ReminderManager.CancelReminder(todoItem, getApplicationContext());

        if(DataManager.TodoItems.containsKey(todoItem.ID)){
            DataManager.TodoItems.remove(todoItem.ID);
        }
    }

    protected void SaveTodoItem(){

        todoItem.Title = titleEdit.getText().toString();
        todoItem.HasReminder = reminderCheck.isChecked();

        if(todoItem.HasReminder) {
            todoItem.SetReminderString(dateEdit.getText().toString(), timeEdit.getText().toString());
            todoItem.Notified = false;
        }
        else {
            todoItem.Reminder = null;
            ReminderManager.CancelReminder(todoItem, getApplicationContext());
        }

        DataManager.TodoItems.put(todoItem.ID, todoItem);

        DataManager.SaveTodoItems(todoApplication);

        if(todoItem.HasReminder)
            ReminderManager.SetupReminder(todoItem, getApplicationContext());

        if(todoItem.HasReminder)
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.reminder_created),
                    Toast.LENGTH_LONG).show();
    }
}

class DateSetListener implements DatePickerDialog.OnDateSetListener {

    Button button;
    TodoApplication todoApplication;

    public DateSetListener(TodoApplication todoApplication, Button button){
        this.todoApplication = todoApplication;
        this.button = button;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth)
    {
        Calendar calendar  = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);

        button.setText(new SimpleDateFormat(DataManager.DateFormat).format(calendar.getTime()));
    }
}

class TimeSetListener implements TimePickerDialog.OnTimeSetListener {

    Button button;
    TodoApplication todoApplication;

    public TimeSetListener(TodoApplication todoApplication, Button button){
        this.todoApplication = todoApplication;
        this.button = button;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        Calendar calendar  = Calendar.getInstance();
        calendar.set(2016, 6, 6, hourOfDay, minute);

        button.setText(new SimpleDateFormat(DataManager.TimeFormat).format(calendar.getTime()));
    }
}