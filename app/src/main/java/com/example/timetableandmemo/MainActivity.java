package com.example.timetableandmemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    Button directAdd;
    LinearLayout timetableColumn_time;
    GridLayout[] timetableColumn_weekdays = new GridLayout[5];

    GridLayout[] timetableColumn_weekdays = new GridLayout[5];

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

        TimeTableManager ttManager = new TimeTableManager("Example Title", 9, 11);

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
        for (int i = 0; i < 5; i ++) {
            ttManager.setTimetableColumn_weekdays(this, timetableColumn_weekdays[i]);
        }
    }
}
