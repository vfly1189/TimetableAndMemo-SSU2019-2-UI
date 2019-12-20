package com.example.timetableandmemo;

import io.realm.RealmObject;

public class AssignmentVO extends RealmObject {
    private String subjectName;
    private String year;
    private String month;
    private String day;
    private String deadlineHour;
    private String deadlineMin;
    private String printDate;
    private String printTime;



    public String getSubjectName(){
        return this.subjectName;
    }
    public String getYear(){return this.year;}
    public String getMonth() {
        return this.month;
    }
    public String getDay(){
        return this.day;
    }
    public String getDeadlineHour(){
        return this.deadlineHour;
    }
    public String getDeadlineMin(){
        return this.deadlineMin;
    }
    public String getPrintDate(){return this.printDate;}
    public String getPrintTime(){return this.printTime;}


    public void setSubjectName(String subjectName){
        this.subjectName = subjectName;
    }
    public void setYear(String year){this.year = year;}
    public void setMonth(String month) {
        this.month = month;
    }
    public void setDay(String day){
        this.day = day;
    }
    public void setDeadlineHour(String deadlineHour){
        this.deadlineHour = deadlineHour;
    }
    public void setDeadlineMin(String deadlineMin){
        this.deadlineMin = deadlineMin;
    }
    public void setPrintDate(String printDate){this.printDate = printDate;}
    public void setPrintTime(String printTime){this.printTime = printTime;}


}
