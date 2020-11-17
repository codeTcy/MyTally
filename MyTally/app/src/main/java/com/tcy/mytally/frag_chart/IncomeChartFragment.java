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
public class IncomeChartFragment extends BaseChartFragment {

    int kind = 1;

    @Override
    public void onResume() {
        super.onResume();
        loadData(year, month, kind);

    }

    @Override
    public void setDate(int year, int month) {
        super.setDate(year, month);
        loadData(year, month, kind);
    }
}
