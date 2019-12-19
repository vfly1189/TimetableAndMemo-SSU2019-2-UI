package com.example.timetableandmemo;


import io.realm.RealmObject;

public class TestVO extends RealmObject {
    private String subjectName;
    private String year;
    private String month;
    private String day;
    private String startHour;
    private String startMin;
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
    public String getStartHour(){
        return this.startHour;
    }
    public String getStartMin(){
        return this.startMin;
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
    public void setStartHour(String startHour){
        this.startHour = startHour;
    }
    public void setStartMin(String startMin){
        this.startMin = startMin;
    }
    public void setPrintDate(String printDate){this.printDate = printDate;}
    public void setPrintTime(String printTime){this.printTime = printTime;}
}
