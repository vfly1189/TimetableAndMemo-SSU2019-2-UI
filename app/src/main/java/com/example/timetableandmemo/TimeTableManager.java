package com.example.timetableandmemo;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Iterator;
import java.util.TreeMap;

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

    //Hour과 Minute으로 나뉘어진 시간을 하나의 blockUnit의 cell(1분에 한칸)수로 변환
    public int time2CellCount(int hour, int min) {
        int blockUnitIndex;
        blockUnitIndex = (hour - this.startingHour) * 60 + min;
        return blockUnitIndex;
    }

    //시간표 화면에 currentTimetalbeVO에 들어있는 SubjectBlock 모두 그리기 <<<<<각각의 Linear Layout 별로 바꿔볼 것
    public void fillTimetableContentRow(LinearLayout timetableColumn_weekday, int weekdayIndex) {
        int lastCellCount = time2CellCount(this.endingHour, 0); //시간표의 마지막 칸의 cellCount인덱스
        String[] weekDayTexts = this.context.getResources().getStringArray(R.array.weekDay);
        String currentWeekday = weekDayTexts[weekdayIndex]; //현재 요일 String으로 받아오기

        TreeMap<Integer, String> subjectBlocksOrder = new TreeMap<>();

        for(int i = 0; i < 5; i++){ timetableColumn_weekday.removeAllViews(); }//시간표 해당 요일에 들어있는 과목 block을 한번 지움

        //subjectBlocksOrder에 해당 요일의 (시작시각 : 과목명)을 저장
        for(SubjectSet ss : currentTimetableVO.getSubjectSets()) {
            String currentSubjectName = ss.getSubjectName();
            for(SubjectBlock sb : ss.getSubjectBlocks()) {
                if (currentWeekday.equals(sb.getWeekday())) {
                    subjectBlocksOrder.put(time2CellCount(sb.getsTime_hour(), sb.getsTime_min()), currentSubjectName);
                }
            }
        }

        Iterator<Integer> treeMapIterator = subjectBlocksOrder.keySet().iterator();
        while(treeMapIterator.hasNext()) {
            int key = treeMapIterator.next();
            int lastEndingTimeCellCount = 0; //SubjectBlock의 가장 늦은 위치의 시간을 저장하는 임시 저장소
            while(treeMapIterator.hasNext()) {
                Space spaceCell = new Space(this.context); //버튼을 놓기 앞서 넣을 빈칸
                Button buttonCell = new Button(this.context); //과목 버튼
                String subjectName = subjectBlocksOrder.get(key); //과목명
                SubjectBlock currentSubjectBlock = findSubjectBlock(subjectName, currentWeekday); //현재 SubjectBlock

                int endingTimeCellCount = time2CellCount(currentSubjectBlock.getfTime_hour(), currentSubjectBlock.getfTime_min());

                //Space와 Button이 각각 차지해야할 공간을 계산
                int spaceWeight = key - 1;
                float buttonWeight = endingTimeCellCount - key;

                LinearLayout.LayoutParams layoutParams_space = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, spaceWeight);
                LinearLayout.LayoutParams layoutParams_button = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, buttonWeight);

                timetableColumn_weekday.addView(spaceCell, layoutParams_space);
                timetableColumn_weekday.addView(buttonCell, layoutParams_button);

                lastEndingTimeCellCount = endingTimeCellCount;
            }

            //맨 마지막에 남는 공간을 채우는 용도의 Space
            Space lastSpaceCell = new Space(this.context);
            int lastSpaceWeight = lastCellCount - lastEndingTimeCellCount;
            LinearLayout.LayoutParams layoutParams_lastSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, lastSpaceWeight);
            timetableColumn_weekday.addView(lastSpaceCell, layoutParams_lastSpace);
        }


        //subjectBlocksOrder에 어떤 과목이 있는지 확인하는 테스트 코드
        while(treeMapIterator.hasNext()) {
            int key = treeMapIterator.next();
            Log.d("Subjects", String.format("%d, %s", key, subjectBlocksOrder.get(key)));
        }
    }

    //과목명과 요일로 해당 SubjectBlock을 찾아 리턴
    public SubjectBlock findSubjectBlock(String subjectName, String weekday) {
        for(SubjectSet ss : currentTimetableVO.getSubjectSets()) {
            if(ss.getSubjectName() == subjectName) {
                for(SubjectBlock sb : ss.getSubjectBlocks()) {
                    if(sb.getWeekday() == weekday) {
                        return sb;
                    }
                }
            }
        }
        return null;
    }
}