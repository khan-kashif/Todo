package com.github.khan_kashif.todomanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ToDoEdit extends AppCompatActivity {

    TodoApplication todoApplication;
    int position;

    EditText titleEdit;
    Button dateEdit, timeEdit;

    TodoItem todoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);

        todoApplication = (TodoApplication)getApplication();

        position = getIntent().getIntExtra("position", -1);
        if(position == -1)
            todoItem = new TodoItem("", new Date());
        else
            todoItem = DataManager.TodoItems.get(position);

        titleEdit = (EditText)findViewById(R.id.title_edit);
        titleEdit.setText(todoItem.Title);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todoItem.Reminder);

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);


        dateEdit = (Button)findViewById(R.id.date_edit);
        dateEdit.setText(todoItem.GetDateString());
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ToDoEdit.this, new DateSetListener(todoApplication, dateEdit), year, month, day).show();
            }
        });

        timeEdit = (Button)findViewById(R.id.time_edit);
        timeEdit.setText(todoItem.GetTimeString());
        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ToDoEdit.this, new TimeSetListener(todoApplication, timeEdit), hour, minute, true).show();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_delete).setVisible(false);
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

            todoItem.Title = titleEdit.getText().toString();
            todoItem.SetReminderString(dateEdit.getText().toString(), timeEdit.getText().toString());

            if(position == -1)
                DataManager.TodoItems.add(todoItem);

            DataManager.SaveTodoItems(todoApplication);
            ToDoEdit.this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
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