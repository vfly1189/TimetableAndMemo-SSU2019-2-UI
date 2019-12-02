package com.example.timetableandmemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    TextView timetableTitle;
    Button directAdd;
    TableRow timetableContentRow;
    LinearLayout timetableColumn_time; //시간표의 첫열(시간 구분선)
    GridLayout[] timetableColumn_weekdays = new GridLayout[5]; //0: 월요일, 1: 화요일, 2: 수요일, 3: 목요일, 4: 금요일
    TimeTableManager ttManager = new TimeTableManager();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timetableTitle = findViewById(R.id.timetable_title);

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

        timetableContentRow = findViewById(R.id.timetable_content_row);

        timetableColumn_time = (LinearLayout) timetableContentRow.getChildAt(0);
        for (int i = 0; i < 5; i++) timetableColumn_weekdays[i] = (GridLayout) timetableContentRow.getChildAt(i + 1);

        ttManager.fillTimetableColumn_time(this, timetableColumn_time);
        ttManager.applyTitle(timetableTitle);
        for (int i = 0; i < 5; i++) ttManager.applyNumberOfColumnsBy5Minutes(timetableColumn_weekdays[i]);

        //테스트 코드
        Button tb1 = new Button(this);
        GridLayout.Spec rowSpec = GridLayout.spec(3,5);
        GridLayout.Spec columnSpec = GridLayout.spec(0);
        GridLayout.LayoutParams gl = new GridLayout.LayoutParams(rowSpec, columnSpec);
        gl.width = 0;
        gl.setGravity(Gravity.FILL);
        tb1.setText("1");
        timetableColumn_weekdays[0].addView(tb1, gl);
    }
}
