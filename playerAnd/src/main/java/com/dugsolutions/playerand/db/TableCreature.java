package com.dugsolutions.playerand.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dugsolutions.playerand.data.RaceCreature;

import timber.log.Timber;

/**
 * Created by dug on 7/12/17.
 */

public class TableCreature {

    static final String HUMANOID = "humanoid";

    static final String TABLE_NAME = "creatures";

    static final String KEY_ROWID = "_id";
    static final String KEY_NAME = "name";

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
        sbuf.append(" text)");
        mDb.execSQL(sbuf.toString());
    }

    public void store(RaceCreature creature) {
        mDb.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, creature.name);
            if (creature.getId() > 0) {
                String where = KEY_ROWID + "=?";
                String[] whereArgs = {Long.toString(creature.getId())};
                mDb.update(TABLE_NAME, values, where, whereArgs);
            } else {
                creature.setId(mDb.insert(TABLE_NAME, null, values));
            }
            mDb.setTransactionSuccessful();
        } catch (Exception ex) {
            Timber.e(ex);
        } finally {
            mDb.endTransaction();
        }
    }

    public RaceCreature query(String name) {
        RaceCreature animal = new RaceCreature();
        String[] columns = {KEY_ROWID};
        String selection = KEY_NAME + "=?";
        String[] selectionArgs = {name};
        Cursor cursor = mDb.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null, null);
        final int idxRowId = cursor.getColumnIndex(KEY_ROWID);
        if (cursor.moveToFirst()) {
            animal.setId(cursor.getLong(idxRowId));
        } else {
            Timber.e("Could not find creature named '" + name + "'");
            return null;
        }
        return animal;
    }
}
