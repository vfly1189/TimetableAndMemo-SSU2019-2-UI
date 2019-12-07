package com.example.timetableandmemo;

import io.realm.RealmList;
import io.realm.RealmObject;

public class TimetableVO extends RealmObject {
    private int id;
    private String title;
    private RealmList<SubjectSet> subjectSetList;

    public TimetableVO() {}

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public String getTitle() { return this.title; }

    //시간표에 과목(SubjectSet) 추가
    public void addSubjectSet(SubjectSet subjectSet) {
        this.subjectSetList.add(subjectSet);
    }
}
