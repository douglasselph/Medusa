package com.dugsolutions.playerand.act;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dugsolutions.playerand.R;
import com.dugsolutions.playerand.app.MyApplication;
import com.dugsolutions.playerand.data.Player;
import com.dugsolutions.playerand.data.SkillDesc;
import com.dugsolutions.playerand.data.SkillRef;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dug on 7/14/17.
 */

public class SkillsListAdapter extends RecyclerView.Adapter<SkillsListAdapter.CustomViewHolder> {

    protected class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.skill_name)  TextView skillName;
        @BindView(R.id.skill_value) TextView skillValue;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    MyApplication  mApp;
    List<SkillRef> mList;

    public SkillsListAdapter(Context context) {
        mApp = (MyApplication) context.getApplicationContext();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_skill, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        SkillRef  skill = mList.get(position);
        SkillDesc desc  = skill.getDesc();
        holder.skillName.setText(desc.name);
        holder.skillValue.setText(Integer.toString(skill.value) + "%");
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public void refresh() {
        Player player = mApp.getPlayer();
        mList = player.querySkills();
    }
}
