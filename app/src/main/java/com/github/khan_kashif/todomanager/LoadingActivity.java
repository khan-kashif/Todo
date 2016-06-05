package com.github.khan_kashif.todomanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        DataManager.SetupDateFormat(getApplication());
        DataManager.GetTodoItems(getApplication());

        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(500);
                    // showing the main screen
                } catch (InterruptedException e) {
                } finally {
                    finish();
                }
            }
        };
        timer.start();
    }

    @Override
    public void finish(){

        Intent navigateToMain = new Intent(this, MainActivity.class);
        startActivity(navigateToMain);

        super.finish();
    }

}
