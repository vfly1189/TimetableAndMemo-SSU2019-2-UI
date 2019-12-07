package com.example.timetableandmemo;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TimeTableManager {

    private String title = "DEFAULT TIMETABLE TITLE - TimeTableManager";
    private int startingHour = -1, endingHour = -1;
    private int numberOfHours;
    private Context context;
    private TimetableVO currentTimetableVO;

    public TimeTableManager(Context context) {
        this.setContext(context);
    }

    public void setTitle(String title) { this.title = title; }
    public void setTitle() { this.title = currentTimetableVO.getTitle(); }
    public String getTitle() { return this.title; }
    public void setStartingHour(int startingHour) { this.startingHour = startingHour; }
    public void setEndingHour(int endingHour) { this.endingHour = endingHour; }
    public void setContext(Context context) { this.context = context; }
    public void setCurrentTimetableVO(TimetableVO ttVO) { this.currentTimetableVO = ttVO; }

    //currentTimetableVO에 들어있는 과목들을 기반으로 startingTime과 EndingTime 계산
    public void calculateStartingAndEndingTimes() {
        int minStartingHour = 23, maxEndingHour = 0;
        for (SubjectSet ss : this.currentTimetableVO.getSubjectSets()) {
            for (SubjectBlock sb : ss.getSubjectBlocks()) {
                if(minStartingHour > sb.getsTime_hour()) minStartingHour = sb.getsTime_hour();
                if(maxEndingHour < sb.getfTime_hour()) maxEndingHour = sb.getfTime_hour();
            }
        }
        this.setStartingHour(minStartingHour);
        this.setEndingHour(maxEndingHour);
    }

    //startingHour과 endingHour로 시간표가 몇시간 짜리인지 계산
    public void calculateNumberOfHours() {
        this.numberOfHours = this.endingHour - this.startingHour + 1;
    }

    //시간표의 첫 행(요일 써져있는 행)을 array.xml 의 weekDay 리소스로 채움
    public void applyTimetableWeekdaysRowText(TableRow parentRow) {
        String[] weekDayTexts = this.context.getResources().getStringArray(R.array.weekDay);
        for (int i = 0; i < 5; i++) {
            TextView tv = (TextView)parentRow.getChildAt(i+1);
            tv.setText(weekDayTexts[i]);
            tv.setGravity(Gravity.CENTER);
        }
    }

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
}
