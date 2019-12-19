package com.example.timetableandmemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class AddTestAndAssignment extends AppCompatActivity implements View.OnClickListener {

    TestAdapter testAdapter;
    AssAdapter assAdapter;
    Button testAdd;
    Button assAdd;

    String name = null;

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
        Realm mRealm = Realm.getDefaultInstance();
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

        RealmResults<TestVO> testItem = mRealm.where(TestVO.class).equalTo("subjectName",name).findAll();
        RealmResults<AssignmentVO> assignmentItem = mRealm.where(AssignmentVO.class).equalTo("subjectName",name).findAll();
        testAdapter = new TestAdapter(this,R.layout.test_list,testItem);
        assAdapter = new AssAdapter(this,R.layout.test_list,assignmentItem);
        testView.setAdapter(testAdapter);
        assignmentView.setAdapter(assAdapter);
    }


    @Override
    public void onClick(View v) {
       // Toast.makeText(this,"in",Toast.LENGTH_SHORT).show();
        if(v == testAdd) {
            Testdialog testDialog = new Testdialog(this, name,testAdapter);
            testDialog.callFunction();
        }
        else if(v == assAdd){
            Assdialog assDialog = new Assdialog(this,name,assAdapter);
            assDialog.callFunction();
        }
    }
}

