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

public class TableRacePlayers {

    static final String TABLE_NAME = "race_players";

    static final String KEY_ROWID = "_id";
    static final String KEY_CREATURE_ID = "creature_race_id";
    static final String KEY_HELP = "help";
    static final String KEY_SILVER = "silver";

    static TableRacePlayers sInstance;

    static void Init(SQLiteDatabase db) {
        new TableRacePlayers(db);
    }

    public static TableRacePlayers getInstance() {
        return sInstance;
    }

    final SQLiteDatabase mDb;

    TableRacePlayers(SQLiteDatabase db) {
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
        sbuf.append(KEY_CREATURE_ID);
        sbuf.append(" int, ");
        sbuf.append(KEY_HELP);
        sbuf.append(" text, ");
        sbuf.append(KEY_SILVER);
        sbuf.append(" nchar(64))");
        mDb.execSQL(sbuf.toString());
    }

    public void store(RacePlayer player) {
        mDb.beginTransaction();
        try {
            TableRaceCreatures.getInstance().store(player.creature);

            ContentValues values = new ContentValues();
            values.put(KEY_HELP, player.help);
            values.put(KEY_CREATURE_ID, player.creature.id);
            values.put(KEY_SILVER, player.silver.toString());
            if (player.id > 0) {
                String where = KEY_ROWID + "=?";
                String[] whereArgs = {Long.toString(player.id)};
                mDb.update(TABLE_NAME, values, where, whereArgs);
            } else {
                player.id = mDb.insert(TABLE_NAME, null, values);
            }
            mDb.setTransactionSuccessful();
        } catch (Exception ex) {
            Timber.e(ex);
        } finally {
            mDb.endTransaction();
        }
    }

    public RacePlayer query(String name) {
        return query(new RacePlayer(), name);
    }

    public RacePlayer query(RacePlayer player, String name) {
        player.creature = TableRaceCreatures.getInstance().query(player.creature, name);
        String selection = KEY_CREATURE_ID + "=?";
        String[] selectionArgs = {name};
        Cursor cursor = mDb.query(TABLE_NAME, null, selection, selectionArgs, null, null, null, null);
        if (cursor.getCount() >= 1) {
            if (cursor.moveToFirst()) {
                final int idxRowId = cursor.getColumnIndex(KEY_ROWID);
                final int idxHelp = cursor.getColumnIndex(KEY_HELP);
                final int idxSilver = cursor.getColumnIndex(KEY_SILVER);
                player.id = cursor.getLong(idxRowId);
                player.help = cursor.getString(idxHelp);
                player.silver = new Roll(cursor.getString(idxSilver));
            }
            if (cursor.getCount() > 1) {
                Timber.e("Too many player races named: " + name);
            }
        }
        cursor.close();
        return player;
    }

    public ArrayList<RacePlayer> query() {
        Cursor cursor = mDb.query(TABLE_NAME, null, null, null, null, null, null, null);
        ArrayList<RacePlayer> list = new ArrayList();
        final int idxRowId = cursor.getColumnIndex(KEY_ROWID);
        final int idxHelp = cursor.getColumnIndex(KEY_HELP);
        final int idxSilver = cursor.getColumnIndex(KEY_SILVER);
        final int idxRaceName = cursor.getColumnIndex(KEY_NAME);

        while (cursor.moveToNext()) {

        }
        return list;
    }
}
