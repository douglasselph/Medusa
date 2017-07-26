package com.dugsolutions.playerand.act;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dugsolutions.playerand.R;
import com.dugsolutions.playerand.app.MyApplication;
import com.dugsolutions.playerand.data.Player;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    @BindView(R.id.attr_action_points_value) TextView mActionPointsValue;
    @BindView(R.id.attr_damage_mod_value)    TextView mDamageModValue;
    @BindView(R.id.attr_exp_mod_value)       TextView mExpModValue;
    @BindView(R.id.attr_healing_rate_value)  TextView mHealingRateValue;
    @BindView(R.id.attr_height)              TextView mHeightValue;
    @BindView(R.id.attr_weight)              TextView mWeightValue;
    @BindView(R.id.attr_hit_points)          TextView mHitPointsValue;
    @BindView(R.id.attr_luck_points)         TextView mLuckPointsValue;
    @BindView(R.id.attr_magic_points)        TextView mMagicPointsValue;
    @BindView(R.id.attr_move_rate)           TextView mMoveRateValue;
    @BindView(R.id.attr_strike_rank)         TextView mStrikeRankValue;

    MyApplication mApp;

    public StatsFragment() {
        // Required empty public constructor
    }

    public static StatsFragment newInstance() {
        StatsFragment fragment = new StatsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stats_fragment, container, false);

        mApp = (MyApplication) getActivity().getApplicationContext();

        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        fill();
    }

    void fill() {
        Player player = mApp.getPlayer();
        player.calcHeightAndWeightIfNeeded();
        mActionPointsValue.setText(Integer.toString(player.getActionPoints()));
        mDamageModValue.setText(player.getDamagerModifier().toString());
        mExpModValue.setText(Integer.toString(player.getExperienceMod()));
        mHealingRateValue.setText(Integer.toString(player.getHealingRate()));
        mHeightValue.setText(player.getHeightStr());
        mWeightValue.setText(player.getWeightStr());
        mHitPointsValue.setText(Integer.toString(player.getInitialHitPoints()));
        mLuckPointsValue.setText(Integer.toString(player.getInitialLuckPoints()));
        mMagicPointsValue.setText(Integer.toString(player.pow));
        mMoveRateValue.setText(Integer.toString(player.getMoveRate()));
        mStrikeRankValue.setText(Integer.toString(player.getStrikeRank()));
    }

}
