package com.dugsolutions.playerand.act;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.dugsolutions.playerand.R;
import com.dugsolutions.playerand.app.MyApplication;
import com.dugsolutions.playerand.data.RacePlayer;

import android.support.v4.app.FragmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class GenActivity extends FragmentActivity implements GenRaceFragment.OnFragmentInteractionListener {

    @BindView(R.id.next)               Button      mNext;
    @BindView(R.id.prev)               Button      mPrev;
    @BindView(R.id.roll)               Button      mRoll;
    @BindView(R.id.fragment_container) FrameLayout mFragmentContainer;
//    @BindView(R.id.main_list_frame) FrameLayout  mMainListFrame;
//    @BindView(R.id.main_list)       RecyclerView mMainList;
//    SimpleListAdapter mSimpleAdapter;

    MyApplication   mApp;
    GenRaceFragment mGenRaceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gen_activity);

        mApp = (MyApplication) getApplicationContext();

        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            mGenRaceFragment = new GenRaceFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mGenRaceFragment).commit();
        }
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        mMainList.setLayoutManager(linearLayoutManager);
//        DividerItemDecoration divider = new DividerItemDecoration(mMainList.getContext(), linearLayoutManager.getOrientation());
//        mMainList.addItemDecoration(divider);
//        mSimpleAdapter = new SimpleListAdapter(this, new SimpleListAdapter.OnItemSelectedListener() {
//            @Override
//            public void onSelectedItem(RacePlayer race) {
//                onSelected(race);
//            }
//        });
        mRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGenRaceFragment.roll();

            }
        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


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

    @Override
    public void onTargetReached() {
        mNext.setEnabled(true);
    }

    @Override
    public void onTargetMissed() {
        mNext.setEnabled(false);
    }
}
