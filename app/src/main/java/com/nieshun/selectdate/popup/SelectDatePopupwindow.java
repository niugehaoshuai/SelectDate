package com.nieshun.selectdate.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.nieshun.selectdate.R;
import com.nieshun.selectdate.wheelview.NumericWheelAdapter;
import com.nieshun.selectdate.wheelview.OnWheelChangedListener;
import com.nieshun.selectdate.wheelview.OnWheelScrollListener;
import com.nieshun.selectdate.wheelview.WheelView;

import java.util.ArrayList;
import java.util.Calendar;


public class SelectDatePopupwindow extends PopupWindow implements OnClickListener {

    private Activity mContext;
    private View mMenuView;
    private TextView btn_submit, btn_cancel;
    private DateNumericAdapter yearAdapter, monthAdapter, dayAdapter;
    private WheelView year, month, day;
    //今天的日期，年月日
    private int mCurYear = 80, mCurMonth = 5, mCurDay = 14;
    public String title = "";
    public String date = "";
    private TextView tv_date_title;
    public ShowIsHide showIsHide;

    public interface ShowIsHide {
        void checkCurrentTime(String time);
    }
    public SelectDatePopupwindow(Activity context, ShowIsHide showIsHide) {
        super(context);
        mContext = context;
        //接口回调,显示日期
        this.showIsHide = showIsHide;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_data_popupwindow, null);

        year = (WheelView) mMenuView.findViewById(R.id.year);
        month = (WheelView) mMenuView.findViewById(R.id.month);
        day = (WheelView) mMenuView.findViewById(R.id.day);
        btn_submit = (TextView) mMenuView.findViewById(R.id.submit);
        btn_cancel = (TextView) mMenuView.findViewById(R.id.cancel);
        LinearLayout linout_date = (LinearLayout) mMenuView.findViewById(R.id.linout_date);
        linout_date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_submit.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(0x90000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(false);

    }

    private void setAdapter() {
        Calendar calendar= Calendar.getInstance();
        mCurYear=calendar.get(Calendar.YEAR);
        mCurMonth=calendar.get(Calendar.MONTH)+1;
        mCurDay=calendar.get(Calendar.DAY_OF_MONTH);
        String[] dates = date.split("/");

        int curYearIndex = Integer.parseInt(dates[0]);
        curYearIndex=curYearIndex-2002;
        if(String.valueOf(dates[1].charAt(0)).equals("0")){
            dates[1]=dates[1].replace("0","");
        }
        int curMonthIndex = Integer.parseInt(dates[1]);
        if(String.valueOf(dates[2].charAt(0)).equals("0")){
            dates[2]=dates[2].replace("0","");
        }
        int curDayIndex = Integer.parseInt(dates[2]);


        yearAdapter = new DateNumericAdapter(mContext, 2002, mCurYear, 100);
        yearAdapter.setTextType("年");
        yearAdapter.setItemResource(R.layout.item_birth);
        yearAdapter.setItemTextResource(R.id.item_birth);
        yearAdapter.setCurrentIndex(curYearIndex);
        year.setViewAdapter(yearAdapter);
        year.setCurrentItem(curYearIndex);
//        setAdapterAndIndex(year,yearAdapter,mCurYear, curYearIndex);

        year.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                yearAdapter.setCurrentIndex(wheel.getCurrentItem());
                updateMonths(year, month, day);
                updateDays(year, month, day);
            }

        });
        year.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {

                yearAdapter.setCurrentIndex(wheel.getCurrentItem());

                String currentText = (String) yearAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, yearAdapter);
            }
        });
        if(mCurYear== Integer.parseInt(dates[0])){
            monthAdapter = new DateNumericAdapter(mContext, 1, mCurMonth, mCurMonth - 1);
            monthAdapter.setItemResource(R.layout.item_birth);
            monthAdapter.setItemTextResource(R.id.item_birth);
            monthAdapter.setTextType("月");
            monthAdapter.setCurrentIndex(curMonthIndex - 1);
            month.setViewAdapter(monthAdapter);
            month.setCurrentItem(curMonthIndex - 1);
//            setAdapterAndIndex(month,monthAdapter,mCurMonth,curMonthIndex-1,"月");
        }else{
            monthAdapter = new DateNumericAdapter(mContext, 1, 12, mCurMonth - 1);
            monthAdapter.setItemResource(R.layout.item_birth);
            monthAdapter.setItemTextResource(R.id.item_birth);
            monthAdapter.setTextType("月");
            monthAdapter.setCurrentIndex(curMonthIndex - 1);
            month.setViewAdapter(monthAdapter);
            month.setCurrentItem(curMonthIndex - 1);
//            setAdapterAndIndex(month,monthAdapter,12,curMonthIndex-1,"月");
        }

        month.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                monthAdapter.setCurrentIndex(wheel.getCurrentItem());
                updateMonths(year, month, day);
                updateDays(year, month, day);
            }

        });
        month.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {

                monthAdapter.setCurrentIndex(wheel.getCurrentItem());
                String currentText = (String) monthAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, monthAdapter);

            }
        });
        if(mCurYear== Integer.parseInt(dates[0])&&mCurMonth==curMonthIndex){
            dayAdapter = new DateNumericAdapter(mContext, 1, mCurDay, mCurDay - 1);
            dayAdapter.setItemResource(R.layout.item_birth);
            dayAdapter.setItemTextResource(R.id.item_birth);
            dayAdapter.setTextType("日");
            dayAdapter.setCurrentIndex(curDayIndex-1);
            day.setViewAdapter(dayAdapter);
            day.setCurrentItem(curDayIndex - 1);
//            setAdapterAndIndex(day,dayAdapter,mCurDay,curDayIndex-1,"日");
        }else{
            calendar.set(Calendar.YEAR, year.getCurrentItem()+2002);
            calendar.set(Calendar.MONTH, month.getCurrentItem());
//            int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DATE, 1);
            calendar.roll(Calendar.DATE, -1);
            int maxDays = calendar.get(Calendar.DATE);
            dayAdapter = new DateNumericAdapter(mContext, 1, maxDays, mCurDay - 1);
            dayAdapter.setTextType("日");
            dayAdapter.setCurrentIndex(curDayIndex-1);
            dayAdapter.setItemResource(R.layout.item_birth);
            dayAdapter.setItemTextResource(R.id.item_birth);
            day.setViewAdapter(dayAdapter);
            day.setCurrentItem(curDayIndex - 1);
//            setAdapterAndIndex(day,dayAdapter,maxDays,curDayIndex-1,"日");
        }

        day.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                dayAdapter.setCurrentIndex(wheel.getCurrentItem());
            }

        });
        day.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                dayAdapter.setCurrentIndex(wheel.getCurrentItem());
                String currentText = (String) dayAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, dayAdapter);
//                updateDays(year, month, day);
            }
        });
    }


    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        TextView tv_date_title =  (TextView) mMenuView.findViewById(R.id.tv_date_title);
        tv_date_title.setText(title);
        setAdapter();
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void dismiss() {
        super.dismiss();

    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);

    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, DateNumericAdapter adapter) {
        ArrayList<View> arrayList = adapter.getArrayList();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            currentText = currentText.substring(0, currentText.length() - 1);
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(14);
                textvew.setTextColor(Color.parseColor("#ff961a"));
            } else {
                textvew.setTextColor(Color.parseColor("#909090"));
                textvew.setTextSize(14);
            }
        }
    }



    private void updateDays(WheelView year, WheelView month, WheelView day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2002+ year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        int maxDays = calendar.get(Calendar.DATE);
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);


        if (year.getCurrentItem() == yearAdapter.getMaxValue() - 2002 && monthAdapter.getMaxValue() != 12 && month.getCurrentItem() == monthAdapter.getMaxValue() - 1) {
            setAdapterAndIndex(day, dayAdapter, mCurDay, mCurDay - 1);
        } else {
            if (day.getCurrentItem() >= 26) {
                setAdapterAndIndex(day, dayAdapter, maxDays, curDay - 1);
            }
        }

    }

    private void updateMonths(WheelView year, WheelView month, WheelView day) {

        Calendar calendar = Calendar.getInstance();


        //年份是今年而且月份数不是不是12个月
        if (year.getCurrentItem() == yearAdapter.getMaxValue()-2002 && monthAdapter.getMaxValue() != mCurMonth) {
            if (month.getCurrentItem() >= mCurMonth - 1) {
                setAdapterAndIndex(month,monthAdapter,mCurMonth,mCurMonth - 1);
            } else {
                setAdapterAndIndex(month,monthAdapter,mCurMonth,month.getCurrentItem());
            }
            if (month.getCurrentItem() == mCurMonth - 1) {

                setAdapterAndIndex(day,dayAdapter,mCurDay,mCurDay - 1);
            } else {
                calendar.set(Calendar.YEAR, 2002+ year.getCurrentItem());
                calendar.set(Calendar.MONTH, month.getCurrentItem());
                calendar.set(Calendar.DATE, 1);
                calendar.roll(Calendar.DATE, -1);
                int maxDays = calendar.get(Calendar.DATE);
                setAdapterAndIndex(day,dayAdapter,maxDays,day.getCurrentItem());
            }
            //年份不是今年而且月份不是12个月的时候刷新，让月份恢复12个月，天数恢复满月
        } else if (year.getCurrentItem() != yearAdapter.getMaxValue()-2002 && monthAdapter.getMaxValue() != 12) {
            calendar.set(Calendar.YEAR, 2002+ year.getCurrentItem());
            calendar.set(Calendar.MONTH, month.getCurrentItem());
            calendar.set(Calendar.DATE, 1);
            calendar.roll(Calendar.DATE, -1);
            int maxDays = calendar.get(Calendar.DATE);
            setAdapterAndIndex(month,monthAdapter,12,month.getCurrentItem());
            setAdapterAndIndex(day,dayAdapter,maxDays,day.getCurrentItem());
            //年份是今年那么月份只有这个月之前的月份如果月份不选择在这个月，那么日期刷新，数据有全月的天数
        } else if (year.getCurrentItem() == yearAdapter.getMaxValue()-2002 && monthAdapter.getMaxValue() != 12 && month.getCurrentItem() != monthAdapter.getMaxValue() - 1) {
            calendar.set(Calendar.YEAR, 2002+ year.getCurrentItem());
            calendar.set(Calendar.MONTH, month.getCurrentItem());
            calendar.set(Calendar.DATE, 1);
            calendar.roll(Calendar.DATE, -1);
            int maxDays = calendar.get(Calendar.DATE);
            setAdapterAndIndex(day,dayAdapter,maxDays,day.getCurrentItem());
            //年份是今年那么月份只有这个月之前的月份而且月份选择在这个月，那么日期刷新，数据只有这个月之前的天数
        } else if (year.getCurrentItem() == yearAdapter.getMaxValue()-2002 && monthAdapter.getMaxValue() != 12 && month.getCurrentItem() == monthAdapter.getMaxValue() - 1) {
            setAdapterAndIndex(day,dayAdapter,mCurDay,mCurDay-1);
        }


    }

    private void setAdapterAndIndex(WheelView wheelView, NumericWheelAdapter adapter,int maxDays,int index) {
        setAdapterAndIndex(wheelView,adapter,maxDays,index,null);
    }
    private void setAdapterAndIndex(WheelView wheelView, NumericWheelAdapter adapter,int maxDays,int index,String type) {
        adapter.setMaxValue(maxDays);
        wheelView.setViewAdapter(adapter);
        adapter.setCurrentIndex(index);
        wheelView.setCurrentItem(index);
    }

    private class DateNumericAdapter extends NumericWheelAdapter {

        public DateNumericAdapter(Context context, int minValue, int maxValue,
                                  int current) {
            super(context, minValue, maxValue);
            setCurrentIndex(current);
            setMaxValue(maxValue);
        }

        protected void configureTextView(TextView view) {
            super.configureTextView(view);
        }


    }

    public void onClick(View v) {
        this.dismiss();
        switch (v.getId()) {
            case R.id.cancel:
                break;
            case R.id.submit:
                String years =  yearAdapter.getItemText(year.getCurrentItem()).toString().substring(0,4);
                String months = monthAdapter.getItemText(month.getCurrentItem()).toString();
                months=months.substring(0,months.length());
                if(months.length()==1){
                    months="0"+months;
                }
                String days = dayAdapter.getItemText(day.getCurrentItem()).toString();
                days=days.substring(0,days.length());
                if(days.length()==1){
                    days="0"+days;
                }
                String age = years + "/" + months + "/" + days;
                showIsHide.checkCurrentTime(age);
                break;
        }
    }

}
