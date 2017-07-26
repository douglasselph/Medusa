package com.dugsolutions.playerand.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dugsolutions.playerand.data.SkillDesc;
import com.dugsolutions.playerand.util.StringUtils;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by dug on 7/14/17.
 */

public class TableSkillDesc {

    static final String TABLE_NAME = "skills_desc";

    static final String KEY_ROWID           = "_id";
    static final String KEY_NAME            = "name";
    static final String KEY_DESC            = "desc";
    static final String KEY_BASE            = "base";
    static final String KEY_IS_PROFESSIONAL = "is_professional";
    static final String KEY_PARENT_ID       = "parent_id";

    static TableSkillDesc sInstance;

    static void Init(SQLiteDatabase db) {
        sInstance = new TableSkillDesc(db);
    }

    public static TableSkillDesc getInstance() {
        return sInstance;
    }

    final SQLiteDatabase mDb;

    TableSkillDesc(SQLiteDatabase db) {
        mDb = db;
    }

    public void create() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append("create table  if not exists ");
        sbuf.append(TABLE_NAME);
        sbuf.append(" (");
        sbuf.append(KEY_ROWID);
        sbuf.append(" integer primary key autoincrement, ");
        sbuf.append(KEY_PARENT_ID);
        sbuf.append(" int, ");
        sbuf.append(KEY_NAME);
        sbuf.append(" nchar(256), ");
        sbuf.append(KEY_DESC);
        sbuf.append(" text, ");
        sbuf.append(KEY_BASE);
        sbuf.append(" nchar(64), ");
        sbuf.append(KEY_IS_PROFESSIONAL);
        sbuf.append(" bit)");
        mDb.execSQL(sbuf.toString());
    }

    public void store(SkillDesc skill) {
        mDb.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            fill(values, skill);
            if (skill.id > 0) {
                String where = KEY_ROWID + "=?";
                String[] whereArgs = {Long.toString(skill.id)};
                mDb.update(TABLE_NAME, values, where, whereArgs);
            } else {
                skill.id = mDb.insert(TABLE_NAME, null, values);
            }
            mDb.setTransactionSuccessful();
        } catch (Exception ex) {
            Timber.e(ex);
        } finally {
            mDb.endTransaction();
        }
    }

    void fill(ContentValues values, SkillDesc skill) {
        values.put(KEY_NAME, StringUtils.capitalize(skill.name));
        values.put(KEY_DESC, skill.desc);
        values.put(KEY_BASE, skill.base);
        values.put(KEY_IS_PROFESSIONAL, skill.isProfessional ? 1 : 0);
        if (skill.parent != null) {
            values.put(KEY_PARENT_ID, skill.parent.id);
        }
    }

    public SkillDesc query(String name) {
        return query(null, name);
    }

    public SkillDesc query(SkillDesc skill, String name) {
        if (skill == null) {
            skill = new SkillDesc();
            skill.name = name;
        }
        String selection = KEY_NAME + "=?";
        String[] selectionArgs = {StringUtils.capitalize(name)};
        Cursor cursor = mDb.query(TABLE_NAME, null, selection, selectionArgs, null, null, null, null);
        if (cursor.getCount() >= 1) {
            if (cursor.moveToFirst()) {
                fill(cursor, skill);
            }
            if (cursor.getCount() > 1) {
                Timber.e("Found too many creatures with the name: " + name);
            }
        }
        cursor.close();
        return skill;
    }

    public SkillDesc query(long id) {
        SkillDesc skill         = new SkillDesc();
        String    selection     = KEY_ROWID + "=?";
        String[]  selectionArgs = {Long.toString(id)};
        Cursor    cursor        = mDb.query(TABLE_NAME, null, selection, selectionArgs, null, null, null, null);
        if (cursor.getCount() >= 1) {
            if (cursor.moveToFirst()) {
                skill.id = id;
                fill(cursor, skill);
            }
        }
        cursor.close();
        return skill;
    }

    public ArrayList<SkillDesc> query() {
        ArrayList<SkillDesc> result = new ArrayList();
        Cursor               cursor = mDb.query(TABLE_NAME, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            SkillDesc skill = new SkillDesc();
            fill(cursor, skill);
            result.add(skill);
        }
        return result;
    }

    void fill(Cursor cursor, SkillDesc skill) {
        final int idxRowId = cursor.getColumnIndex(KEY_ROWID);
        final int idxName = cursor.getColumnIndex(KEY_NAME);
        final int idxDesc = cursor.getColumnIndex(KEY_DESC);
        final int idxBase = cursor.getColumnIndex(KEY_BASE);
        final int idxIsProf = cursor.getColumnIndex(KEY_IS_PROFESSIONAL);
        final int idxParentId = cursor.getColumnIndex(KEY_PARENT_ID);

        skill.id = cursor.getLong(idxRowId);
        skill.name = cursor.getString(idxName);
        skill.desc = cursor.getString(idxDesc);
        skill.base = cursor.getString(idxBase);
        skill.isProfessional = cursor.getShort(idxIsProf) != 0;

        int parentId = cursor.getInt(idxParentId);
        if (parentId > 0) {
            skill.parent = query(parentId);
        }
    }

}
