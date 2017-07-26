package com.dugsolutions.playerand.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import timber.log.Timber;

/**
 * Created by dug on 7/12/17.
 */

public class DatabaseManager {

    static final String DATABASE_NAME = "playerand.db";
    static final int DATABASE_VERSION = 1;

    public static void Init(Context ctx) {
        new DatabaseManager(ctx);
    }

    static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            init(db);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            init(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion > DATABASE_VERSION) {
            }
        }

        void init(SQLiteDatabase db) {
            TableRaceCreatures.Init(db);
            TableRaceLocations.Init(db);
            TableCreature.Init(db);
            TablePlayer.Init(db);
            TableSkillDesc.Init(db);
            TableSkillRef.Init(db);

            try {
                TableRaceCreatures.getInstance().create();
                TableRaceLocations.getInstance().create();
                TableCreature.getInstance().create();
                TablePlayer.getInstance().create();
                TableSkillDesc.getInstance().create();
                TableSkillRef.getInstance().create();
            } catch (Exception ex) {
                Timber.e(ex);
            }
        }
    }

    static DatabaseManager sInstance;

    final DatabaseHelper mDbHelper;
    final Context mCtx;
    SQLiteDatabase mDb;

    DatabaseManager(Context ctx) {
        mCtx = ctx;
        mDbHelper = new DatabaseHelper(ctx);
        mDb = mDbHelper.getWritableDatabase();
        sInstance = this;
    }

    public void close() {
        mDbHelper.close();
    }

}
