package com.example.timetableandmemo;

import android.app.AlertDialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class AddTestAndAssignment extends AppCompatActivity implements View.OnClickListener {

    TestAdapter testAdapter;
    AssAdapter assAdapter;
    ImageButton testAdd;
    ImageButton assAdd;
    String name = null;
    List<TestVO> testList = new ArrayList<TestVO>();
    List<AssignmentVO> assList = new ArrayList<AssignmentVO>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Realm.init(this);
        /*
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        */

        setContentView(R.layout.activity_add_test_and_assignment);
        TextView subjectName = findViewById(R.id.Sname);
        ListView testView = findViewById(R.id.Test_list);
        ListView assignmentView = findViewById(R.id.Ass_list);
        testAdd = findViewById(R.id.Test_ADD);
        assAdd = findViewById(R.id.Ass_ADD);

        testAdd.setOnClickListener(this);
        assAdd.setOnClickListener(this);

        Intent intent = getIntent();
                name = intent.getStringExtra("subjectName");
                //Realm mRealm = Realm.getInstance(config);
                final Realm mRealm = Realm.getDefaultInstance();
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
//                TestVO vo1 = realm.createObject(TestVO.class);
//                vo1.setSubjectName("Hello");
//                vo1.setMonth("12");
//                vo1.setDay("1");
//                vo1.setStartHour("12");
//                vo1.setStartMin("30");

//                AssignmentVO vo2 = realm.createObject(AssignmentVO.class);
//                vo2.setSubjectName("Hello");
//                vo2.setMonth("12");
//                vo2.setDay("1");
//                vo2.setDeadlineHour("12");
//                vo2.setDeadlineMin("30");
            }
        });

        //String name = "과목 이름";
        subjectName.setText(name);

        final RealmResults<TestVO> testItem = mRealm.where(TestVO.class).equalTo("subjectName",name).findAll();
        testList = mRealm.copyFromRealm(testItem);
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
                    return 1;
                else if(year1+month1+day1+hour1+min1<year2+month2+day2+hour2+min2)
                    return -1;
                else
                    return 0;
            }
        };
        Collections.sort(testList,test);
        RealmResults<AssignmentVO> assignmentItem = mRealm.where(AssignmentVO.class).equalTo("subjectName",name).findAll();
        assList = mRealm.copyFromRealm(assignmentItem);
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
                    return 1;
                else if(year1+month1+day1+hour1+min1<year2+month2+day2+hour2+min2)
                    return -1;
                else
                    return 0;
            }
        };
        Collections.sort(assList,ass);
        testAdapter = new TestAdapter(this,R.layout.test_list,testList);
        assAdapter = new AssAdapter(this,R.layout.test_list,assList);

        testView.setAdapter(testAdapter);
        assignmentView.setAdapter(assAdapter);
        testView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int ID = (int)id;
                AlertDialog.Builder builder = new AlertDialog.Builder(AddTestAndAssignment.this);

                builder.setTitle("삭제하시겠습니까?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getApplicationContext(), "OK Click", Toast.LENGTH_SHORT).show();
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                TestVO delete = testList.get((int)ID);
                                TestVO find =mRealm.where(TestVO.class).equalTo("subjectName",delete.getSubjectName()).equalTo("year",delete.getYear()).equalTo("month",delete.getMonth())
                                        .equalTo("day",delete.getDay()).equalTo("startHour",delete.getStartHour()).equalTo("startMin",delete.getStartMin())
                                        .findFirst();
                                find.deleteFromRealm();
                            }
                        });
                        testAdapter.remove((int)ID);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });

        testView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddTestAndAssignment.this);
                builder.setTitle("정보");
                TestVO info = testList.get((int)id);
                builder.setMessage(String.format("\n날짜 : %s\n\n시간 : %s", info.getPrintDate(),info.getPrintTime()));
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        assignmentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddTestAndAssignment.this);
                builder.setTitle("정보");
                AssignmentVO info = assList.get((int)id);
                builder.setMessage(String.format("\n날짜 : %s\n\n시간 : %s", info.getPrintDate(),info.getPrintTime()));
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        assignmentView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int ID = (int)id;
                AlertDialog.Builder builder = new AlertDialog.Builder(AddTestAndAssignment.this);

                builder.setTitle("삭제하시겠습니까?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getApplicationContext(), "OK Click", Toast.LENGTH_SHORT).show();
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                AssignmentVO delete = assList.get((int)ID);
                                AssignmentVO find =mRealm.where(AssignmentVO.class).equalTo("subjectName",delete.getSubjectName()).equalTo("year",delete.getYear()).equalTo("month",delete.getMonth())
                                        .equalTo("day",delete.getDay()).equalTo("deadlineHour",delete.getDeadlineHour()).equalTo("deadlineMin",delete.getDeadlineMin())
                                        .findFirst();
                                find.deleteFromRealm();
                            }
                        });
                        assAdapter.remove((int)ID);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });

    }


    @Override
    public void onClick(View v) {
       // Toast.makeText(this,"in",Toast.LENGTH_SHORT).show();
        if(v == testAdd) {
            Testdialog testDialog = new Testdialog(this, name,testAdapter);
            testDialog.callFunction();
            Log.d("hi","hi");
        }
        else if(v == assAdd){
            Assdialog assDialog = new Assdialog(this,name,assAdapter);
            assDialog.callFunction();
        }
    }
}

