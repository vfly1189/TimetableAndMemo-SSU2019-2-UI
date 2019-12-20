package com.example.timetableandmemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class DirectAdd extends AppCompatActivity {

    Button addBtn;
    Button okayAdd;

    EditText lectureName;
    EditText professorName;


    LinearLayout firstLL;
    Context context = this;

    ArrayStack<LinearLayout> stack = new ArrayStack(20);

    //겹치는 시간 존재했을 시 스택 맨 위를 가리키게 할 변수
    int stackTop = -1;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_add);

        lectureName = (EditText)findViewById(R.id.lectureName);
        professorName = (EditText)findViewById(R.id.professorName);
        firstLL = (LinearLayout) findViewById(R.id.firstLL);
        addBtn = (Button)findViewById(R.id.addBtn);
        okayAdd = (Button)findViewById(R.id.okayAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                addInfo();
                count++;
            }
        });
        okayAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int k = 0;
                int flag=0;

                //처음
                if(stackTop == -1) stackTop = stack.top;
                //여러번
                else stack.top = stackTop;

                //수업명과 교수명 입력안할 시 Toast
                if(lectureName.getText().toString().equals("") || professorName.getText().toString().equals(""))
                {
                    Toast.makeText(context,"수업명과 교수명을 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                //과목 set을 하나 만듬
                final SubjectSet subjectSet = new SubjectSet(lectureName.getText().toString(),professorName.getText().toString());

                //과목에 대한 요일,시간 설정을 안할시
                if(stack.empty())
                {
                    List<SubjectBlock> temp = subjectSet.getSubjectBlocks();
                    if(temp.isEmpty())
                    {
                        Toast.makeText(context,"과목을 추가해주세요",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    while(!stack.empty())
                    {
                        String className = new String();
                        String start[] = new String[2];
                        String end[] = new String[2];

                        LinearLayout out = (LinearLayout) stack.top();

                        EditText classInfo = (EditText)out.getChildAt(0);
                        className = classInfo.getText().toString();

                        if(className.equals(""))
                        {
                            Toast.makeText(context,"강의실명을 입력하세요",Toast.LENGTH_LONG).show();
                            return;
                        }

                        LinearLayout out_child = (LinearLayout)out.getChildAt(1);

                        Button startBtn = (Button)out_child.getChildAt(1);
                        if((startBtn.getText()).equals("시작시각"))
                        {
                            Toast.makeText(getApplicationContext(),"시간을 입력하세요",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        StringTokenizer token1 = new StringTokenizer((String)startBtn.getText(),":");
                        int i=0;
                        while(token1.hasMoreTokens())
                        {
                            start[i] = token1.nextToken();
                            i++;
                        }

                        Button endBtn = (Button)out_child.getChildAt(2);
                        if((endBtn.getText()).equals("종료시각"))
                        {
                            Toast.makeText(getApplicationContext(),"시간을 입력하세요",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        StringTokenizer token2 = new StringTokenizer((String)endBtn.getText(),":");
                        int j=0;
                        while(token2.hasMoreTokens())
                        {
                            end[j] = token2.nextToken();
                            j++;
                        }

                        Button weekDay = (Button)out_child.getChildAt(0);

                        int startHour = Integer.parseInt(start[0]);
                        int startMinute = Integer.parseInt(start[1]);
                        int endHour = Integer.parseInt(end[0]);
                        int endMinute = Integer.parseInt(end[1]);

                        //과목set에 Block추가
                        SubjectBlock subjectBlock = new SubjectBlock(className,(String)weekDay.getText(),startHour,startMinute,endHour,endMinute);
                        subjectSet.add(subjectBlock);

                        stack.pop();
                        k++;
                    }

                    final Realm mRealm = Realm.getDefaultInstance();
                    final TimetableVO ttVO = (TimetableVO)mRealm.where(TimetableVO.class).equalTo("id",0).findFirst();

                    //겹치는 시간 계산
                    List<SubjectSet> Overlap = ttVO.getSubjectSets();
                    Iterator<SubjectSet> iteratorSet = Overlap.iterator();

                    while(iteratorSet.hasNext())
                    {
                        SubjectSet alreadyExistSubjectSet = iteratorSet.next();

                        List<SubjectBlock> alreadyExistSubjectBlock = alreadyExistSubjectSet.getSubjectBlocks();
                        Iterator<SubjectBlock> alreadyExistIterator = alreadyExistSubjectBlock.iterator();
                        List<SubjectBlock> newSubjectBlock = subjectSet.getSubjectBlocks();
                        Iterator<SubjectBlock> newSubjectIterator = newSubjectBlock.iterator();

                        while(alreadyExistIterator.hasNext())
                        {
                            SubjectBlock alreadyBlock = alreadyExistIterator.next();
                            while(newSubjectIterator.hasNext())
                            {
                                SubjectBlock newBlock = newSubjectIterator.next();

                                //요일이 같아야됨
                                if(!(newBlock.getWeekday().equals(alreadyBlock.getWeekday()))) break;

                                //안겹치는다는거
                                int not_hour_diff = alreadyBlock.getsTime_hour() - newBlock.getfTime_hour();
                                int not_min_diff = ((not_hour_diff * 60) + alreadyBlock.getsTime_min()) - newBlock.getfTime_min();

                                int not_hour_diff2 = newBlock.getsTime_hour() - alreadyBlock.getfTime_hour();
                                int not_min_diff2 = ((not_hour_diff2 * 60) + newBlock.getsTime_min()) - alreadyBlock.getfTime_min();


                                if(not_min_diff > 0 || not_min_diff2 > 0)
                                {
                                    break;
                                }

                                //겹치는거
                                int hour_diff = alreadyBlock.getfTime_hour() - newBlock.getsTime_hour();
                                int min_diff = (hour_diff * 60 + alreadyBlock.getfTime_min()) - newBlock.getsTime_min();


                                int hour_diff2 = newBlock.getfTime_hour() - alreadyBlock.getsTime_hour();
                                int min_diff2 = (hour_diff2 * 60 + newBlock.getfTime_min()) - alreadyBlock.getsTime_min();

                                if(min_diff > 0 || min_diff2 > 0) { flag = 1; break; }
                            }
                            if(flag == 1 ) break;
                        }
                        if(flag == 1) break;
                    }

                    //정상적인 접근(겹치시는 시간 없음)
                    if(flag == 0)
                    {
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                ttVO.addSubjectSet(subjectSet);
                            }
                        });
                        //정상종료시 0 초기화
                        stackTop = -1;
                        finish();
                    }
                    else if(flag == 1)
                    {
                        Toast.makeText(context,"겹치는 시간이 존재합니다.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    public void addInfo()
    {
        LinearLayout newInfoGroup = new LinearLayout(context);
        LinearLayout.LayoutParams newInfoGroupParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        newInfoGroupParams.topMargin= 50 ;
        newInfoGroupParams.leftMargin= 60 ;
        newInfoGroup.setOrientation(LinearLayout.VERTICAL);

        //강의실명
        EditText setClassRoom = new EditText(context);
        setClassRoom.setHint("강의실명");

        LinearLayout.LayoutParams classRoomParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        classRoomParams.rightMargin=120;
        setClassRoom.setLayoutParams(classRoomParams);

        //요일,시간 레이아웃
        LinearLayout newInfo = new LinearLayout(context);
        LinearLayout.LayoutParams newInfoParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        newInfo.setOrientation(LinearLayout.HORIZONTAL);
        newInfo.setLayoutParams(newInfoParams);

        //요일선택
        final Button setWeekDay = new Button(context);
        setWeekDay.setText("월요일");
        setWeekDay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("요일 선택");
                builder.setSingleChoiceItems(R.array.weekDay, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {
                        String[] selectedText = getResources().getStringArray(R.array.weekDay);
                        setWeekDay.setText(selectedText[pos]);
                    }
                });

                builder.setPositiveButton("확인",null);
                builder.setNegativeButton("취소",null);

                builder.show();
            }
        });
        LinearLayout.LayoutParams weekDayParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        weekDayParams.gravity = Gravity.LEFT;
        setWeekDay.setLayoutParams(weekDayParams);

        //시간선택 1
        final Button setTime1 = new Button(context);
        setTime1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timeDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        setTime1.setText(hourOfDay + ":" + minute);
                    }
                },hour,minute,true);
                timeDialog.show();
            }
        });
        setTime1.setText("시작시각");
        LinearLayout.LayoutParams time1Pararms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setTime1.setLayoutParams(time1Pararms);

        //시간선택 2
        final Button setTime2 = new Button(context);
        setTime2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timeDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        setTime2.setText(hourOfDay + ":" + minute);
                    }
                },hour,minute,true);
                timeDialog.show();
            }
        });
        setTime2.setText("종료시각");
        LinearLayout.LayoutParams time2Pararms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setTime2.setLayoutParams(time2Pararms);

        newInfo.addView(setWeekDay,weekDayParams);
        newInfo.addView(setTime1,time1Pararms);
        newInfo.addView(setTime2,time2Pararms);

        newInfoGroup.addView(setClassRoom,classRoomParams);
        newInfoGroup.addView(newInfo);

        firstLL.addView(newInfoGroup,newInfoGroupParams);

        stack.push(newInfoGroup);

        firstLL.removeView(addBtn);
        firstLL.removeView(okayAdd);
        firstLL.addView(addBtn);
        firstLL.addView(okayAdd);
    }
}
