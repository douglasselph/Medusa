package com.dugsolutions.playerand.util;

/**
 * Created by dug on 7/13/17.
 */

public enum WeightCategory {
    Lithe,
    Medium,
    Heavy;

    int getWeightMax(int siz) {
        if (this == Lithe) {
            return siz * 5;
        } else if (this == Medium) {
            return siz * 7;
        } else if (this == Heavy) {
            return siz * 9;
        }
        return 0;
    }
}
