package com.example.timetableandmemo;

import io.realm.RealmObject;

public class SubjectBlock extends RealmObject{
    private String classroomName; //강의실 이름
    private String weekday; //요일

    private int sTime_hour; //시작 시각의 '시' 부분
    private int sTime_min; //시작 시각의 '분' 부분

    private int fTime_hour; //끝나는 시각의 '시' 부분
    private int fTime_min; //끝나는 시각의 '분' 부분

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

    public String getClassroomName() { return classroomName; }
    public String getWeekday() { return weekday; }
    public int getsTime_hour() { return sTime_hour; }
    public int getsTime_min() { return sTime_min; }
    public int getfTime_hour() { return fTime_hour; }
    public int getfTime_min() { return fTime_min; }
}
