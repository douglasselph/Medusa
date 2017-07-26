package com.dugsolutions.playerand.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dugsolutions.playerand.data.Creature;
import com.dugsolutions.playerand.data.RaceCreature;
import com.dugsolutions.playerand.data.SkillDesc;
import com.dugsolutions.playerand.data.SkillRef;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by dug on 7/14/17.
 */

public class TableSkillRef {

    static final String TABLE_NAME = "skills_ref";

    static final String KEY_ROWID         = "_id";
    static final String KEY_SKILL_DESC_ID = "skill_desc_id";
    static final String KEY_CREATURE_ID   = "creature_id";
    static final String KEY_VALUE         = "value";
    static final String KEY_IS_RACE       = "is_race";

    static TableSkillRef sInstance;

    static void Init(SQLiteDatabase db) {
        sInstance = new TableSkillRef(db);
    }

    public static TableSkillRef getInstance() {
        return sInstance;
    }

    final SQLiteDatabase mDb;

    TableSkillRef(SQLiteDatabase db) {
        mDb = db;
    }

    public void create() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append("create table if not exists ");
        sbuf.append(TABLE_NAME);
        sbuf.append(" (");
        sbuf.append(KEY_ROWID);
        sbuf.append(" integer primary key autoincrement, ");
        sbuf.append(KEY_SKILL_DESC_ID);
        sbuf.append(" int, ");
        sbuf.append(KEY_CREATURE_ID);
        sbuf.append(" int, ");
        sbuf.append(KEY_VALUE);
        sbuf.append(" tinyint, ");
        sbuf.append(KEY_IS_RACE);
        sbuf.append(" bit)");
        mDb.execSQL(sbuf.toString());
    }

    public void store(Creature creature) {
        if (creature.skills.size() > 0) {
            store(creature.skills, creature.id, false);
        }
    }

    public void store(RaceCreature creature) {
        if (creature.skills.size() > 0) {
            store(creature.skills, creature.id, true);
        }
    }

    public void store(ArrayList<SkillRef> skills, long id, boolean is_race) {
        mDb.beginTransaction();
        try {
            for (SkillRef skill : skills) {
                ContentValues values = new ContentValues();
                fill(values, skill, id, is_race);
                if (skill.id > 0) {
                    String   where     = KEY_ROWID + "=?";
                    String[] whereArgs = {Long.toString(skill.id)};
                    mDb.update(TABLE_NAME, values, where, whereArgs);
                } else {
                    skill.id = mDb.insert(TABLE_NAME, null, values);
                }
            }
            mDb.setTransactionSuccessful();
        } catch (Exception ex) {
            Timber.e(ex);
        } finally {
            mDb.endTransaction();
        }
    }

    void fill(ContentValues values, SkillRef skill, long id, boolean is_race) {
        values.put(KEY_SKILL_DESC_ID, skill.skill_desc_id);
        values.put(KEY_VALUE, skill.value);
        values.put(KEY_CREATURE_ID, id);
        values.put(KEY_IS_RACE, is_race ? 1 : 0);
    }

    public void query(Creature creature) {
        creature.skills = query(creature.id, false);
    }

    public void query(RaceCreature creature) {
        creature.skills = query(creature.id, true);
    }

    public ArrayList<SkillRef> query(long id, boolean is_race) {
        String              selection      = KEY_CREATURE_ID + "=? AND " + KEY_IS_RACE + "=?";
        String[]            selectionArgs  = {Long.toString(id), is_race ? "1" : "0"};
        Cursor              cursor         = mDb.query(TABLE_NAME, null, selection, selectionArgs, null, null, null, null);
        ArrayList<SkillRef> skills         = new ArrayList();
        final int           idxRowId       = cursor.getColumnIndex(KEY_ROWID);
        final int           idxSkillDescId = cursor.getColumnIndex(KEY_SKILL_DESC_ID);
        final int           idxValue       = cursor.getColumnIndex(KEY_VALUE);
        while (cursor.moveToNext()) {
            SkillRef skill = new SkillRef();
            skill.id = cursor.getLong(idxRowId);
            skill.skill_desc_id = cursor.getLong(idxSkillDescId);
            skill.value = cursor.getShort(idxValue);
            skills.add(skill);
        }
        cursor.close();
        return skills;
    }

}
