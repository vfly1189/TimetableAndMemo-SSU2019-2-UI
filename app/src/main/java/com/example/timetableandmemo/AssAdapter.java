package com.example.timetableandmemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.List;

import io.realm.RealmResults;

public class AssAdapter extends ArrayAdapter {
    Context context;
    int resId;
    List<AssignmentVO> mData = null;
    Calendar tCalendar = Calendar.getInstance();
    Calendar dCalendar = Calendar.getInstance();
    int d_day;
    float t_millis = tCalendar.getTimeInMillis()/86400000;
    float d_millis;


    public AssAdapter(Context context, int resId, List<AssignmentVO> data) {
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
        TextView deadlineHour;
        TextView deadlineMin;
        TextView printDate;
        TextView dDay;


        ViewHolder(View root) {
//            subjectName = root.findViewById(R.id.printDate);
//            month = root.findViewById(R.id.month_test);
//            day = root.findViewById(R.id.day_test);
//            deadlineHour = root.findViewById(R.id.startHour_test);
//            deadlineMin = root.findViewById(R.id.startMin_test);
              printDate = root.findViewById(R.id.printDate);
                dDay = root.findViewById(R.id.Dday);
        }


    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
//        TextView subjectName = holder.subjectName;
//        TextView month = holder.month;
//        TextView day = holder.day;
//        TextView deadlineHour = holder.deadlineHour;
//        TextView deadlineMin = holder.deadlineMin;
          TextView printDate = holder.printDate;

        final AssignmentVO vo = mData.get(position);
//        subjectName.setText(vo.getSubjectName());
//        month.setText(vo.getMonth());
//        day.setText(vo.getDay());
//        deadlineHour.setText(vo.getDeadlineHour());
//        deadlineMin.setText(vo.getDeadlineMin());
          printDate.setText(vo.getPrintDate());
         TextView dDay = holder.dDay;

        dCalendar.set(Integer.parseInt(vo.getYear()),Integer.parseInt(vo.getMonth())-1,Integer.parseInt(vo.getDay()));
//        dCalendar.set(2019,11,3);
        d_millis = dCalendar.getTimeInMillis()/86400000;

        float count = (d_millis - t_millis);
        d_day = (int)count;

        dDay.setText("D - "+String.valueOf(d_day));

        return convertView;


    }

    public void add(List<AssignmentVO> addData){
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
