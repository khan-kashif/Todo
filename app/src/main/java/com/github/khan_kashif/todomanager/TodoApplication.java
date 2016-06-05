package com.github.khan_kashif.todomanager;

import android.app.Application;
import android.content.Context;
import android.provider.ContactsContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kashif on 04/06/2016.
 */
public class TodoApplication extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();
    }
}
