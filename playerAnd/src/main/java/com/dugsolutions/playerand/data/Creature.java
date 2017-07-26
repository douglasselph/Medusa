package com.dugsolutions.playerand.data;

import com.dugsolutions.playerand.db.TableSkillDesc;
import com.dugsolutions.playerand.util.MathEval;
import com.dugsolutions.playerand.util.Roll;
import com.dugsolutions.playerand.util.RollGroup;
import com.dugsolutions.playerand.util.Values;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by dug on 7/12/17.
 */

public class Creature {

    public static Creature Gen(RaceCreature race) {
        return new Creature(race);
    }

    public long         id;
    public RaceCreature race;
    public String       name; // The name of this specific creature.
    public short        str;
    public short        con;
    public short        siz;
    public short        dex;
    public short        ins;
    public short        pow;
    public short        cha;
    public ArrayList<SkillRef> skills = new ArrayList();
    short     actionPoints; // computed
    RollGroup damageModifer; // computed

    public Creature(RaceCreature race) {
        this.race = race;
        roll();
    }

    public void roll() {
        str = race.str.roll();
        con = race.con.roll();
        siz = race.siz.roll();
        dex = race.dex.roll();
        ins = race.ins.roll();
        pow = race.pow.roll();
        cha = race.cha.roll();
    }

    public int getActionPoints() {
        if (actionPoints == 0) {
            actionPoints = (short) Math.ceil(((float) (ins + dex)) / 12f);
        }
        return actionPoints;
    }

    public int getInitialHitPoints() {
        return con + siz;
    }

    public int getMoveRate() {
        return 6;
    }

    public RollGroup getDamagerModifier() {
        if (damageModifer == null) {
            short value = (short) Math.floor((float) ((str + siz) - 21) / 5f * 2);
            if (value > 12) {
                damageModifer = new RollGroup(new Roll(1, 12, 0), new Roll(1, value - 12, 0));
            } else {
                damageModifer = new RollGroup(new Roll(1, value, 0));
            }
        }
        return damageModifer;
    }

    public int getStrikeRank() {
        return (ins + dex) / 2;
    }

    public int getTotalPoints() {
        return str + con + siz + dex + ins + pow + cha;
    }

    public void levelOut(int targetPoints) {
        int currentTotal;
        while ((currentTotal = getTotalPoints()) != targetPoints) {
            if (currentTotal < targetPoints) {
                int minValue = Values.getInstance().getMinValue(str, con, siz, dex, ins, pow, cha);
                incMatch(minValue, (short) 1);
            } else {
                int maxValue = Values.getInstance().getMinValue(str, con, siz, dex, ins, pow, cha);
                incMatch(maxValue, (short) -1);
            }
        }
    }

    void incMatch(int target, short amt) {
        if (str == target) {
            str += amt;
        } else if (con == target) {
            con += amt;
        } else if (dex == target) {
            dex += amt;
        } else if (ins == target) {
            ins += amt;
        } else if (pow == target) {
            pow += amt;
        } else if (siz == target) {
            siz += amt;
        } else if (cha == target) {
            cha += amt;
        } else {
            Timber.e("Could not find anything with value " + target);
            str += amt;
        }
    }

    public ArrayList<SkillRef> querySkills() {
        ArrayList<SkillDesc> descList = TableSkillDesc.getInstance().query();
        ArrayList<SkillRef>  skills   = new ArrayList();
        for (SkillDesc desc : descList) {
            skills.add(querySkill(desc));
        }
        return skills;
    }

    public SkillRef querySkill(SkillDesc desc) {
        for (SkillRef skill : skills) {
            if (skill.skill_desc_id == desc.id) {
                return skill;
            }
        }
        SkillRef skill = new SkillRef();
        skill.skill_desc_id = desc.id;
        String base = desc.base;
        if (base == null && desc.parent != null) {
            base = desc.parent.base;
        }
        if (base != null) {
            MathEval eval = new MathEval();
            eval.setConstant("STR", str);
            eval.setConstant("CON", con);
            eval.setConstant("SIZ", siz);
            eval.setConstant("DEX", dex);
            eval.setConstant("INT", ins);
            eval.setConstant("CHA", cha);
            eval.setConstant("POW", pow);
            eval.setConstant("KEN", pow);
            skill.value = (short) eval.evaluate(base);
        } else {
            Timber.e("Unexpected NULL base for skill " + desc.name);
        }
        return skill;
    }
}
