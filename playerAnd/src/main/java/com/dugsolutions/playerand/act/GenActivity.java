package com.dugsolutions.playerand.act;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.dugsolutions.playerand.R;
import com.dugsolutions.playerand.app.MyApplication;

import android.support.v4.app.FragmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenActivity extends FragmentActivity implements GenCharsFragment.OnFragmentInteractionListener {

    enum Stage {
        CHARS,
        STATS;

        static Stage from(int ord) {
            for (Stage s : values()) {
                if (s.ordinal() == ord) {
                    return s;
                }
            }
            return CHARS;
        }
    }

    @BindView(R.id.next)               Button      mNext;
    @BindView(R.id.prev)               Button      mPrev;
    @BindView(R.id.roll)               Button      mRoll;
    @BindView(R.id.fragment_container) FrameLayout mFragmentContainer;

    MyApplication    mApp;
    GenCharsFragment mGenRaceFragment;
    StatsFragment    mStatsFragment;
    Stage mCurStage = Stage.CHARS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gen_activity);

        mApp = (MyApplication) getApplicationContext();

        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            mGenRaceFragment = new GenCharsFragment();
            mStatsFragment = new StatsFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mGenRaceFragment).commit();
        }
        mRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMiddle();
            }
        });
        mPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPrev();
            }
        });
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNext();
            }
        });
        mPrev.setEnabled(false);
    }

    void btnPrev() {
        if (mCurStage.ordinal() > Stage.CHARS.ordinal()) {
            mCurStage = Stage.from(mCurStage.ordinal() - 1);
            initStage();
        }
    }

    void btnNext() {
        if (mCurStage.ordinal() < Stage.STATS.ordinal()) {
            mCurStage = Stage.from(mCurStage.ordinal() + 1);
            initStage();
        }
    }

    void initButtons() {
        if (mCurStage.ordinal() > Stage.CHARS.ordinal()) {
            mPrev.setEnabled(true);
        } else {
            mPrev.setEnabled(false);
        }
        if (mCurStage.ordinal() < Stage.STATS.ordinal()) {
            mNext.setEnabled(true);
        } else {
            mNext.setEnabled(false);
        }
    }

    void initStage() {
        Fragment fragment = null;
        switch (mCurStage) {
            case CHARS:
                fragment = mGenRaceFragment;
                break;
            case STATS:
                fragment = mStatsFragment;
                break;
        }
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        initButtons();
    }

    void btnMiddle() {
        mGenRaceFragment.roll();
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

    @Override
    public void onTargetReached() {
        mNext.setEnabled(true);
    }

    @Override
    public void onTargetMissed() {
        mNext.setEnabled(false);
    }
}
