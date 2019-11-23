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
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.StringTokenizer;

public class DirectAdd extends AppCompatActivity {

    Button addBtn;
    Button okayAdd;

    LinearLayout firstLL;
    Context context = this;

    ArrayStack<LinearLayout> stack = new ArrayStack(10);

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_add);

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
                DirectInfo[] information = new DirectInfo[count];
                for(int i=0; i<count; i++)
                    information[i] = new DirectInfo();


                if(stack.empty())
                {
                    finish();
                }
                else
                {
                    while(!stack.empty())
                    {
                        String start[] = new String[2];
                        String end[] = new String[2];

                        LinearLayout out = (LinearLayout) stack.top();


                        stack.pop();

                        Button startBtn = (Button)out.getChildAt(1);
                        StringTokenizer token1 = new StringTokenizer((String)startBtn.getText(),":");
                        int i=0;
                        while(token1.hasMoreTokens())
                        {
                            start[i] = token1.nextToken();
                            i++;
                        }


                        Button endBtn = (Button)out.getChildAt(2);
                        StringTokenizer token2 = new StringTokenizer((String)endBtn.getText(),":");
                        int j=0;
                        while(token2.hasMoreTokens())
                        {
                            end[j] = token2.nextToken();
                            j++;
                        }

                        Button weekDay = (Button)out.getChildAt(0);



                        int startHour = Integer.parseInt(start[0]);
                        int startMinute = Integer.parseInt(start[1]);
                        int endHour = Integer.parseInt(end[0]);
                        int endMinute = Integer.parseInt(end[1]);
                        Log.d(this.getClass().getName(),"test123");

                        information[k] = new DirectInfo((String)weekDay.getText(),startHour,startMinute,endHour,endMinute);

                        k++;
                    }
                    finish();
                }
            }
        });


    }

    public void addInfo()
    {
        LinearLayout newInfo = new LinearLayout(context);
        LinearLayout.LayoutParams newInfoParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        newInfoParams.topMargin = 50;
        newInfoParams.leftMargin = 60;
        newInfo.setOrientation(LinearLayout.HORIZONTAL);

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


        firstLL.addView(newInfo,newInfoParams);

        stack.push(newInfo);

        firstLL.removeView(addBtn);
        firstLL.removeView(okayAdd);
        firstLL.addView(addBtn);
        firstLL.addView(okayAdd);
    }
}
