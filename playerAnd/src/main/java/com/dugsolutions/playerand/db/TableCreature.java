package com.dugsolutions.playerand.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dugsolutions.playerand.data.Creature;
import com.dugsolutions.playerand.data.RaceCreature;
import com.dugsolutions.playerand.util.Roll;

import timber.log.Timber;

/**
 * Created by dug on 7/13/17.
 */

public class TableCreature {

    static final String TABLE_NAME = "creatures";

    static final String KEY_ROWID   = "_id";
    static final String KEY_NAME    = "name";
    static final String KEY_STR     = "str";
    static final String KEY_CON     = "con";
    static final String KEY_SIZ     = "siz";
    static final String KEY_DEX     = "dex";
    static final String KEY_INT     = "ins";
    static final String KEY_POW     = "pow";
    static final String KEY_CHA     = "cha";
    static final String KEY_RACE_ID = "race_id";

    static TableCreature sInstance;

    static void Init(SQLiteDatabase db) {
        new TableCreature(db);
    }

    public static TableCreature getInstance() {
        return sInstance;
    }

    final SQLiteDatabase mDb;

    TableCreature(SQLiteDatabase db) {
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
        sbuf.append(" tinyint, ");
        sbuf.append(KEY_CON);
        sbuf.append(" tinyint, ");
        sbuf.append(KEY_SIZ);
        sbuf.append(" tinyint, ");
        sbuf.append(KEY_DEX);
        sbuf.append(" tinyint, ");
        sbuf.append(KEY_INT);
        sbuf.append(" tinyint, ");
        sbuf.append(KEY_POW);
        sbuf.append(" tinyint, ");
        sbuf.append(KEY_CHA);
        sbuf.append(" tinyint, ");
        sbuf.append(KEY_RACE_ID);
        sbuf.append(" int)");
        mDb.execSQL(sbuf.toString());
    }

    public void store(Creature creature) {
        mDb.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, creature.name);
            values.put(KEY_STR, creature.str);
            values.put(KEY_CON, creature.con);
            values.put(KEY_SIZ, creature.siz);
            values.put(KEY_DEX, creature.dex);
            values.put(KEY_INT, creature.ins);
            values.put(KEY_POW, creature.pow);
            values.put(KEY_CHA, creature.cha);
            values.put(KEY_RACE_ID, creature.race.id);
            if (creature.id > 0) {
                String where = KEY_ROWID + "=?";
                String[] whereArgs = {Long.toString(creature.id)};
                mDb.update(TABLE_NAME, values, where, whereArgs);
            } else {
                creature.id = mDb.insert(TABLE_NAME, null, values);
            }
            mDb.setTransactionSuccessful();
        } catch (Exception ex) {
            Timber.e(ex);
        } finally {
            mDb.endTransaction();
        }
    }

    public Creature query(String name) {
        String selection = KEY_NAME + "=?";
        String[] selectionArgs = {name};
        Cursor cursor = mDb.query(TABLE_NAME, null, selection, selectionArgs, null, null, null, null);
        Creature creature = null;
        if (cursor.getCount() > 0) {
            if (cursor.getCount() > 1) {
                Timber.e("Found too many creatures named: " + name);
            }
            if (cursor.moveToFirst()) {

            }
        }
        cursor.close();
        return creature;
    }

    void fill(Cursor cursor, Creature creature) {
        final int idxRowId = cursor.getColumnIndex(KEY_ROWID);
        final int idxStr = cursor.getColumnIndex(KEY_STR);
        final int idxCon = cursor.getColumnIndex(KEY_CON);
        final int idxSiz = cursor.getColumnIndex(KEY_SIZ);
        final int idxDex = cursor.getColumnIndex(KEY_DEX);
        final int idxInt = cursor.getColumnIndex(KEY_INT);
        final int idxPow = cursor.getColumnIndex(KEY_POW);
        final int idxCha = cursor.getColumnIndex(KEY_CHA);
        final int idxRace = cursor.getColumnIndex(KEY_RACE_ID);
        long raceId = cursor.getLong(idxRace);
        RaceCreature race = TableRaceCreatures.getInstance().query(raceId);
        creature = new Creature(race);
        creature.id = cursor.getLong(idxRowId);
        creature.str = cursor.getShort(idxStr);
        creature.con = cursor.getShort(idxCon);
        creature.siz = cursor.getShort(idxSiz);
        creature.dex = cursor.getShort(idxDex);
        creature.ins = cursor.getShort(idxInt);
        creature.pow = cursor.getShort(idxPow);
        creature.cha = cursor.getShort(idxCha);
    }

}
