package com.example.timetableandmemo;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.TreeMap;

public class TimeTableManager {

    private String title = "DEFAULT TIMETABLE TITLE - TimeTableManager";
    private int startingHour = -1, endingHour = -1;
    private int numberOfHours;
    private Context context;
    private TimetableVO currentTimetableVO;
    private AlertDialog deletSubjectSetDialog;

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
            timeRow.setBackgroundResource(R.drawable.border_line_bottom);
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
        int lastCellCount = time2CellCount(this.endingHour + 1, 0); //시간표의 마지막 칸의 cellCount인덱스
        String[] weekDayTexts = this.context.getResources().getStringArray(R.array.weekDay);
        String currentWeekday = weekDayTexts[weekdayIndex]; //현재 요일 String으로 받아오기

        final TreeMap<Integer, String> subjectBlocksOrder = new TreeMap<>();

        for(int i = 0; i < 5; i++){ timetableColumn_weekday.removeAllViews(); }//시간표 해당 요일에 들어있는 과목 block을 한번 지움

        //subjectBlocksOrder에 해당 요일의 (시작시각에 해당하는 index : 과목명)을 저장
        for(SubjectSet ss : currentTimetableVO.getSubjectSets()) {
            String currentSubjectName = ss.getSubjectName();
            for(SubjectBlock sb : ss.getSubjectBlocks()) {
                if (currentWeekday.equals(sb.getWeekday())) {
                    subjectBlocksOrder.put(time2CellCount(sb.getsTime_hour(), sb.getsTime_min()) + 1, currentSubjectName); //시작 인덱스는 계산 값에 +1을 해야 정확한 위치에 들어감
                }
            }
        }

        int lastEndingTimeCellCount = 0; //SubjectBlock의 가장 늦은 위치의 시간을 저장하는 임시 저장소
        for(int key : subjectBlocksOrder.keySet()){
            Space spaceCell = new Space(this.context); //버튼을 놓기 앞서 넣을 빈칸
            Button buttonCell = new Button(this.context); //과목 버튼
            final String subjectName = subjectBlocksOrder.get(key); //과목명
            SubjectBlock currentSubjectBlock = findSubjectBlock(subjectName, currentWeekday); //현재 SubjectBlock

            int endingTimeCellCount = time2CellCount(currentSubjectBlock.getfTime_hour(), currentSubjectBlock.getfTime_min());

            //Space와 Button이 각각 차지해야할 공간을 계산 및 할당
            int spaceWeight = ((key - 1) - (lastEndingTimeCellCount + 1)) + 1;
            float buttonWeight = (endingTimeCellCount - key) + 1; //칸의 갯수는 endingCellCount - startingCellCount + 1
            LinearLayout.LayoutParams layoutParams_space = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, spaceWeight);
            LinearLayout.LayoutParams layoutParams_button = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, buttonWeight);

            //buttonCell의 기타 attribute 설정
            buttonCell.setText(String.format("%s\n(%s)", subjectName, currentSubjectBlock.getClassroomName())); //버튼에 과목명과 강의실 표시
            buttonCell.setBackgroundColor(0x5f000000 + subjectName.hashCode() % 0x1000000); //(투명도) + (과목이름으로 생성된 컬러코드 중 색깔부분만 추출)

            //버튼을 클릭했을 때의 동작 - AddTestAndAssignment 액티비티 호출
            buttonCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("디버그", String.format("%s 클릭됨", subjectName));
                    Intent intent = new Intent();
                    ComponentName componentName = new ComponentName(
                            "com.example.timetableandmemo",
                            "com.example.timetableandmemo.AddTestAndAssignment"
                    );
                    intent.putExtra("subjectName", subjectName);
                    intent.setComponent(componentName);
                    context.startActivity(intent);
                }
            });

            //버튼을 길게 눌렀을 때의 동작
            buttonCell.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d("디버그", String.format("%s 길게 눌림", subjectName));
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("과목 삭제");
                    builder.setMessage(String.format("해당 과목(%s)을 정말 시간표에서 삭제하시겠습니까?", subjectName));
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.deleteSubjectSet(subjectName);
                            Toast.makeText(context, String.format("'%s'이(가) 삭제됨", subjectName), Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("아니오", null);
                    deletSubjectSetDialog = builder.create();
                    deletSubjectSetDialog.show();
                    return true;
                }
            });

            //시간표에 Block들 추가
            timetableColumn_weekday.addView(spaceCell, layoutParams_space);
            timetableColumn_weekday.addView(buttonCell, layoutParams_button);

            lastEndingTimeCellCount = endingTimeCellCount;

            Log.d("디버그", String.format("%s %d:%d~%d:%d (%d %s %d)", currentWeekday, currentSubjectBlock.getsTime_hour(), currentSubjectBlock.getsTime_min(), currentSubjectBlock.getfTime_hour(), currentSubjectBlock.getfTime_min(), key, subjectName, endingTimeCellCount));
        }
        //맨 마지막에 남는 공간을 채우는 용도의 Space
        Space lastSpaceCell = new Space(this.context);
        int lastSpaceWeight = lastCellCount - lastEndingTimeCellCount;
        LinearLayout.LayoutParams layoutParams_lastSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, lastSpaceWeight);
        timetableColumn_weekday.addView(lastSpaceCell, layoutParams_lastSpace);
        Log.d("디버그", String.format("%s 마지막 Space %d %d %d", currentWeekday, lastCellCount, lastEndingTimeCellCount, lastSpaceWeight));
    }

    //과목명과 요일로 해당 SubjectBlock을 찾아 리턴
    public SubjectBlock findSubjectBlock(String subjectName, String weekday) {
        for(SubjectSet ss : currentTimetableVO.getSubjectSets()) {
            if(subjectName.equals(ss.getSubjectName())) {
                for(SubjectBlock sb : ss.getSubjectBlocks()) {
                    if(weekday.equals(sb.getWeekday())) {
                        return sb;
                    }
                }
            }
        }
        return null;
    }
}