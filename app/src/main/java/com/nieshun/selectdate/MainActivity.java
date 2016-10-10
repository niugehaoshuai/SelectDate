package com.nieshun.selectdate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.nieshun.selectdate.popup.SelectDatePopupwindow;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,SelectDatePopupwindow.ShowIsHide{
    private TextView tv_selectdate;
    private SelectDatePopupwindow selectDatePopupwindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
    }

    private void initview() {
        tv_selectdate= (TextView) findViewById(R.id.tv_selectdate);
        tv_selectdate.setOnClickListener(this);
        String currenttime = getSystemTime();
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime date = DateTime.parse(currenttime, format);
        tv_selectdate.setText(date.toString("yyyy/MM/dd"));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_selectdate:
                showPopWindow("请选择起始日期",tv_selectdate.getText().toString());
                break;
        }
    }
    private void showPopWindow(String title, String date) {
        if (selectDatePopupwindow == null) {
            selectDatePopupwindow = new SelectDatePopupwindow(this, this);
            selectDatePopupwindow.title = title;
            selectDatePopupwindow.date = date;
            selectDatePopupwindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else {
            selectDatePopupwindow.title = title;
            selectDatePopupwindow.date = date;
            selectDatePopupwindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }

    }

    @Override
    public void checkCurrentTime(String time) {
        tv_selectdate.setText(time);
    }
    public String getSystemTime() {
        return getSystemTime("yyyyMMdd");
    }
    /**
     *
     * @param format 返回系统时间格式
     * @return
     */
    public String getSystemTime(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
}
