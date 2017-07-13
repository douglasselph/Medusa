package com.dugsolutions.playerand.data;

import com.dugsolutions.playerand.util.Range;
import com.dugsolutions.playerand.util.Values;
import com.dugsolutions.playerand.util.WeightCategory;

/**
 * Created by dug on 7/12/17.
 */

public class Player extends Creature {

    short height; // cm
    short weight; // Kg
    short luckPoints;

    public Player(RaceCreature race) {
        super(race);
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

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getLuckPoints() {
        return luckPoints;
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
}
