package com.tcy.mytally;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.tcy.mytally.adapter.AccountAdapter;
import com.tcy.mytally.db.AccountBean;
import com.tcy.mytally.db.DBManger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView todayLV;//展示今日收支情况的listView

    //声明数据源
    List<AccountBean> mDatas;

    //适配器
    AccountAdapter adapter;

    //
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todayLV = findViewById(R.id.main_lv);
        //设置时间
        initTime();

        //初始化数据源，设置适配器,加载每一行数据到List当中
        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(this, mDatas);
        todayLV.setAdapter(adapter);

    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();//得到日历对象
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    /*
     * onResume() --->当activity获取焦点时会调用的方法
     * 把数据的加载放到onResume当中，因为当返回的时候，数据还会变化
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadDBdata();
    }

    private void loadDBdata() {
        List<AccountBean> list = DBManger.getAccountListFromAccounttb(year, month, day);
        mDatas.clear();//清空原来的
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_iv_search:

                break;
            case R.id.main_btn_edit:
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intent);
                break;
            case R.id.main_btn_more:

                break;
        }
    }
}
