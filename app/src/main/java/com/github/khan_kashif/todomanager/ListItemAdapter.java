package com.github.khan_kashif.todomanager;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kashif on 04/06/2016.
 */
public class ListItemAdapter extends BaseAdapter {

    Context context;

    public ListItemAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return DataManager.TodoItems.size();
    }

    @Override
    public Object getItem(int position) {
        return DataManager.GetTodoItem(position);
    }

    @Override
    public long getItemId(int position) {
        return ((TodoItem)getItem(position)).ID;
    }

    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent)
    {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            view = vi.inflate(R.layout.list_item, null);
        }

        TodoItem todoItem = DataManager.GetTodoItem(position);

        CheckBox taskCheck = (CheckBox) view.findViewById(R.id.task_check);
        taskCheck.setChecked(todoItem.MarkForDelete);

        if(DataManager.IsDeleteMode)
            taskCheck.setVisibility(View.VISIBLE);
        else
            taskCheck.setVisibility(View.INVISIBLE);

        TextView titleView = (TextView)view.findViewById(R.id.task_title);
        titleView.setText(todoItem.Title);

        TextView reminderView = (TextView) view.findViewById(R.id.task_reminder);

        if(todoItem.HasReminder) {
            reminderView.setVisibility(View.VISIBLE);
            reminderView.setText(todoItem.GetReminderString());
        }
        else
            reminderView.setVisibility(View.INVISIBLE);

        return view;
    }

    public void Refresh(){
        notifyDataSetChanged();
    }
}
