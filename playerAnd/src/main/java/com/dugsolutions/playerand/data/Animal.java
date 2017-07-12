package com.dugsolutions.playerand.data;

import com.dugsolutions.playerand.util.Roll;
import com.dugsolutions.playerand.util.RollGroup;

/**
 * Created by dug on 7/12/17.
 */

public class Animal {

    public static Animal Gen(RaceCreature race) {
        return new Animal(race);
    }

    public RaceCreature race;
    public String name;
    public short str;
    public short con;
    public short siz;
    public short dex;
    public short ins;
    public short ken;
    public short cha;

    short actionPoints;
    RollGroup damageModifer;

    public Animal(RaceCreature race) {
        this.race = race;
        str = race.str.roll();
        con = race.con.roll();
        siz = race.siz.roll();
        dex = race.dex.roll();
        ins = race.ins.roll();
        ken = race.ken.roll();
        cha = race.cha.roll();
    }

    int getActionPoints() {
        if (actionPoints == 0) {
            actionPoints = (short) Math.ceil(((float) (ins + dex)) / 12f);
        }
        return actionPoints;
    }

    RollGroup getDamagerModifier() {
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

}
