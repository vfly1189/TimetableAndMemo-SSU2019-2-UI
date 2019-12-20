package com.example.timetableandmemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    TextView timetableTitle;
    ImageButton buttonSubjectAdd;
    TableRow timetableWeekdaysRow;
    TableRow timetableContentRow;
    LinearLayout timetableColumn_time; //시간표의 첫열(시간 구분선)
    LinearLayout[] timetableColumn_weekdays = new LinearLayout[5]; //0: 월요일, 1: 화요일, 2: 수요일, 3: 목요일, 4: 금요일
    TimeTableManager ttManager = new TimeTableManager(this); //TimeTableManager 객체 하나 생성
    TimetableVO ttVO;
    AlertDialog titleChangeDialog;
    AlertDialog selectAddTypeDialog;

    static Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Realm 초기화
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
//        final Realm realm = Realm.getInstance(config);
        realm = Realm.getInstance(config);

        //activity_main.xml에서 id로 객체 찾기
        timetableTitle = (TextView)findViewById(R.id.timetable_title);
        buttonSubjectAdd = findViewById(R.id.button_subject_add);
        timetableWeekdaysRow = (TableRow)findViewById(R.id.timetable_weekdays_row);
        timetableContentRow = (TableRow)findViewById(R.id.timetable_content_row);

        //이후 사용할 timetableColumn_time과 timetableColumn_weekdays 객체 찾기
        timetableColumn_time = (LinearLayout)timetableContentRow.getChildAt(0);
        for (int i = 0; i < 5; i++) timetableColumn_weekdays[i] = (LinearLayout)timetableContentRow.getChildAt(i + 1);

        //제목 오래 누를 시의 동작 - 화면에 제목 바꿀 수 있는 다이얼로그 띄움
        timetableTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final EditText editText = new EditText(v.getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("시간표 제목 변경");
                builder.setView(editText);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String inputTitle = editText.getText().toString();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                //입력한 제목으로 DB Data변경
                                ttVO.setTitle(inputTitle);
                                //화면에서 제목 한번 갱신해주기
                                ttManager.setTitle();
                                ttManager.applyTitle(timetableTitle);
                            }
                        });
                    }
                });
                builder.setNegativeButton("취소", null);
                titleChangeDialog = builder.create();
                titleChangeDialog.show();
                return true;
            }
        });

        //'+'버튼 클릭시 동작
        buttonSubjectAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"DB로부터 과목 추가", "직접 과목정보를 입력해서 추가"};

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("과목을 시간표에 추가");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            //SubjectListActivity로 이동
                            Intent intent = new Intent(getApplicationContext(), SubjectListActivity.class);
                            startActivity(intent);
                        } else if (which == 1) {
                            //DirectAdd로 이동
                            Intent intent = new Intent(getApplicationContext(), DirectAdd.class);
                            startActivity(intent);
                        }
                    }
                });
                selectAddTypeDialog = builder.create();
                selectAddTypeDialog.show();
            }
        });

        //만들어진 시간표 객체가 존재하지 않으면 id=0로 객체 하나 생성
        if(!isTimetableVOExistInDB(realm)) { createTimetableVO(realm, 0, "길게 눌러 제목을 변경"); }

        //DB에 저장되어있는 TimetableVO객체 가져오기
        this.ttVO = realm.where(TimetableVO.class).equalTo("id", 0).findFirst();
    }


    @Override
    protected void onResume() {
        super.onResume();

        //TimeTableManager 설정 및 동작
        ttManager.setCurrentTimetableVO(this.ttVO);
        ttManager.setTitle();
        ttManager.calculateStartingAndEndingTimes();
        ttManager.calculateNumberOfHours();

        ttManager.applyTimetableWeekdaysRowText(timetableWeekdaysRow);
        ttManager.applyTitle(timetableTitle);
        ttManager.fillTimetableColumn_time(timetableColumn_time);
        for(int i = 0; i < 5; i ++) ttManager.fillTimetableContentRow(timetableColumn_weekdays[i], i); //각 요일에 SubjectBlock 배치
    }

    //RealmDB내에 TimetableVO객체가 존재하는지 확인
    private Boolean isTimetableVOExistInDB(Realm realm) {
        RealmQuery<TimetableVO> query = realm.where(TimetableVO.class);
        RealmResults<TimetableVO> results = query.findAll();
        if(results.size() == 0) return false;
        else return true;
    }

    //새로운 TimetableVO객체 생성
    public void createTimetableVO(Realm realm, int id, final String title) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TimetableVO vo = realm.createObject(TimetableVO.class);
                vo.setTitle(title);
            }
        });
    }

    //받은 과목명에 해당하는 SubjectSet을 Realm에서 삭제
    static public void deleteSubjectSet(String subjectName) {
        final SubjectSet sb = realm.where(SubjectSet.class).equalTo("subjectName", subjectName).findFirst();
        final RealmResults<TestVO> testVO = realm.where(TestVO.class).equalTo("subjectName",subjectName).findAll();
        final RealmResults<AssignmentVO> assignmentVO = realm.where(AssignmentVO.class).equalTo("subjectName",subjectName).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                sb.deleteFromRealm();
                testVO.deleteAllFromRealm();
                assignmentVO.deleteAllFromRealm();
            }
        });

    }
}
