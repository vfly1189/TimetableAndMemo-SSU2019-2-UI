package com.example.timetableandmemo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class Testdialog{
    private Context context;
    private String subjectName;
    int Year,Month,Day;
    int startHour,startMin;
    List<TestVO> testList = new ArrayList<TestVO>();
    String changeHour, changeMin;
    TestAdapter testAdapter;
    Calendar myCalendar = Calendar.getInstance();



    public Testdialog(Context context,String subjectName,TestAdapter testAdapter) {
        this.context = context;
        this.subjectName = subjectName;
        this.testAdapter = testAdapter;
    }


    @SuppressLint("ClickableViewAccessibility")
    public void callFunction() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.testadd_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText date = (EditText) dlg.findViewById(R.id.inputdate);
        final EditText time = (EditText) dlg.findViewById(R.id.inputtime);
        final Button okButton = (Button) dlg.findViewById(R.id.okButton);
        final Button cancelButton = (Button) dlg.findViewById(R.id.cancelButton);


        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Year = year;
                            Month = month + 1;
                            Day = dayOfMonth;
                            date.setText(Year + "." + Month + "." + Day);
                        }
                    }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

                    datePickerDialog.show();
                }
            return true;
            }
        });
        time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view,MotionEvent event) {
               if(event.getAction() == MotionEvent.ACTION_DOWN) {
                   TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                       @Override
                       public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                           startHour = hourOfDay;
                           startMin = minute;
                           changeHour = String.format("%02d", startHour);
                           changeMin = String.format("%02d", startMin);
                           time.setText(changeHour + ":" + changeMin);

                       }
                   }, 12, 00, true);

                   timePickerDialog.show();
               }
            return true;
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                RealmConfiguration config = new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded()
                        .build();

                if(startHour==0||Month==0)
                    Toast.makeText(context, "날짜와 시간을 입력해주세요", Toast.LENGTH_SHORT).show();
                else {
                    Realm mRealm = Realm.getInstance(config);
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            TestVO vo1 = realm.createObject(TestVO.class);
                            vo1.setSubjectName(subjectName);
                            vo1.setYear(String.valueOf(Year));
                            vo1.setMonth(String.valueOf(Month));
                            vo1.setDay(String.valueOf(Day));
                            vo1.setStartHour(changeHour);
                            vo1.setStartMin(changeMin);
                            vo1.setPrintDate(date.getText().toString());
                            vo1.setPrintTime(time.getText().toString());
                        }
                    });

                    dlg.dismiss();
                    RealmResults<TestVO> testItem = mRealm.where(TestVO.class).equalTo("subjectName",subjectName).findAll();
                    testList = mRealm.copyFromRealm(testItem);
                    Log.d("hi","생성");
                    Toast.makeText(context, date.getText().toString(), Toast.LENGTH_SHORT).show();
                    Log.d("hi","알림");
                    Comparator<TestVO> test = new Comparator<TestVO>(){
                        @Override
                        public int compare(TestVO o1, TestVO o2) {
                            int year1 = Integer.parseInt(o1.getYear())*1000000;
                            int year2 = Integer.parseInt(o2.getYear())*1000000;
                            int month1 = Integer.parseInt(o1.getMonth())*10000;
                            int month2 = Integer.parseInt(o2.getMonth())*10000;
                            int day1 = Integer.parseInt(o1.getDay())*100;
                            int day2 = Integer.parseInt(o2.getDay())*100;
                            int hour1 = Integer.parseInt(o1.getStartHour())*10;
                            int hour2 = Integer.parseInt(o2.getStartHour())*10;
                            int min1 = Integer.parseInt(o1.getStartMin());
                            int min2 = Integer.parseInt(o2.getStartMin());
                            if(year1+month1+day1+hour1+min1>year2+month2+day2+hour2+min2)
                                return -1;
                            else if(year1+month1+day1+hour1+min1<year2+month2+day2+hour2+min2)
                                return 1;
                            else
                                return 0;
                        }
                    };
                    Collections.sort(testList,test);
                    testAdapter.clear();
                    testAdapter.add(testList);
                    Log.d("hi","끝");
                    //testAdapter.notifyDataSetChanged();
                }

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });

    }

}


