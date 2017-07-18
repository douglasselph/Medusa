package com.dugsolutions.playerand.data;

import com.dugsolutions.playerand.util.Roll;

/**
 * Created by dug on 7/11/17.
 */

public class RacePlayer extends RaceCreature {

    public String help;
    public Roll silver;

    public RacePlayer() {
        super();
    }

    public void copy(RacePlayer other) {
        super.copy(other);
        help = other.help;
        silver = other.silver;
    }
}
