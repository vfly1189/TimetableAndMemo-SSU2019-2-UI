package com.example.timetableandmemo;

public class SubjectSet {

    String subjectName; //과목명
    String profName; //교수명
    int blockCount; //이 객체 내의 SubjectBlock 갯수
    int max_size; //이 객체의 저장용량
    SubjectBlock[] sbList; //SubjectBlock의 리스트

    SubjectSet(int max_size, String subjectName, String profName)
    {
        this.max_size = max_size;
        sbList = new SubjectBlock[max_size];
        blockCount = 0;

        this.subjectName = subjectName;
        this.profName = profName;
    }
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

    public void add(SubjectBlock subjectBlock)
    {
        //현재 꽉 참 ( 1개 정도 여유를 두고 꽉 찼다고 판정 내림 )
        if(blockCount == max_size - 1)
        {
            //나중에 구현
        }
        else
        {
            sbList[blockCount] = subjectBlock;
            blockCount++;
        }
    }

}
