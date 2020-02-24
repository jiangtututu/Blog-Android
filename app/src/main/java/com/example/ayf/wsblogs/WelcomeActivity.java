package com.example.ayf.wsblogs;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {
    TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        tv_title = (TextView)findViewById(R.id.welcome_title);
        Typeface face = Typeface.createFromAsset(getAssets(),"WenYue-XinQingNianTi-NC-W8.ttf");
        tv_title.setTypeface(face);
        getSupportActionBar().hide();
        final Intent intent = new Intent(this,LoginAcitivity.class);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                WelcomeActivity.this.finish();
                startActivity(intent);
            }
        };
        timer.schedule(task,1000*3);
    }
}
