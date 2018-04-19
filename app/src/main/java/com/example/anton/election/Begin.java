package com.example.anton.election;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Timer;
import java.util.TimerTask;


public class Begin extends AppCompatActivity {

    Timer timer = new Timer();
    MyTimerTask mTimerTask = new MyTimerTask();
    boolean use = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_begin);

        timer.schedule(mTimerTask,3000);
    }

    @Override
    protected void onResume(){
        super .onResume();

    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            Intent intent = new Intent(Begin.this, General.class);
            startActivity(intent);
        }
    }
}
