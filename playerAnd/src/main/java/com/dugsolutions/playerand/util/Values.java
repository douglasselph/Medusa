package com.dugsolutions.playerand.util;

/**
 * Created by dug on 7/12/17.
 */

public class Values {

    public static void Init() {
        sInstance = new Values();
    }

    public static Values getInstance() {
        return sInstance;
    }

    static Values sInstance;

    MathEval mathEval;

    private Values() {
        sInstance = this;
        mathEval = new MathEval();
    }

    public Range getWeightRange(WeightCategory cat, int siz) {
        return new Range(cat.getWeightMax(siz - 1) + 1, cat.getWeightMax(siz));
    }

    public Range getHeightRange(int siz) {
        return new Range(getHeightMax(siz - 1) + 1, getHeightMax(siz));
    }

    int getHeightMax(int siz) {
        if (siz <= 0) {
            return 0;
        } else if (siz == 1) {
            return 45;
        } else if (siz == 2) {
            return 80;
        } else if (siz == 3) {
            return 105;
        } else if (siz < 8) {
            return (siz - 4) * 10 + 120;
        } else {
            return (siz - 8) * 5 + 155;
        }
    }

    public MathEval getMath() {
        return mathEval;
    }

    public int getLuckPoints(int pow) {
        return (int) Math.ceil(pow / 6);
    }
}

