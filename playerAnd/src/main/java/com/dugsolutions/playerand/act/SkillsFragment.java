package com.dugsolutions.playerand.act;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dugsolutions.playerand.R;
import com.dugsolutions.playerand.app.MyApplication;
import com.dugsolutions.playerand.data.Player;
import com.dugsolutions.playerand.data.RacePlayer;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SkillsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SkillsFragment extends Fragment {

    @BindView(R.id.main_list) RecyclerView mMainList;
    SkillsListAdapter mSimpleAdapter;

    MyApplication mApp;

    public SkillsFragment() {
    }

    public static SkillsFragment newInstance(String param1, String param2) {
        SkillsFragment fragment = new SkillsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stats_fragment, container, false);

        mApp = (MyApplication) getActivity().getApplicationContext();

        ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mMainList.setLayoutManager(linearLayoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(mMainList.getContext(), linearLayoutManager.getOrientation());
        mMainList.addItemDecoration(divider);
        mSimpleAdapter = new SkillsListAdapter(getContext());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
