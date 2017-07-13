package com.dugsolutions.playerand.data;

import android.support.annotation.NonNull;

import com.dugsolutions.playerand.util.MathEval;
import com.dugsolutions.playerand.util.Range;
import com.dugsolutions.playerand.util.Values;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by dug on 7/12/17.
 */

public class RaceLocations {

    public static class RaceLocation implements Comparable<RaceLocation> {
        public long id;
        public String name;
        public String hpExpr;
        public short roll;

        @Override
        public int compareTo(@NonNull RaceLocation o) {
            return roll - o.roll;
        }
    }

    ArrayList<RaceLocation> locations = new ArrayList();
    public String baseExpr;

    public RaceLocations() {
    }

    public void add(RaceLocation location) {
        locations.add(location);
    }

    public void sort() {
        Collections.sort(locations);
    }

    public Locations getLocations(int con, int siz) {
        MathEval eval = Values.getInstance().getMath();
        eval.clear();
        eval.setConstant("SIZ", siz);
        eval.setConstant("CON", con);
        double base = eval.evaluate(baseExpr);
        eval.setConstant("base", base);
        Locations locs = new Locations(this);
        for (int i = 0; i < locations.size(); i++) {
            locs.hp[i] = (short) Math.round(eval.evaluate(locations.get(i).hpExpr));
        }
        return locs;
    }

    public ArrayList<RaceLocation> getLocations() {
        return locations;
    }
}
