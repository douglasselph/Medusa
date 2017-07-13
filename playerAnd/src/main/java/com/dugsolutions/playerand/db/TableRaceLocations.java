package com.dugsolutions.playerand.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dugsolutions.playerand.data.RaceLocations;
import com.dugsolutions.playerand.data.RaceLocations.RaceLocation;

import timber.log.Timber;

/**
 * Created by dug on 7/12/17.
 */

public class TableRaceLocations {

    static final String TABLE_NAME = "race_locations";

    static final String KEY_ROWID = "_id";
    static final String KEY_RACE_ID = "race_id";
    static final String KEY_NAME = "name";
    static final String KEY_HP_EXPR = "hp_expr";
    static final String KEY_ROLL = "roll";

    static TableRaceLocations sInstance;

    static void Init(SQLiteDatabase db) {
        new TableRaceCreatures(db);
    }

    public static TableRaceLocations getInstance() {
        return sInstance;
    }

    final SQLiteDatabase mDb;

    TableRaceLocations(SQLiteDatabase db) {
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
        sbuf.append(KEY_RACE_ID);
        sbuf.append(" integer, ");
        sbuf.append(KEY_NAME);
        sbuf.append(" nchar(256), ");
        sbuf.append(KEY_HP_EXPR);
        sbuf.append(" nchar(64), ");
        sbuf.append(KEY_ROLL);
        sbuf.append(" smallint)");
        mDb.execSQL(sbuf.toString());
    }

    public void store(long race_id, RaceLocations locs) {
        for (RaceLocation loc : locs.getLocations()) {
            store(race_id, loc);
        }
    }

    void store(long race_id, RaceLocation loc) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, loc.name);
        values.put(KEY_RACE_ID, race_id);
        values.put(KEY_HP_EXPR, loc.hpExpr);
        values.put(KEY_ROLL, loc.roll);
        if (loc.id > 0) {
            String where = KEY_ROWID + "=?";
            String[] whereArgs = {Long.toString(loc.id)};
            mDb.update(TABLE_NAME, values, where, whereArgs);
        } else {
            loc.id = mDb.insert(TABLE_NAME, null, values);
        }
    }

    public RaceLocations query(long race_id) {
        RaceLocations locations = new RaceLocations();
        String selection = KEY_RACE_ID + "=?";
        String[] selectionArgs = {Long.toString(race_id)};
        Cursor cursor = mDb.query(TABLE_NAME, null, selection, selectionArgs, null, null, null, null);
        while (cursor.moveToNext()) {
            final int idxRowId = cursor.getColumnIndex(KEY_ROWID);
            final int idxName = cursor.getColumnIndex(KEY_NAME);
            final int idxHpExpr = cursor.getColumnIndex(KEY_HP_EXPR);
            final int idxRoll = cursor.getColumnIndex(KEY_ROLL);
            RaceLocation loc = new RaceLocation();
            loc.id = cursor.getLong(idxRowId);
            loc.name = cursor.getString(idxName);
            loc.hpExpr = cursor.getString(idxHpExpr);
            loc.roll = cursor.getShort(idxRoll);
            locations.add(loc);
        }
        cursor.close();
        locations.sort();
        return locations;
    }

}
