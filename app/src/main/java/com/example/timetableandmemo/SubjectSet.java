package com.example.timetableandmemo;

public class SubjectSet {
    String subjectName; //과목명
    String profName; //교수명
    int blockCount; //이 객체 내의 SubjectBlock 갯수
    SubjectBlock[] sbList = new SubjectBlock[this.blockCount]; //SubjectBlock의 리스트
}
