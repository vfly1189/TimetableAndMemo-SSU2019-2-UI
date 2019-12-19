package com.example.timetableandmemo;

import android.widget.Toast;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class SubjectSet extends RealmObject{

    private String subjectName; //과목명
    private String profName; //교수명
    private RealmList<SubjectBlock> sbList; //SubjectBlock의 리스트


    public SubjectSet() {}
    public SubjectSet(String subjectName, String profName)
    {
        sbList = new RealmList<>();
        this.subjectName = subjectName;
        this.profName = profName;
    }
    public SubjectSet(Subject subject){
        subjectName = subject.mSname;
        profName = subject.mName;

        /*
        if(subject.mDay1!=null)
            blockCount++;
        if(subject.mDay2!=null)
            blockCount++;
        if(subject.mDay3!=null)
            blockCount++;
        if(subject.mDay4!=null)
            blockCount++;*/
        //sbList = new SubjectBlock[blockCount];
    }

    public void add(SubjectBlock subjectBlock)
    {
       sbList.add(subjectBlock);
    }
    public String getSubjectName() { return subjectName; }
    public String getProfName() { return profName; }
    public List<SubjectBlock> getSubjectBlocks()
    {
        return sbList;
    }
}
