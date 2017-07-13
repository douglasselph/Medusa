package com.dugsolutions.playerand.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dugsolutions.playerand.data.Creature;
import com.dugsolutions.playerand.data.Player;
import com.dugsolutions.playerand.data.RaceCreature;

/**
 * Created by dug on 7/13/17.
 */

public class TablePlayer extends TableCreature {
    static final String TABLE_NAME = "players";

    static final String KEY_SILVER       = "silver";
    static final String KEY_HEIGHT       = "height";
    static final String KEY_WEIGHT       = "weight";
    static final String KEY_LUCK_POINTS  = "luck_points";
    static final String KEY_MAGIC_POINTS = "magic_points";

    static TablePlayer sInstance;

    static void Init(SQLiteDatabase db) {
        sInstance = new TablePlayer(db);
    }

    public static TablePlayer getInstance() {
        return sInstance;
    }

    TablePlayer(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected void create(StringBuilder sbuf) {
        super.create(sbuf);
        sbuf.append(", ");
        sbuf.append(KEY_SILVER);
        sbuf.append(" smallint, ");
        sbuf.append(KEY_HEIGHT);
        sbuf.append(" smallint, ");
        sbuf.append(KEY_WEIGHT);
        sbuf.append(" smallint, ");
        sbuf.append(KEY_MAGIC_POINTS);
        sbuf.append(" smallint, ");
        sbuf.append(KEY_LUCK_POINTS);
        sbuf.append(" smallint");
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected void fill(ContentValues values, Creature creature) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            values.put(KEY_SILVER, player.silver);
            values.put(KEY_HEIGHT, player.height);
            values.put(KEY_WEIGHT, player.weight);
            values.put(KEY_LUCK_POINTS, player.luckPoints);
            values.put(KEY_MAGIC_POINTS, player.magicPoints);
        }
    }

    @Override
    protected Creature create(RaceCreature race) {
        return new Player(race);
    }

    @Override
    protected Creature fill(Cursor cursor) {
        Player player = (Player) super.fill(cursor);
        final int idxSilver = cursor.getColumnIndex(KEY_SILVER);
        final int idxHeight = cursor.getColumnIndex(KEY_HEIGHT);
        final int idxWeight = cursor.getColumnIndex(KEY_WEIGHT);
        final int idxLuckPoints = cursor.getColumnIndex(KEY_LUCK_POINTS);
        final int idxMagicPoints = cursor.getColumnIndex(KEY_MAGIC_POINTS);
        player.silver = cursor.getShort(idxSilver);
        player.height = cursor.getShort(idxHeight);
        player.weight = cursor.getShort(idxWeight);
        player.luckPoints = cursor.getShort(idxLuckPoints);
        player.magicPoints = cursor.getShort(idxMagicPoints);
        return player;
    }

}