package com.example.timetableandmemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Button directAdd;
    LinearLayout timetableColumn_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timetableColumn_time = findViewById(R.id.timetable_column_time);

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

        fillTimetableColumn_time(this, timetableColumn_time, 9, 11);
    }

    //시작 시간 ~ 끝나는 시간으로 첫 열 채우기
    void fillTimetableColumn_time(Context context, LinearLayout ttLayout, int startTime, int endTime) {
        LinearLayout.LayoutParams timeRowLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , 0, 1);
        for (int i = startTime; i <= endTime; i++) {
            TextView timeRow = new TextView(context);
            timeRow.setText(String.format("%d", i));
            ttLayout.addView(timeRow, timeRowLayout);
        }
    }
}
