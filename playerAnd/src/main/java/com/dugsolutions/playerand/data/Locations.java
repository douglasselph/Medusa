package com.dugsolutions.playerand.data;

/**
 * Created by dug on 7/12/17.
 */

public class Locations {
    final RaceLocations race;
    public final short[] hp;

    public Locations(RaceLocations race) {
        this.race = race;
        hp = new short[race.locations.size()];
    }
}
