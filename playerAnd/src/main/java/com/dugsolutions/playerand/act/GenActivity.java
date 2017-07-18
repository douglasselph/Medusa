package com.dugsolutions.playerand.act;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dugsolutions.playerand.R;
import com.dugsolutions.playerand.data.Player;
import com.dugsolutions.playerand.data.RacePlayer;
import com.dugsolutions.playerand.db.TableRaceCreatures;
import com.dugsolutions.playerand.util.Roll;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class GenActivity extends AppCompatActivity {

    enum Stage {
        CHARS,
    }

    @BindView(R.id.next)            Button       mNext;
    @BindView(R.id.prev)            Button       mPrev;
    @BindView(R.id.roll)            Button       mRoll;
    @BindView(R.id.frame_chars)     ViewGroup    mFrameChars;
    @BindView(R.id.main_list_frame) FrameLayout  mMainListFrame;
    @BindView(R.id.main_list)       RecyclerView mMainList;
    @BindView(R.id.prev_race)       ImageView    mPrevRace;
    @BindView(R.id.next_race)       ImageView    mNextRace;
    @BindView(R.id.race_name)       TextView     mRaceName;
    @BindView(R.id.str_value)       NumberPicker mStrValue;
    @BindView(R.id.con_value)       NumberPicker mConValue;
    @BindView(R.id.siz_value)       NumberPicker mSizValue;
    @BindView(R.id.dex_value)       NumberPicker mDexValue;
    @BindView(R.id.int_value)       NumberPicker mIntValue;
    @BindView(R.id.ken_value)       NumberPicker mPowValue;
    @BindView(R.id.cha_value)       NumberPicker mChaValue;
    @BindView(R.id.str_help)        TextView     mStrHelp;
    @BindView(R.id.con_help)        TextView     mConHelp;
    @BindView(R.id.siz_help)        TextView     mSizHelp;
    @BindView(R.id.dex_help)        TextView     mDexHelp;
    @BindView(R.id.int_help)        TextView     mIntHelp;
    @BindView(R.id.ken_help)        TextView     mPowHelp;
    @BindView(R.id.cha_help)        TextView     mChaHelp;

    Player            mPlayer;
    int               mRacePlayerIndex;
    RacePlayer        mRacePlayer;
    List<RacePlayer>  mRacePlayers;
    Stage             mCurStage;
    SimpleListAdapter mSimpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gen_activity);

        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mMainList.setLayoutManager(linearLayoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(mMainList.getContext(), linearLayoutManager.getOrientation());
        mMainList.addItemDecoration(divider);
        mSimpleAdapter = new SimpleListAdapter(this, new SimpleListAdapter.OnItemSelectedListener() {
            @Override
            public void onSelectedItem(RacePlayer race) {
                onSelected(race);
            }
        });
        mRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.roll();
                fillStage();
            }
        });
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCurStage = Stage.CHARS;
        mRacePlayerIndex = 0;
        mRacePlayers = TableRaceCreatures.getInstance().queryPlayers();

        fillStage();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void fillStage() {
        mMainListFrame.setVisibility(View.GONE);
        mFrameChars.setVisibility(View.GONE);

        switch (mCurStage) {
            case CHARS:
                mFrameChars.setVisibility(View.VISIBLE);
                mRacePlayer = mRacePlayers.get(mRacePlayerIndex);
                if (mPlayer != null) {
                    mPlayer.race = mRacePlayer;
                } else {
                    mPlayer = new Player(mRacePlayer);
                }
                mRaceName.setText(mRacePlayer.name);
                set(mStrHelp, mStrValue, mRacePlayer.str, mPlayer.str);
                set(mConHelp, mConValue, mRacePlayer.con, mPlayer.con);
                set(mSizHelp, mSizValue, mRacePlayer.siz, mPlayer.siz);
                set(mDexHelp, mDexValue, mRacePlayer.dex, mPlayer.dex);
                set(mIntHelp, mIntValue, mRacePlayer.ins, mPlayer.ins);
                set(mPowHelp, mPowValue, mRacePlayer.pow, mPlayer.pow);
                set(mChaHelp, mChaValue, mRacePlayer.cha, mPlayer.cha);
                break;
        }
    }

    void set(TextView help, NumberPicker picker, Roll roll, short value) {
        help.setText(roll.toString());
        picker.setMin(roll.getMin());
        picker.setMax(roll.getMax());
        picker.setValue(value);
    }

    void nextRace(int inc) {
        mRacePlayerIndex += inc;
        if (mRacePlayerIndex >= mRacePlayers.size()) {
            mRacePlayerIndex = 0;
        } else if (mRacePlayerIndex < 0) {
            mRacePlayerIndex = mRacePlayers.size()-1;
        }
        fillStage();
    }

    void onSelected(RacePlayer race) {
    }
}
