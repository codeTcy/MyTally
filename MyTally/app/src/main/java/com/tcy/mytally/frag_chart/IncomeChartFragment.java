package com.tcy.mytally.frag_chart;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tcy.mytally.R;
import com.tcy.mytally.adapter.ChartItemAdapter;
import com.tcy.mytally.db.ChartItemBean;
import com.tcy.mytally.db.DBManger;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeChartFragment extends Fragment {

    ListView chartLv;

    //.field得到的
    private int year;
    private int month;

    //数据源
    List<ChartItemBean> mDatas;

    //.field得到
    private ChartItemAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income_chart, container, false);
        chartLv = view.findViewById(R.id.frag_chart_lv);
        //获取Activity传递的数据
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");
        //设置数据源
        mDatas = new ArrayList<>();
        //设置适配器
        adapter = new ChartItemAdapter(getContext(), mDatas);
        chartLv.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(year, month, 1);

    }

    public void loadData(int year, int month, int kind) {
        List<ChartItemBean> list = DBManger.getChartListFromAccounttb(year, month, kind);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();//提示更新
    }
}
