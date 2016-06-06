package com.github.khan_kashif.todomanager;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kashif on 05/06/2016.
 */
public class DataManager {

    public static boolean IsDataReady = false;

    public static HashMap<Integer, TodoItem> TodoItems;

    private static String fileName = "tasks";
    public static String DateTimeFormat;
    public static String DateFormat;
    public static String TimeFormat;

    private static String SaveDateFormat = "yyyy-MM-dd HH:mm";

    public static boolean IsDeleteMode = false;

    private static int NextID = 0;

    public static int GetNextID(){
        return ++NextID;
    }

    public static TodoItem GetTodoItem(int position) {
        return new ArrayList<TodoItem>(TodoItems.values()).get(position);
    }

    public static void Setup(Context context) {
        SetupDateFormat(context);
        GetTodoItems(context);

        IsDataReady = true;
    }

    public static void SetupDateFormat(Context context){
        SimpleDateFormat dateFormat = ((SimpleDateFormat) android.text.format.DateFormat.getDateFormat(context));
        SimpleDateFormat timeFormat = ((SimpleDateFormat) android.text.format.DateFormat.getTimeFormat(context));

        DateTimeFormat = dateFormat.toLocalizedPattern() + " " + timeFormat.toLocalizedPattern();
        DateFormat = dateFormat.toLocalizedPattern();
        TimeFormat = timeFormat.toLocalizedPattern();
    }

    public static void GetTodoItems(Context context) {
        TodoItems = new HashMap<>();

        String jsonData = null;

        try{
            FileInputStream fis = context.openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            StringBuilder temp = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                temp.append(line);
            }

            jsonData = temp.toString();

            reader.close();
            fis.close();
        }
        catch (Exception e)
        {
        }

        if(jsonData != null)
            TodoItems = ParseJsonData(jsonData);
    }

    public static HashMap<Integer, TodoItem> ParseJsonData(String jsonData) {

        HashMap<Integer, TodoItem> result = new HashMap<Integer, TodoItem>();

        JSONArray jArray = null;

        try {
            jArray = new JSONArray(jsonData);
        } catch (JSONException e) {
            return result;
        }

        for (int i=0; i< jArray.length(); i++){

            JSONObject jObject;
            int id;
            String title;
            String reminder = null;
            boolean hasReminder;
            Date reminderDate = null;

            try {
                jObject = jArray.getJSONObject(i);
                id = jObject.getInt("id");
                title = jObject.getString("title");
                hasReminder = jObject.getBoolean("hasReminder");

                if(hasReminder)
                    reminder = jObject.getString("reminder");
            } catch (JSONException e) {
                continue;
            }

            if(hasReminder) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(SaveDateFormat);

                try {
                    reminderDate = dateFormat.parse(reminder);
                } catch (ParseException e) {
                    continue;
                }
            }

            TodoItem todoItem = new TodoItem(id, title, reminderDate, hasReminder);
            result.put(id, todoItem);

            NextID = todoItem.ID;
        }

        return result;
    }

    public static void SaveTodoItems(Context context){
        JSONArray jArray = new JSONArray();

        for(int i: TodoItems.keySet()){
            JSONObject jsonObject = new JSONObject();
            try{
                jsonObject.put("id", TodoItems.get(i).ID);
                jsonObject.put("title", TodoItems.get(i).Title);
                jsonObject.put("hasReminder", TodoItems.get(i).HasReminder);

                if(TodoItems.get(i).HasReminder)
                    jsonObject.put("reminder", TodoItems.get(i).GetReminderString(SaveDateFormat));

                jArray.put(jsonObject);
            }catch (Exception e){
                continue;
            }
        }

        String jString = jArray.toString();

        try{
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(jString.getBytes());
            fos.close();
        }
        catch (Exception e)
        {
        }
    }

    public static void DeleteTodoItems(Application application){

        HashMap<Integer, TodoItem> temp = new HashMap<>();
        temp.putAll(TodoItems);

        for(int i: temp.keySet()) {
            if(temp.get(i).MarkForDelete) {

                if(TodoItems.get(i).HasReminder)
                    ReminderManager.CancelReminder(TodoItems.get(i), application);

                TodoItems.remove(i);
            }
        }

        SaveTodoItems(application);
    }

    public static void SetDeleteMode(boolean mode){
        IsDeleteMode = mode;

        if(mode)
        {
            for(int i : TodoItems.keySet()) {
                TodoItems.get(i).MarkForDelete = false;
            }
        }
    }

    public static String GetDateString(Date date){
        return new SimpleDateFormat(DataManager.DateFormat).format(date);
    }

    public static String GetTimeString(Date date){
        return new SimpleDateFormat(DataManager.TimeFormat).format(date);
    }
}
