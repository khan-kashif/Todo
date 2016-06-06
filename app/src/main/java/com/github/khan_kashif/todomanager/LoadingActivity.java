package com.github.khan_kashif.todomanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        if(!DataManager.IsDataReady) {
            DataManager.Setup(getApplication());
        }

        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(500);
                    // showing the main screen
                } catch (InterruptedException e) {
                }finally {
                    NavigateToMainActivity();
                }
            }
        };

        timer.start();
    }

    public void NavigateToMainActivity(){
        Intent navigateToMain = new Intent(this, MainActivity.class);
        startActivity(navigateToMain);

        finish();
    }
}
