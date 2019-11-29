package com.example.timetableandmemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import javax.security.auth.Subject;

public class Sublist extends View {
    String Name;
    String Sname;
    Rect Space;
    int left;
    int right;
    int top;
    int bottom;


    public Sublist(Context context) {
        super(context);
    }
    public Sublist(Context context, Subject subject) {
        super(context);
        Name = subject.getmName();
        Sname = subject.getmSname();

        if(subject.mDay1.equals("월"))
            left =100;
        if(subject.mDay1.equals("화"))
            left =200;
        if(subject.mDay1.equals("수"))
            left =300;
        if(subject.mDay1.equals("목"))
            left =600;
        if(subject.mDay1.equals("금"))
            left =500;

        top = subject.mShour1*100;
        bottom = subject.mFhour1*10+100;
        right = left+200;

        Space = new Rect(0,0,right,bottom);
        //              요일 시작시간 고정값  종료시간
    }


    public Sublist(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sublist(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();

        paint.setTextSize(60);
        paint.setColor(0x0000FF);
        paint.setAlpha(250);
        canvas.drawRect(Space,paint);
        paint.setColor(Color.WHITE);
        canvas.drawText(Sname, 0, 60, paint);
        paint.setTextSize(30);
        canvas.drawText(Name, 0, 90, paint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(right-left, bottom-top);
    }
}
