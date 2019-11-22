package com.example.timetableandmemo;

import android.content.Context;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimeTableManager {


    //시작 시간 ~ 끝나는 시간으로 첫 열 채우기
    void fillTimetableColumn_time(Context context, LinearLayout ttLayout, int startTime, int endTime) {
        LinearLayout.LayoutParams timeRowLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , 0, 1);
        for (int i = startTime; i <= endTime; i++) {
            TextView timeRow = new TextView(context);
            timeRow.setText(String.format("%d", i));
            ttLayout.addView(timeRow, timeRowLayout);
        }
    }

//    void setTimetableColumn_weekdays(Context context, GridLayout ttLayout, int startTime, int endTime) {
//        GridLayout.LayoutParams weekdayRowLayout = new GridLayout.LayoutParams(GridLayout.LayoutParams)
//    }
}
