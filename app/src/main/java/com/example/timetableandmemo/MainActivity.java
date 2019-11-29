package com.example.timetableandmemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    Button directAdd;
    LinearLayout timetableColumn_time;
    RelativeLayout[] timetableColumn_weekdays = new RelativeLayout[5];
    TimeTableManager ttManager = new TimeTableManager("Example Title", 9, 13);


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        timetableColumn_time = findViewById(R.id.timetable_column_time);
        timetableColumn_weekdays[0] = findViewById(R.id.timetable_column_MON);
        timetableColumn_weekdays[1] = findViewById(R.id.timetable_column_TUE);
        timetableColumn_weekdays[2] = findViewById(R.id.timetable_column_WED);
        timetableColumn_weekdays[3] = findViewById(R.id.timetable_column_THU);
        timetableColumn_weekdays[4] = findViewById(R.id.timetable_column_FRI);

        directAdd = (Button)findViewById(R.id.directAdd);
        directAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), DirectAdd.class);
                startActivity(intent);
            }
        });

        ttManager.fillTimetableColumn_time(this, timetableColumn_time);
        ttManager.setColumnHeight(timetableColumn_time.getMeasuredHeight());
        Log.d("테스트", String.format("%d", ttManager.getColumnHeight()));
    }
}
