package com.example.timetableandmemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timetableandmemo.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;

//import javax.security.auth.Subject;

public class BaseAdapterEx extends BaseAdapter {
    Context mContext = null;
    ArrayList<Subject> mData = null;
    ArrayList<Subject> mDataShow = null;
    LayoutInflater mLayoutInflater = null;
   // private OnClickEvent handler;

    public BaseAdapterEx(Context context, ArrayList<Subject> data) {
        mContext = context;
//        handler = (OnClickEvent) context;
        mData = data;
        mDataShow = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Subject getItem(int position) {
        return mData.get(position);
    }

    class ViewHolder {
        TextView nameTv;
        TextView snameTv;
        TextView TimeTv;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemLayout = convertView;
        ViewHolder viewHolder = null;


        if (itemLayout == null) {
            itemLayout = mLayoutInflater.inflate(R.layout.list_view_item_layout, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.snameTv = (TextView) itemLayout.findViewById(R.id.sname_text);
            viewHolder.nameTv = (TextView) itemLayout.findViewById(R.id.name_text);
            viewHolder.TimeTv = (TextView) itemLayout.findViewById(R.id.start1_text);

            itemLayout.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemLayout.getTag();
        }
        viewHolder.snameTv.setText(mData.get(position).mSname);
        viewHolder.nameTv.setText(mData.get(position).mName);
        viewHolder.TimeTv.setText(mData.get(position).mTime);


        Button button = (Button)itemLayout.findViewById(R.id.addButton);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              //  handler.onClick(mData.get(position));
                Subject data = mData.get(position);

                //Toast.makeText(mContext,Integer.toString(data.mShour2),Toast.LENGTH_SHORT).show();


                //요일 갯수
                int count = 0;
                int mon=0,tue=0,wed=0,thu=0,fri=0;

                String[] weekDay = new String[4];
                weekDay[0] = data.mDay1;
                weekDay[1] = data.mDay2;
                weekDay[2] = data.mDay3;
                weekDay[3] = data.mDay4;

                if(!(data.mDay1.equals(""))) count++;
                if(!(data.mDay2.equals(""))) count++;
                if(!(data.mDay3.equals(""))) count++;
                if(!(data.mDay4.equals(""))) count++;

                //Toast.makeText(mContext,data.mName,Toast.LENGTH_SHORT).show();


                //유효한 요일데이터들
                String[] weekDayData = new String[count];
                for(int i=0; i<count; i++)
                {
                    if(weekDay[i].equals("월")) weekDayData[i] = "월요일";
                    else if(weekDay[i].equals("화")) weekDayData[i] = "화요일";
                    else if(weekDay[i].equals("수")) weekDayData[i] = "수요일";
                    else if(weekDay[i].equals("목")) weekDayData[i] = "목요일";
                    else if(weekDay[i].equals("금")) weekDayData[i] = "금요일";
                    else if(weekDay[i].equals("토")) weekDayData[i] = "토요일";
                    else if(weekDay[i].equals("일")) weekDayData[i] = "일요일";
                }

                //시간 데이터들 배열화
                int startHour[] = {data.mShour1,data.mShour2};
                int endHour[] = {data.mFhour1,data.mFhour2};
                int startMin[] = {data.mSmin1,data.mSmin2};
                int endMin[] = {data.mFmin1,data.mFmin2};
                int Chour[] = {data.mCshour,data.mCfhour};
                int Cmin[] = {data.mCsmin,data.mCfmin};

                //Toast.makeText(mContext,Integer.toString(data.mCshour),Toast.LENGTH_SHORT).show();


                //강의실 정보 배열화
                String room[] = new String[2];
                room[0] = data.mRoom1;
                room[1] = data.mRoom2;


                SubjectBlock[] subjectBlocks = new SubjectBlock[count];

                //요일 4개짜리
                if(count == 4)
                {
                    subjectBlocks[0] = new SubjectBlock(room[0],weekDayData[0],Chour[0],Cmin[0],Chour[1],Cmin[1]);
                    subjectBlocks[1] = new SubjectBlock(room[0],weekDayData[1],Chour[0],Cmin[0],Chour[1],Cmin[1]);
                    subjectBlocks[2] = new SubjectBlock(room[0],weekDayData[2],Chour[0],Cmin[0],Chour[1],Cmin[1]);
                    subjectBlocks[3] = new SubjectBlock(room[0],weekDayData[3],Chour[0],Cmin[0],Chour[1],Cmin[1]);
                }
                //요일 2개짜리
                else if(count == 2)
                {
                    if(Chour[0] > 0)
                    {
                        for(int i=0; i<count; i++)
                        {
                            subjectBlocks[i] = new SubjectBlock(room[i],weekDayData[i],Chour[0],Cmin[0],Chour[1],Cmin[1]);
                        }
                    }
                    else if(startHour[0] > 0)
                    {
                        for(int i=0; i<count; i++)
                        {
                            subjectBlocks[i] = new SubjectBlock(room[i],weekDayData[i],startHour[i],startMin[i],endHour[i],endMin[i]);
                        }
                    }
                }
                //요일 1개 짜리
                else if(count == 1)
                {
                    subjectBlocks[0] = new SubjectBlock(room[0],weekDayData[0],Chour[0],Cmin[0],Chour[1],Cmin[1]);
                }

                final SubjectSet input = new SubjectSet(data.mSname,data.mName);

                for(int i=0; i<count; i++) input.add(subjectBlocks[i]);


                final Realm mRealm = Realm.getDefaultInstance();
                final TimetableVO ttVO = (TimetableVO)mRealm.where(TimetableVO.class).equalTo("id",0).findFirst();

                List<SubjectSet> Overlap = ttVO.getSubjectSets();
                Iterator<SubjectSet> iteratorSet = Overlap.iterator();

                int flag = 0;
                while(iteratorSet.hasNext())
                {
                    SubjectSet alreadyExistSubjectSet = iteratorSet.next();

                    List<SubjectBlock> alreadyExistSubjectBlock = alreadyExistSubjectSet.getSubjectBlocks();
                    Iterator<SubjectBlock> alreadyExistIterator = alreadyExistSubjectBlock.iterator();
                    List<SubjectBlock> newSubjectBlock = input.getSubjectBlocks();
                    Iterator<SubjectBlock> newSubjectIterator = newSubjectBlock.iterator();

                    while(alreadyExistIterator.hasNext())
                    {
                        SubjectBlock alreadyBlock = alreadyExistIterator.next();
                        while(newSubjectIterator.hasNext())
                        {
                            SubjectBlock newBlock = newSubjectIterator.next();

                            //요일이 같아야됨
                            if(!(newBlock.getWeekday().equals(alreadyBlock.getWeekday()))) break;

                            //안겹치는다는거
                            int not_hour_diff = alreadyBlock.getsTime_hour() - newBlock.getfTime_hour();
                            int not_min_diff = ((not_hour_diff * 60) + alreadyBlock.getsTime_min()) - newBlock.getfTime_min();

                            int not_hour_diff2 = newBlock.getsTime_hour() - alreadyBlock.getfTime_hour();
                            int not_min_diff2 = ((not_hour_diff2 * 60) + newBlock.getsTime_min()) - alreadyBlock.getfTime_min();


                            if(not_min_diff > 0 || not_min_diff2 > 0)
                            {
                                break;
                            }

                            //겹치는지 판별
                            int hour_diff = alreadyBlock.getfTime_hour() - newBlock.getsTime_hour();
                            int min_diff = (hour_diff * 60 + alreadyBlock.getfTime_min()) - newBlock.getsTime_min();

                            int hour_diff2 = newBlock.getfTime_hour() - alreadyBlock.getsTime_hour();
                            int min_diff2 = (hour_diff2 * 60 + newBlock.getfTime_min()) - alreadyBlock.getsTime_min();

                            if(min_diff > 0 || min_diff2 > 0) { flag = 1; break; }
                        }
                        if(flag == 1 ) break;
                    }
                    if(flag == 1) break;
                }


                if(flag == 0)
                {
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            ttVO.addSubjectSet(input);
                        }
                    });
                }
                else if(flag == 1)
                {
                    Toast.makeText(mContext,"겹치는 시간 존재",Toast.LENGTH_SHORT).show();
                }


            }
        });
        return itemLayout;

    }

}
