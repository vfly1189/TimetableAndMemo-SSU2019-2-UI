package com.example.timetableandmemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class showTestAdapter extends ArrayAdapter {
    Context context;
    int resId;
    List<TestVO> mData = null;
    Calendar tCalendar = Calendar.getInstance();
    Calendar dCalendar = Calendar.getInstance();
    int D_Day;
    float t_millis = tCalendar.getTimeInMillis()/86400000;
    float d_millis;

    public showTestAdapter(Context context, int resId, List<TestVO> data) {
        super(context, resId);
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
        return super.getItemId(position);
    }

    public class Holder {
        public TextView subjectName;
        public TextView d_Day;
        public Holder(View root) {
            subjectName = root.findViewById(R.id.showsubjectname);
            d_Day = root.findViewById(R.id.showDday);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);
            Holder holder = new Holder(convertView);
            convertView.setTag(holder);
        }

        Holder holder = (Holder)convertView.getTag();

        TextView subjectName = holder.subjectName;
        TextView d_Day = holder.d_Day;

        final TestVO vo = mData.get(position);
        subjectName.setText(vo.getSubjectName());

        dCalendar.set(Integer.parseInt(vo.getYear()),Integer.parseInt(vo.getMonth())-1,Integer.parseInt(vo.getDay()));
//        dCalendar.set(2019,11,3);
        d_millis = dCalendar.getTimeInMillis()/86400000;

        float count = (d_millis - t_millis);
        D_Day = (int)count;

        d_Day.setText("D - "+String.valueOf(D_Day));

        return convertView;


    }
}
