package com.example.timetableandmemo;

import android.content.Context;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimeTableManager {

    public TimeTableManager() { this.init(); }

    private String title = "DEFAULT TIMETABLE TITLE - TimeTableManager";
    private int startingHour = -1, endingHour = -1;
    private int numberOfHours;

    private void setTitle(String title) { this.title = title; }
    private String getTitle() { return this.title; }
    private void setStartingHour(int startingHour) { this.startingHour = startingHour; }
    private void setEndingHour(int endingHour) { this.endingHour = endingHour; }
    private void setNumberOfHours() { this.numberOfHours = this.endingHour - this.startingHour + 1; }

    //초기화 함수
    private void init() {
        this.setStartingHour(9);
        this.setEndingHour(12);
        this.setNumberOfHours();
    }

    //시작 시간 ~ 끝나는 시간으로 첫 열 채우기
    public void fillTimetableColumn_time(Context context, LinearLayout ttLayout) {
        LinearLayout.LayoutParams timeRowLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , 0, 1);
        for (int i = this.startingHour; i <= this.endingHour; i++) {
            TextView timeRow = new TextView(context);
            timeRow.setText(String.format("%d", i));
            ttLayout.addView(timeRow, timeRowLayout);
        }
    }

    //Main Activity 화면의 시간표 제목 변경
    public void applyTitle(TextView titleTextView) {
        titleTextView.setText(this.getTitle());
    }

    //요일별 과목 칸을 5분단위로 쪼개는 함수
    public void applyNumberOfColumnsBy5Minutes(GridLayout weekdayLayout) {
        weekdayLayout.setRowCount(numberOfHours * 4);
    }
}
