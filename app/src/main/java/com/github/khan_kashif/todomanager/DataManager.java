package com.github.khan_kashif.todomanager;

import android.app.Application;
import android.content.Context;

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
import java.util.List;

/**
 * Created by Kashif on 05/06/2016.
 */
public class DataManager {

    public static List<TodoItem> TodoItems;

    private static String fileName = "tasks";
    public static String DateTimeFormat;
    public static String DateFormat;
    public static String TimeFormat;

    private static String SaveDateFormat = "yyyy-MM-dd HH:mm";

    public static boolean IsDeleteMode = false;

    public static void SetupDateFormat(Application application){
        SimpleDateFormat dateFormat = ((SimpleDateFormat) android.text.format.DateFormat.getDateFormat(application));
        SimpleDateFormat timeFormat = ((SimpleDateFormat) android.text.format.DateFormat.getTimeFormat(application));

        DateTimeFormat = dateFormat.toLocalizedPattern() + " " + timeFormat.toLocalizedPattern();
        DateFormat = dateFormat.toLocalizedPattern();
        TimeFormat = timeFormat.toLocalizedPattern();
    }

    public static void GetTodoItems(Application application) {
        TodoItems = new ArrayList<>();

        String jsonData = null;

        try{
            FileInputStream fis = application.openFileInput(fileName);
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

    public static List<TodoItem> ParseJsonData(String jsonData) {

        List<TodoItem> result = new ArrayList<>();

        JSONArray jArray = null;

        try {
            jArray = new JSONArray(jsonData);
        } catch (JSONException e) {
            return new ArrayList<TodoItem>();
        }

        for (int i=0; i< jArray.length(); i++){

            JSONObject jObject;
            String title;
            String reminder;
            Date reminderDate;

            try {
                jObject = jArray.getJSONObject(i);
                title = jObject.getString("title");
                reminder = jObject.getString("reminder");
            } catch (JSONException e) {
                continue;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(SaveDateFormat);

            try {
                reminderDate = dateFormat.parse(reminder);
            } catch (ParseException e) {
                continue;
            }

            TodoItem todoItem = new TodoItem(title, reminderDate);
            result.add(todoItem);

        }

        return result;
    }

    public static void SaveTodoItems(Application application){
        JSONArray jArray = new JSONArray();

        for(int i=0; i< TodoItems.size(); i++){
            JSONObject jsonObject = new JSONObject();
            try{
                jsonObject.put("title", TodoItems.get(i).Title);
                jsonObject.put("reminder", TodoItems.get(i).GetReminderString(SaveDateFormat));

                jArray.put(jsonObject);
            }catch (Exception e){
                continue;
            }
        }

        String jString = jArray.toString();

        try{
            FileOutputStream fos = application.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(jString.getBytes());
            fos.close();
        }
        catch (Exception e)
        {
        }
    }

    public static void DeleteTodoItems(Application application){

        List<TodoItem> temp = new ArrayList<>();
        temp.addAll(TodoItems);

        for(int i= temp.size() - 1; i > -1; i--) {
            if(temp.get(i).MarkForDelete)
                TodoItems.remove(i);
        }

        SaveTodoItems(application);
    }

    public static void SetDeleteMode(boolean mode){
        IsDeleteMode = mode;

        if(mode)
        {
            for(int i= 0; i < TodoItems.size(); i++) {
                TodoItems.get(i).MarkForDelete = false;
            }
        }
    }
}
