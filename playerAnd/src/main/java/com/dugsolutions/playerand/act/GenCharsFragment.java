package com.dugsolutions.playerand.act;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dugsolutions.playerand.R;
import com.dugsolutions.playerand.app.MyApplication;
import com.dugsolutions.playerand.data.Player;
import com.dugsolutions.playerand.data.RacePlayer;
import com.dugsolutions.playerand.db.TableRaceCreatures;
import com.dugsolutions.playerand.util.Roll;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenCharsFragment extends Fragment {

    public interface OnFragmentInteractionListener {
        void onTargetReached();

        void onTargetMissed();
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.prev_race)      ImageView    mPrevRace;
    @BindView(R.id.next_race)      ImageView    mNextRace;
    @BindView(R.id.race_name)      TextView     mRaceName;
    @BindView(R.id.race_help)      TextView     mRaceHelp;
    @BindView(R.id.str_value)      NumberPicker mStrValue;
    @BindView(R.id.con_value)      NumberPicker mConValue;
    @BindView(R.id.siz_value)      NumberPicker mSizValue;
    @BindView(R.id.dex_value)      NumberPicker mDexValue;
    @BindView(R.id.int_value)      NumberPicker mIntValue;
    @BindView(R.id.ken_value)      NumberPicker mPowValue;
    @BindView(R.id.cha_value)      NumberPicker mChaValue;
    @BindView(R.id.str_help)       TextView     mStrHelp;
    @BindView(R.id.con_help)       TextView     mConHelp;
    @BindView(R.id.siz_help)       TextView     mSizHelp;
    @BindView(R.id.dex_help)       TextView     mDexHelp;
    @BindView(R.id.int_help)       TextView     mIntHelp;
    @BindView(R.id.ken_help)       TextView     mPowHelp;
    @BindView(R.id.cha_help)       TextView     mChaHelp;
    @BindView(R.id.summary_points) TextView     mSummaryPoints;

    MyApplication    mApp;
    int              mRacePlayerIndex;
    int              mTargetPoints;
    RacePlayer       mRacePlayer;
    List<RacePlayer> mRacePlayers;

    public GenCharsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenCharsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenCharsFragment newInstance(String param1, String param2) {
        GenCharsFragment fragment = new GenCharsFragment();
//        Bundle          args     = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.gen_race_fragment, container, false);

        ButterKnife.bind(this, view);

        mApp = (MyApplication) getActivity().getApplicationContext();

        mPrevRace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextRace(-1);
            }
        });
        mNextRace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextRace(1);
            }
        });

        mRacePlayerIndex = 0;
        mRacePlayers = TableRaceCreatures.getInstance().queryPlayers();
        mTargetPoints = getResources().getInteger(R.integer.attr_target_points);

        mStrValue.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                mApp.getPlayer().str = (short) value;
                setTotalPoints();
            }
        });
        mConValue.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                mApp.getPlayer().con = (short) value;
                setTotalPoints();
            }
        });
        mSizValue.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                mApp.getPlayer().siz = (short) value;
                setTotalPoints();
            }
        });
        mDexValue.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                mApp.getPlayer().dex = (short) value;
                setTotalPoints();
            }
        });
        mIntValue.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                mApp.getPlayer().ins = (short) value;
                setTotalPoints();
            }
        });
        mPowValue.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                mApp.getPlayer().pow = (short) value;
                setTotalPoints();
            }
        });
        mChaValue.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                mApp.getPlayer().cha = (short) value;
                setTotalPoints();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initStats();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    void initStats() {
        mRacePlayer = mRacePlayers.get(mRacePlayerIndex);
        if (mApp.getPlayer() != null) {
            mApp.getPlayer().race = mRacePlayer;
        } else {
            mApp.setPlayer(new Player(mRacePlayer));
            mApp.getPlayer().levelOut(mTargetPoints);
        }
        mRaceName.setText(mRacePlayer.name);
        mRaceHelp.setText(mRacePlayer.help);

        Player player = mApp.getPlayer();

        set(mStrHelp, mStrValue, mRacePlayer.str, player.str);
        set(mConHelp, mConValue, mRacePlayer.con, player.con);
        set(mSizHelp, mSizValue, mRacePlayer.siz, player.siz);
        set(mDexHelp, mDexValue, mRacePlayer.dex, player.dex);
        set(mIntHelp, mIntValue, mRacePlayer.ins, player.ins);
        set(mPowHelp, mPowValue, mRacePlayer.pow, player.pow);
        set(mChaHelp, mChaValue, mRacePlayer.cha, player.cha);

        setTotalPoints();
    }

    void set(TextView help, NumberPicker picker, Roll roll, short value) {
        help.setText(roll.toString());
        picker.setMin(roll.getMin());
        picker.setMax(roll.getMax());
        picker.setValue(value);
    }

    void setTotalPoints() {
        int           totalPoints = mApp.getPlayer().getTotalPoints();
        StringBuilder sbuf        = new StringBuilder();
        sbuf.append(totalPoints);
        sbuf.append("/");
        sbuf.append(mTargetPoints);
        mSummaryPoints.setText(sbuf.toString());
        if (totalPoints == mTargetPoints) {
            mListener.onTargetReached();
        } else {
            mListener.onTargetMissed();
        }
    }

    void nextRace(int inc) {
        mRacePlayerIndex += inc;
        if (mRacePlayerIndex >= mRacePlayers.size()) {
            mRacePlayerIndex = 0;
        } else if (mRacePlayerIndex < 0) {
            mRacePlayerIndex = mRacePlayers.size() - 1;
        }
        initStats();
    }

    public void roll() {
        mApp.getPlayer().roll();
        mApp.getPlayer().levelOut(mTargetPoints);
        initStats();
    }
}
