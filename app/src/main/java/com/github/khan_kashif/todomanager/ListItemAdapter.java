package com.github.khan_kashif.todomanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kashif on 04/06/2016.
 */
public class ListItemAdapter extends ArrayAdapter<TodoItem> {

    List<TodoItem> Items;

    public ListItemAdapter(Context context, int resource, List<TodoItem> items) {
        super(context, resource, items);
        this.Items = items;
    }

    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent)
    {
        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.list_item, null);
        }

        CheckBox taskCheck = (CheckBox) view.findViewById(R.id.task_check);
        taskCheck.setChecked(Items.get(position).MarkForDelete);

        if(DataManager.IsDeleteMode)
            taskCheck.setVisibility(View.VISIBLE);
        else
            taskCheck.setVisibility(View.INVISIBLE);

        TextView titleView = (TextView)view.findViewById(R.id.task_title);
        titleView.setText(Items.get(position).Title);

        TextView reminderView = (TextView)view.findViewById(R.id.task_reminder);
        reminderView.setText(Items.get(position).GetReminderString());

        return view;
    }

    public void Refresh(){
        notifyDataSetChanged();
    }
}
