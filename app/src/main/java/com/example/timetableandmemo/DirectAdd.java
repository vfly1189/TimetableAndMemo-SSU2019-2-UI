package com.example.timetableandmemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class DirectAdd extends AppCompatActivity {

    InputMethodManager imm;

    LinearLayout fLL;

    EditText lectureName;
    EditText professorName;
    EditText className;

    TextView viewWeekDay;
    TextView viewSetTime;

    Button setWeekDay;
    Button setTime;
    Button okayAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_add);

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        fLL = (LinearLayout)findViewById(R.id.firstLL);

        lectureName = (EditText)findViewById(R.id.lectureName);
        className  = (EditText)findViewById(R.id.className);
        professorName = (EditText)findViewById(R.id.professorName);

        viewWeekDay = (TextView)findViewById(R.id.viewWeekDay);
        viewSetTime = (TextView)findViewById(R.id.viewSetTime);

        setTime = (Button)findViewById(R.id.setTime);
        setTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectTime();
            }
        });
        setWeekDay = (Button)findViewById(R.id.setWeekDay);
        setWeekDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                selectWeekDay();
            }
        });

        okayAdd = (Button)findViewById(R.id.okayAdd);
        okayAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                //데이터 넘겨줘야됨
                finish();
            }
        });

        fLL.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                imm.hideSoftInputFromWindow(lectureName.getWindowToken(),0);
                imm.hideSoftInputFromWindow(professorName.getWindowToken(),0);
                imm.hideSoftInputFromWindow(className.getWindowToken(),0);
            }
        });
    }

    void selectWeekDay()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("요일 선택");
        builder.setSingleChoiceItems(R.array.weekDay, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {
                String[] selectedText = getResources().getStringArray(R.array.weekDay);
                viewWeekDay.setText(selectedText[pos]);
            }
        });

        builder.setPositiveButton("확인",null);
        builder.setNegativeButton("취소",null);

        builder.show();
    }

    void selectTime()
    {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timeDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                viewSetTime.setText(hourOfDay + ":" + minute);
            }
        },hour,minute,true);
        timeDialog.show();
    }

}
