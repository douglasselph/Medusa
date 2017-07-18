package com.dugsolutions.playerand.act;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dugsolutions.playerand.R;
import com.dugsolutions.playerand.data.RaceCreature;
import com.dugsolutions.playerand.data.RacePlayer;
import com.dugsolutions.playerand.db.TableRaceCreatures;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dug on 7/14/17.
 */

public class SimpleListAdapter extends RecyclerView.Adapter<SimpleListAdapter.CustomViewHolder> {


    public interface OnItemSelectedListener {
        void onSelectedItem(RacePlayer race);
    }

    protected class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.main_line)    TextView mainText;
        @BindView(R.id.main_subline) TextView subText;
        @BindView(R.id.help_details) Button   helpDetails;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    Context                mContext;
    OnItemSelectedListener mListener;
    List<RacePlayer>       mPlayerRaces;

    public SimpleListAdapter(Context context, OnItemSelectedListener listener) {
        mContext = context;
        mListener = listener;
        mPlayerRaces = TableRaceCreatures.getInstance().queryPlayers();
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_race, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        final RacePlayer race = mPlayerRaces.get(position);
        holder.mainText.setText(race.name);
        holder.subText.setText(race.help);
        holder.helpDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        holder.mainText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(race);
            }
        });
        holder.subText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(race);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlayerRaces.size();
    }

    void select(RacePlayer race) {
        if (mListener != null) {
            mListener.onSelectedItem(race);
        }
    }
}
