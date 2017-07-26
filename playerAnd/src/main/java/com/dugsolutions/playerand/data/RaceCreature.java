package com.dugsolutions.playerand.data;

import com.dugsolutions.playerand.util.Roll;

import java.util.ArrayList;

/**
 * Created by dug on 7/11/17.
 */
public class RaceCreature {
    public long                id;
    public String              name;
    public Roll                str;
    public Roll                con;
    public Roll                siz;
    public Roll                dex;
    public Roll                ins;
    public Roll                pow;
    public Roll                cha;
    public RaceLocations       locations;
    public ArrayList<SkillRef> skills = new ArrayList();
    public short               move; // meters
    public short               strikeRank;

    public void copy(RaceCreature other) {
        if (other.str != null) {
            str = other.str;
            con = other.con;
            siz = other.siz;
            dex = other.dex;
            ins = other.ins;
            pow = other.pow;
            cha = other.cha;
        }
        move = other.move;
        strikeRank = other.strikeRank;
        if (locations == null) {
            locations = new RaceLocations(other.locations);
        } else {
            locations.copy(other.locations);
        }
    }
}
