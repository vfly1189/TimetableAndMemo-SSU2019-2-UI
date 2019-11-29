package com.example.timetableandmemo;

public class SubjectSet {
    String subjectName; //과목명
    String profName; //교수명
    int blockCount; //이 객체 내의 SubjectBlock 갯수
    SubjectBlock[] sbList; //SubjectBlock의 리스트

    SubjectSet(Subject subject){
        subjectName = subject.mSname;
        profName = subject.mName;

        if(subject.mDay1!=null)
            blockCount++;
        if(subject.mDay2!=null)
            blockCount++;
        if(subject.mDay3!=null)
            blockCount++;
        if(subject.mDay4!=null)
            blockCount++;
        sbList = new SubjectBlock[blockCount];
    }

}
