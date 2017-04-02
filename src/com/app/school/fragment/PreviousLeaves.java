package com.app.school.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.util.ArrayList;

import com.app.school.R;
import com.app.school.adapter.PreviousLeavesAdapter;
import com.app.school.bean.LeaveDescription;

/**
 * Created by admin on 17/08/16.
 */
public class PreviousLeaves extends Fragment
{

    ArrayList<LeaveDescription> previous_leave_list;
    String TAG = "PreviousLeaves";
    RecyclerView previous_leaves__recycle_view;
    StaggeredGridLayoutManager mStaggeredLayoutManager;

    public PreviousLeaves()
    {

    }

    public PreviousLeaves(ArrayList<LeaveDescription> c_previous_leave_list)
    {
        this.previous_leave_list = c_previous_leave_list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.previous_leaves_fragment_layout, container, false);
        previous_leaves__recycle_view = (RecyclerView) rootView.findViewById(R.id.previous_leaves__recycle_view);
        previous_leaves__recycle_view.setHasFixedSize(true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        previous_leaves__recycle_view.setLayoutManager(mStaggeredLayoutManager);

        PreviousLeavesAdapter previousLeavesAdapter = new PreviousLeavesAdapter(previous_leave_list, getActivity());

        previous_leaves__recycle_view.setAdapter(previousLeavesAdapter);

        return rootView;
    }

}
