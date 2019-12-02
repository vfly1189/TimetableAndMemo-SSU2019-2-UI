package com.example.timetableandmemo;

import io.realm.RealmList;
import io.realm.RealmObject;

public class TimetableVO extends RealmObject {
    private String title;
    private RealmList<SubjectSet> subjectSetList;

    public TimetableVO(String title) {
        this.subjectSetList = new RealmList<>();
        this.title = title;
    }

    public void setTitle(String title) { this.title = title; }
    public String getTitle() { return this.title; }

    //시간표에 과목(SubjectSet) 추가
    public void addSubjectSet(SubjectSet subjectSet) {
        this.subjectSetList.add(subjectSet);
    }
}
