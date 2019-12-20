package com.example.timetableandmemo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.app.TimePickerDialog;
        import android.content.Context;
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

        import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

        import io.realm.Realm;
        import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class Assdialog{
    private Context context;
    private String subjectName;
    int Year,Month,Day;
    int deadlineHour,deadlineMin;
    List<AssignmentVO> assList = new ArrayList<AssignmentVO>();
    String changeHour, changeMin;
    AssAdapter assAdapter;
    Calendar myCalendar = Calendar.getInstance();



    public Assdialog(Context context, String subjectName, AssAdapter assAdapter) {
        this.context = context;
        this.subjectName = subjectName;
        this.assAdapter = assAdapter;
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
        final EditText story = (EditText) dlg.findViewById(R.id.story_text);
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
                            deadlineHour = hourOfDay;
                            deadlineMin = minute;
                            changeHour = String.format("%02d", deadlineHour);
                            changeMin = String.format("%02d", deadlineMin);
                            time.setText(changeHour + ":" + changeMin);

                        }
                    }, 12, 00, true);

                    timePickerDialog.show();
                }
                return true;
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RealmConfiguration config = new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded()
                        .build();

                if(deadlineHour==0||Month==0)
                    Toast.makeText(context, "날짜와 시간을 입력해주세요", Toast.LENGTH_SHORT).show();
                else {
                    Realm mRealm = Realm.getInstance(config);
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            AssignmentVO vo1 = realm.createObject(AssignmentVO.class);
                            vo1.setSubjectName(subjectName);
                            vo1.setYear(String.valueOf(Year));
                            vo1.setMonth(String.valueOf(Month));
                            vo1.setDay(String.valueOf(Day));
                            vo1.setDeadlineHour(changeHour);
                            vo1.setDeadlineMin(changeMin);
                            vo1.setPrintDate(date.getText().toString());
                            vo1.setPrintTime(time.getText().toString());
                            vo1.setStory(story.getText().toString());
                        }
                    });
                    dlg.dismiss();
                    RealmResults<AssignmentVO> assItem = mRealm.where(AssignmentVO.class).equalTo("subjectName",subjectName).findAll();
                    assList = mRealm.copyFromRealm(assItem);
                    Comparator<AssignmentVO> ass = new Comparator<AssignmentVO>(){
                        @Override
                        public int compare(AssignmentVO o1, AssignmentVO o2) {
                            int year1 = Integer.parseInt(o1.getYear())*1000000;
                            int year2 = Integer.parseInt(o2.getYear())*1000000;
                            int month1 = Integer.parseInt(o1.getMonth())*10000;
                            int month2 = Integer.parseInt(o2.getMonth())*10000;
                            int day1 = Integer.parseInt(o1.getDay())*100;
                            int day2 = Integer.parseInt(o2.getDay())*100;
                            int hour1 = Integer.parseInt(o1.getDeadlineHour())*10;
                            int hour2 = Integer.parseInt(o2.getDeadlineHour())*10;
                            int min1 = Integer.parseInt(o1.getDeadlineMin());
                            int min2 = Integer.parseInt(o2.getDeadlineMin());
                            if(year1+month1+day1+hour1+min1>year2+month2+day2+hour2+min2)
                                return -1;
                            else if(year1+month1+day1+hour1+min1<year2+month2+day2+hour2+min2)
                                return 1;
                            else
                                return 0;
                        }
                    };
                    Collections.sort(assList,ass);
                    assAdapter.clear();
                    assAdapter.add(assList);
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



