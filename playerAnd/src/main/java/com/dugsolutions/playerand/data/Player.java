package com.dugsolutions.playerand.data;

import com.dugsolutions.playerand.db.TableRaceCreatures;
import com.dugsolutions.playerand.util.Range;
import com.dugsolutions.playerand.util.Values;
import com.dugsolutions.playerand.util.WeightCategory;

/**
 * Created by dug on 7/12/17.
 */

public class Player extends Creature {

    public short height; // cm
    public short weight; // Kg
    public short luckPoints; // computed
    public short magicPoints; // computed
    public short silver;

    public Player(RaceCreature race) {
        super(race);
    }

    public void calcHeightAndWeightIfNeeded() {
        if (height == 0) {
            height = (short) getHeightRange().roll();
        }
        if (weight == 0) {
            weight = (short) getWeightRange(WeightCategory.Medium).roll();
        }
    }

    public int getExperienceMod() {
        int value = (int) Math.ceil(cha / 6) - 2;
        if (value < -1) {
            return -1;
        }
        return value;
    }

    public int getHealingRate() {
        return (int) Math.ceil(con / 6);
    }

    public Range getHeightRange() {
        return Values.getInstance().getHeightRange(siz);
    }

    public Range getWeightRange(WeightCategory cat) {
        return Values.getInstance().getWeightRange(cat, siz);
    }

    public int getInitialLuckPoints() {
        return Values.getInstance().getLuckPoints(pow);
    }

    public int getStrikeRank() {
        if (race.strikeRank == 0) {
            return (ins + dex) / 2;
        }
        return race.strikeRank;
    }

    public String getHeightStr() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(height);
        sbuf.append(" cm");
        return sbuf.toString();
    }

    public String getWeightStr() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(weight);
        sbuf.append(" Kg");
        return sbuf.toString();
    }
}
