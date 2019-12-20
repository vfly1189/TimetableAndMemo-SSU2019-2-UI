package com.example.timetableandmemo;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.RealmResults;

public class TestAdapter extends ArrayAdapter<TestVO>{
    Context context;
    int resId;
    List<TestVO> mData = null;
    Calendar tCalendar = Calendar.getInstance();
    Calendar dCalendar = Calendar.getInstance();
    //    int tYear = tCalendar.get(Calendar.YEAR);
//    int tMonth = tCalendar.get(Calendar.MONTH);
//    int tDay = tCalendar.get(Calendar.DAY_OF_MONTH);
    int d_day;
    float t_millis = tCalendar.getTimeInMillis()/86400000;
    float d_millis;

    public TestAdapter(Context context, int resId, List<TestVO> data) {
        super(context,resId);
        this.context = context;
        this.resId = resId;
        this.mData = data;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    class ViewHolder {
        TextView subjectName;
        TextView month;
        TextView day;
        TextView startHour;
        TextView startMin;
        TextView printDate;
        TextView dDay;
        TextView story;

        ViewHolder(View root){
//            subjectName = root.findViewById(R.id.subjectName_test);
//            month = root.findViewById(R.id.month_test);
//            day = root.findViewById(R.id.day_test);
//            startHour = root.findViewById(R.id.startHour_test);
//            startMin = root.findViewById(R.id.startMin_test);
            printDate = root.findViewById(R.id.printDate);
            dDay = root.findViewById(R.id.Dday);
            story = root.findViewById(R.id.story);
        }
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder)convertView.getTag();
//        TextView subjectName = holder.subjectName;
//        TextView month = holder.month;
//        TextView day = holder.day;
//        TextView startHour = holder.startHour;
//        TextView startMin = holder.startMin;
        TextView printDate = holder.printDate;
        TextView dDay = holder.dDay;
        TextView story = holder.story;

        final TestVO vo = mData.get(position);
//        subjectName.setText(vo.getSubjectName());
//        month.setText(vo.getMonth());
//        day.setText(vo.getDay());
//        startHour.setText(vo.getStartHour());
//        startMin.setText(vo.getStartMin());
        printDate.setText(vo.getPrintDate());
        story.setText(vo.getStory());

        dCalendar.set(Integer.parseInt(vo.getYear()),Integer.parseInt(vo.getMonth())-1,Integer.parseInt(vo.getDay()));
//        dCalendar.set(2019,11,3);
        d_millis = dCalendar.getTimeInMillis()/86400000;

        float count = (d_millis - t_millis);
        d_day = (int)count;

        dDay.setText("D - "+String.valueOf(d_day));

        if(d_day <= 3)
        {
            dDay.setTextColor(Color.RED);
        }
        else
        {
            dDay.setTextColor(Color.BLACK);
        }

        return convertView;
    }

    public void add(int index,TestVO addData){
        Log.d("hi","add1");
        mData.add(index,addData);
        notifyDataSetChanged();
    }
    public void add(List<TestVO> addData){
        Log.d("hi","add2");
        for(int i=0;i<addData.size();i++){
            mData.add(0, addData.get(i));
        }
        notifyDataSetChanged();
    }
    public void clear(){
        Log.d("hi","clear");
        mData.clear();
        notifyDataSetChanged();
    }
    public void remove(int index){
        mData.remove(index);
        notifyDataSetChanged();
    }
}