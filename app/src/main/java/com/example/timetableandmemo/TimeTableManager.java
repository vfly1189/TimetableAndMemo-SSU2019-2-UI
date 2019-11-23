package com.example.timetableandmemo;

import android.content.Context;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

public class TimeTableManager {

    private String title;
    private int startTime;
    private int endTime;

    TimeTableManager() {
        this.title = "DEFAULT_TABLE_TITLE";
    }

    TimeTableManager(String title) {
        this.title = title;
    }

    TimeTableManager(String title, int startTime, int endTime) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    //시작 시간 ~ 끝나는 시간으로 첫 열 채우기
    public void fillTimetableColumn_time(Context context, LinearLayout ttLayout) {
        LinearLayout.LayoutParams timeRowLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , 0, 1);
        for (int i = this.startTime; i <= this.endTime; i++) {
            TextView timeRow = new TextView(context);
            timeRow.setText(String.format("%d", i));
            ttLayout.addView(timeRow, timeRowLayout);
        }
    }

    //시간표의 각 열을 5분단위의 Space로 채움
    public void setTimetableColumn_weekdays(Context context, GridLayout ttLayout) {
        int numberOfRows_5min = (this.endTime - this.startTime) * 12;
        for (int i = 0; i < numberOfRows_5min; i++) {
            Space minSpace = new Space(context);
            ttLayout.addView(minSpace);
        }
    }
}
