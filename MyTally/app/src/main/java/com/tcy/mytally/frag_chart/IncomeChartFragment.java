package com.tcy.mytally.frag_chart;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcy.mytally.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeChartFragment extends Fragment {


    public IncomeChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_income_chart, container, false);
    }

}
