package com.dugsolutions.playerand.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dugsolutions.playerand.data.RaceCreature;
import com.dugsolutions.playerand.data.RacePlayer;
import com.dugsolutions.playerand.util.Roll;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by dug on 7/12/17.
 */

public class TableRaceCreatures {

    static final String TABLE_NAME = "race_creatures";

    static final String KEY_ROWID       = "_id";
    static final String KEY_NAME        = "name";
    static final String KEY_STR         = "str";
    static final String KEY_CON         = "con";
    static final String KEY_SIZ         = "siz";
    static final String KEY_DEX         = "dex";
    static final String KEY_INT         = "ins";
    static final String KEY_POW         = "pow";
    static final String KEY_CHA         = "cha";
    static final String KEY_LOC_BASE    = "loc_base";
    static final String KEY_MOVE        = "move";
    static final String KEY_STRIKE_RANK = "sr";
    static final String KEY_HELP      = "help";
    static final String KEY_SILVER    = "silver";
    static final String KEY_IS_PLAYER = "is_player";

    static TableRaceCreatures sInstance;

    static void Init(SQLiteDatabase db) {
        new TableRaceCreatures(db);
    }

    public static TableRaceCreatures getInstance() {
        return sInstance;
    }

    final SQLiteDatabase mDb;

    TableRaceCreatures(SQLiteDatabase db) {
        this.mDb = db;
        sInstance = this;
    }

    public void create() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append("create table ");
        sbuf.append(TABLE_NAME);
        sbuf.append(" (");
        sbuf.append(KEY_ROWID);
        sbuf.append(" integer primary key autoincrement, ");
        sbuf.append(KEY_NAME);
        sbuf.append(" nchar(256), ");
        sbuf.append(KEY_STR);
        sbuf.append(" nchar(64), ");
        sbuf.append(KEY_CON);
        sbuf.append(" nchar(64), ");
        sbuf.append(KEY_SIZ);
        sbuf.append(" nchar(64), ");
        sbuf.append(KEY_DEX);
        sbuf.append(" nchar(64), ");
        sbuf.append(KEY_INT);
        sbuf.append(" nchar(64), ");
        sbuf.append(KEY_POW);
        sbuf.append(" nchar(64), ");
        sbuf.append(KEY_CHA);
        sbuf.append(" nchar(64), ");
        sbuf.append(KEY_LOC_BASE);
        sbuf.append(" nchar(64), ");
        sbuf.append(KEY_MOVE);
        sbuf.append(" tinyint, ");
        sbuf.append(KEY_STRIKE_RANK);
        sbuf.append(" tinyint, ");
        sbuf.append(KEY_HELP);
        sbuf.append(" text, ");
        sbuf.append(KEY_SILVER);
        sbuf.append(" nchar(64), ");
        sbuf.append(KEY_IS_PLAYER);
        sbuf.append(" bit)");
        mDb.execSQL(sbuf.toString());
    }

    public void store(RaceCreature creature) {
        mDb.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            fill(values, creature);
            if (creature instanceof RacePlayer) {
                fill(values, (RacePlayer) creature);
            }
            if (creature.id > 0) {
                String where = KEY_ROWID + "=?";
                String[] whereArgs = {Long.toString(creature.id)};
                mDb.update(TABLE_NAME, values, where, whereArgs);
            } else {
                creature.id = mDb.insert(TABLE_NAME, null, values);
            }
            TableRaceLocations.getInstance().store(creature.id, creature.locations);
            mDb.setTransactionSuccessful();
        } catch (Exception ex) {
            Timber.e(ex);
        } finally {
            mDb.endTransaction();
        }
    }

    void fill(ContentValues values, RaceCreature creature) {
        values.put(KEY_NAME, creature.name);
        values.put(KEY_STR, creature.str.toString());
        values.put(KEY_CON, creature.con.toString());
        values.put(KEY_SIZ, creature.siz.toString());
        values.put(KEY_DEX, creature.dex.toString());
        values.put(KEY_INT, creature.ins.toString());
        values.put(KEY_POW, creature.pow.toString());
        values.put(KEY_CHA, creature.cha.toString());
        values.put(KEY_LOC_BASE, creature.locations.baseExpr);
        values.put(KEY_MOVE, creature.move);
        values.put(KEY_STRIKE_RANK, creature.strikeRank);
    }

    void fill(ContentValues values, RacePlayer creature) {
        values.put(KEY_SILVER, creature.silver.toString());
        values.put(KEY_HELP, creature.help);
        values.put(KEY_IS_PLAYER, 1);
    }

    public RaceCreature query(String name) {
        return query(null, name);
    }

    public RacePlayer queryPlayer(String name) {
        RaceCreature creature = query(new RacePlayer(), name);
        return (RacePlayer) creature;
    }

    public RaceCreature query(RaceCreature creature, String name) {
        if (creature == null) {
            creature = new RaceCreature();
        }
        String selection = KEY_NAME + "=?";
        String[] selectionArgs = {name};
        Cursor cursor = mDb.query(TABLE_NAME, null, selection, selectionArgs, null, null, null, null);
        if (cursor.getCount() >= 1) {
            if (cursor.moveToFirst()) {
                creature.name = name;
                fill(cursor, creature);
            }
            if (cursor.getCount() > 1) {
                Timber.e("Found too many creatures with the name: " + name);
            }
        }
        cursor.close();
        return creature;
    }

    void fill(Cursor cursor, RaceCreature creature) {
        final int idxRowId = cursor.getColumnIndex(KEY_ROWID);
        final int idxStr = cursor.getColumnIndex(KEY_STR);
        final int idxCon = cursor.getColumnIndex(KEY_CON);
        final int idxSiz = cursor.getColumnIndex(KEY_SIZ);
        final int idxDex = cursor.getColumnIndex(KEY_DEX);
        final int idxInt = cursor.getColumnIndex(KEY_INT);
        final int idxPow = cursor.getColumnIndex(KEY_POW);
        final int idxCha = cursor.getColumnIndex(KEY_CHA);
        final int idxBase = cursor.getColumnIndex(KEY_LOC_BASE);
        final int idxMove = cursor.getColumnIndex(KEY_MOVE);
        final int idxStrikeRank = cursor.getColumnIndex(KEY_STRIKE_RANK);
        creature.id = cursor.getLong(idxRowId);
        creature.str = new Roll(cursor.getString(idxStr));
        creature.con = new Roll(cursor.getString(idxCon));
        creature.siz = new Roll(cursor.getString(idxSiz));
        creature.dex = new Roll(cursor.getString(idxDex));
        creature.ins = new Roll(cursor.getString(idxInt));
        creature.pow = new Roll(cursor.getString(idxPow));
        creature.cha = new Roll(cursor.getString(idxCha));
        creature.locations = TableRaceLocations.getInstance().query(creature.id);
        creature.locations.baseExpr = cursor.getString(idxBase);
        creature.move = cursor.getShort(idxMove);
        creature.strikeRank = cursor.getShort(idxStrikeRank);
        if (creature instanceof RacePlayer) {
            fill(cursor, (RacePlayer) creature);
        }
    }

    void fill(Cursor cursor, RacePlayer player) {
        final int idxHelp = cursor.getColumnIndex(KEY_HELP);
        final int idxSilver = cursor.getColumnIndex(KEY_SILVER);
        player.help = cursor.getString(idxHelp);
        player.silver = new Roll(cursor.getString(idxSilver));
    }

    public RaceCreature query(long id) {
        String selection = KEY_ROWID + "=?";
        String[] selectionArgs = {Long.toString(id)};
        Cursor cursor = mDb.query(TABLE_NAME, null, selection, selectionArgs, null, null, null, null);
        RaceCreature creature = null;
        if (cursor.getCount() > 0) {
            int idxIsPlayer = cursor.getColumnIndex(KEY_IS_PLAYER);
            if (cursor.getShort(idxIsPlayer) != 0) {
                creature = new RacePlayer();
            } else {
                creature = new RaceCreature();
            }
            if (cursor.moveToFirst()) {
                fill(cursor, creature);
            }
        }
        cursor.close();
        return creature;
    }

    public ArrayList<RaceCreature> query() {
        ArrayList<RaceCreature> list = new ArrayList();
        Cursor cursor = mDb.query(TABLE_NAME, null, null, null, null, null, null, null);
        final int idxRowId = cursor.getColumnIndex(KEY_ROWID);
        final int idxStr = cursor.getColumnIndex(KEY_STR);
        final int idxCon = cursor.getColumnIndex(KEY_CON);
        final int idxSiz = cursor.getColumnIndex(KEY_SIZ);
        final int idxDex = cursor.getColumnIndex(KEY_DEX);
        final int idxInt = cursor.getColumnIndex(KEY_INT);
        final int idxPow = cursor.getColumnIndex(KEY_POW);
        final int idxCha = cursor.getColumnIndex(KEY_CHA);
        final int idxBase = cursor.getColumnIndex(KEY_LOC_BASE);
        int idxIsPlayer = cursor.getColumnIndex(KEY_IS_PLAYER);
        final int idxHelp = cursor.getColumnIndex(KEY_HELP);
        final int idxSilver = cursor.getColumnIndex(KEY_SILVER);
        final int idxMove = cursor.getColumnIndex(KEY_MOVE);
        final int idxStrikeRank = cursor.getColumnIndex(KEY_STRIKE_RANK);
        while (cursor.moveToNext()) {
            RaceCreature creature;
            if (cursor.getShort(idxIsPlayer) != 0) {
                RacePlayer player = new RacePlayer();
                player.help = cursor.getString(idxHelp);
                player.silver = new Roll(cursor.getString(idxSilver));
                creature = player;
            } else {
                creature = new RaceCreature();
            }
            creature.id = cursor.getLong(idxRowId);
            creature.str = new Roll(cursor.getString(idxStr));
            creature.con = new Roll(cursor.getString(idxCon));
            creature.siz = new Roll(cursor.getString(idxSiz));
            creature.dex = new Roll(cursor.getString(idxDex));
            creature.ins = new Roll(cursor.getString(idxInt));
            creature.pow = new Roll(cursor.getString(idxPow));
            creature.cha = new Roll(cursor.getString(idxCha));
            creature.locations = TableRaceLocations.getInstance().query(creature.id);
            creature.locations.baseExpr = cursor.getString(idxBase);
            creature.move = cursor.getShort(idxMove);
            creature.strikeRank = cursor.getShort(idxStrikeRank);
            list.add(creature);
        }
        cursor.close();
        return list;
    }

}
