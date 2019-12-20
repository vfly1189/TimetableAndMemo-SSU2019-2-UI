package com.example.timetableandmemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class showAll extends AppCompatActivity {

    List<TestVO> testList = new ArrayList<TestVO>();
    List<AssignmentVO> assList = new ArrayList<AssignmentVO>();
    float initX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);

        ListView testView = findViewById(R.id.showtest);
        ListView assView =findViewById(R.id.showass);

        TabHost tabHost = findViewById(R.id.host);
        tabHost.setup();

        TabHost.TabSpec spec = tabHost.newTabSpec("tab1");
        spec.setIndicator("Test");
        spec.setContent(R.id.showtest);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("tab2");
        spec.setIndicator("Ass");
        spec.setContent(R.id.showass);
        tabHost.addTab(spec);




        final Realm mRealm = Realm.getDefaultInstance();
        final RealmResults<TestVO> testItem = mRealm.where(TestVO.class).findAll();
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
        RealmResults<AssignmentVO> assignmentItem = mRealm.where(AssignmentVO.class).findAll();
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
        showTestAdapter showTestAdapter = new showTestAdapter(this,R.layout.show_all,testList);
        showAssAdapter showAssAdapter = new showAssAdapter(this,R.layout.show_all,assList);
        testView.setAdapter(showTestAdapter);
        assView.setAdapter(showAssAdapter);

    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            initX = event.getRawX();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            float diffX = initX - event.getRawX();
            if ((diffX < -30 )){
                startActivity(new Intent(showAll.this, MainActivity.class));
                overridePendingTransition(R.anim.move_right, R.anim.move_left);
            }
            finish();
        }
        return true;
    }
}
