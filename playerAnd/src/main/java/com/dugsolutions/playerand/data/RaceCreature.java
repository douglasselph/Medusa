package com.dugsolutions.playerand.data;

import com.dugsolutions.playerand.util.Roll;

/**
 * Created by dug on 7/11/17.
 */
public class RaceCreature {
    long id;
    public String name;
    public Roll str;
    public Roll con;
    public Roll siz;
    public Roll dex;
    public Roll ins;
    public Roll ken;
    public Roll cha;
    public RaceLocations locations;

    public short magicPoints;
    public short move;
    public short strikeRank;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
