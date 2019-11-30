package com.example.timetableandmemo;

import io.realm.RealmObject;

public class SubjectBlock extends RealmObject{
    String classroomName; //강의실 이름
    String weekday; //요일

    int sTime_hour; //시작 시각의 '시' 부분
    int sTime_min; //시작 시각의 '분' 부분

    int fTime_hour; //끝나는 시각의 '시' 부분
    int fTime_min; //끝나는 시각의 '분' 부분

    public SubjectBlock() {}
    SubjectBlock(String classroomName, String weekday, int sTime_hour, int sTime_min, int fTime_hour, int fTime_min)
    {
        this.classroomName = classroomName;
        this.weekday = weekday;
        this.sTime_hour = sTime_hour;
        this.sTime_min = sTime_min;
        this.fTime_hour = fTime_hour;
        this.fTime_min = fTime_min;
    }
}
