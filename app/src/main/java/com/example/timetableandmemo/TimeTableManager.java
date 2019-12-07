package com.example.timetableandmemo;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TimeTableManager {

    private String title = "DEFAULT TIMETABLE TITLE - TimeTableManager";
    private int startingHour = -1, endingHour = -1;
    private int numberOfHours;
    private Context context;
    private TimetableVO currentTimetableVO;
    private ArrayList<Block>[] blocksList = new ArrayList[5]; //0:월, 1:화, 2:수, 3:목, 4:금

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

    //blocksList에 Space와 SubjectBlock을 차례로 요일별로 분류해서 추가
    public void calculateBlocksList() {
        String subjectName;
        int minuteCount;

        //currentTimetableVO의 SubjectBlock들의 정보를 blocksList에 요일별로 분류해서 추가
        for(SubjectSet ss : currentTimetableVO.getSubjectSets()) {
            subjectName = ss.getSubjectName();
            for(SubjectBlock sb : ss.getSubjectBlocks()) {
                Block block = new Block(sb);
                switch (sb.getWeekday()) {
                    case "월요일":
                        this.blocksList[0].add(block);
                        break;
                    case "화요일":
                        this.blocksList[1].add(block);
                        break;
                    case "수요일":
                        this.blocksList[2].add(block);
                        break;
                    case "목요일":
                        this.blocksList[3].add(block);
                        break;
                    case "금요일":
                        this.blocksList[4].add(block);
                        break;
                }
            }
        }
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
        ttLayout.removeAllViews();
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
//    public void applyNumberOfColumnsBy5Minutes(GridLayout weekdayLayout) {
//        weekdayLayout.setRowCount(numberOfHours * 4);
//    }
}

class Block {
    private int minuteCount = 0;//이 블럭이 몇분의 공간을 차지하는지
    @Nullable private SubjectBlock subjectBlock = null;//null이면 Space객체에 해당, 있으면 클릭할 수 있는 시간표 블럭에 해당

    public Block() {}
    public Block(SubjectBlock subjectBlock) { this.subjectBlock = subjectBlock; }

    public int getMinuteCount() { return this.minuteCount; }
    public void setMinuteCount(int minuteCount) {this.minuteCount = minuteCount; }
    public SubjectBlock getSubjectBlock() { return this.subjectBlock; }
    public void setSubjectBlock(SubjectBlock subjectBlock) { this.subjectBlock = subjectBlock; }
}
