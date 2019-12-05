package com.example.timetableandmemo;

import android.content.Context;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.realm.Realm;

public class TimeTableManager {

    private String title = "DEFAULT TIMETABLE TITLE - TimeTableManager";
    private int startingHour = -1, endingHour = -1;
    private int numberOfHours;
    private Context context;
    private Realm realm;

    public TimeTableManager(Context context) {
        this.setContext(context);
        this.setStartingHour(9);
        this.setEndingHour(12);
        this.setNumberOfHours();
    }

    public void setTitle(String title) { this.title = title; }
    public String getTitle() { return this.title; }
    public void setStartingHour(int startingHour) { this.startingHour = startingHour; }
    public void setEndingHour(int endingHour) { this.endingHour = endingHour; }
    public void setNumberOfHours() { this.numberOfHours = this.endingHour - this.startingHour + 1; }
    public void setContext(Context context) { this.context = context; }
    public void setRealm(Realm realm) { this.realm = realm; }

    //시작 시간 ~ 끝나는 시간으로 첫 열 채우기
    public void fillTimetableColumn_time(LinearLayout ttLayout) {
        LinearLayout.LayoutParams timeRowLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , 0, 1);
        for (int i = this.startingHour; i <= this.endingHour; i++) {
            TextView timeRow = new TextView(this.context);
            timeRow.setText(String.format("%d", i));
            ttLayout.addView(timeRow, timeRowLayout);
        }
    }

    //Main Activity 화면의 시간표 제목 변경
    public void applyTitle(TextView titleTextView) {
        titleTextView.setText(this.title);
    }

    //요일별 과목 칸을 5분단위로 쪼개는 함수
    public void applyNumberOfColumnsBy5Minutes(GridLayout weekdayLayout) {
        weekdayLayout.setRowCount(numberOfHours * 4);
    }
}
