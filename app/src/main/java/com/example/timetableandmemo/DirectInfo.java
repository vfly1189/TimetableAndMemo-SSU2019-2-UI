package com.example.timetableandmemo;

import android.os.Parcel;
import android.os.Parcelable;

public class DirectInfo implements Parcelable {
    private String weekDay;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;

    public DirectInfo() {}
    public DirectInfo(String weekday, int startHour, int startMinute, int endHour, int endMinute)
    {
        this.weekDay = weekday;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    public DirectInfo(Parcel in) {
        weekDay = in.readString();
        startHour = in.readInt();
        startMinute = in.readInt();
        endHour = in.readInt();
        endMinute = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(weekDay);
        dest.writeInt(startHour);
        dest.writeInt(startMinute);
        dest.writeInt(endHour);
        dest.writeInt(endMinute);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DirectInfo> CREATOR = new Creator<DirectInfo>() {
        @Override
        public DirectInfo createFromParcel(Parcel in) {
            return new DirectInfo(in);
        }

        @Override
        public DirectInfo[] newArray(int size) {
            return new DirectInfo[size];
        }
    };
}
