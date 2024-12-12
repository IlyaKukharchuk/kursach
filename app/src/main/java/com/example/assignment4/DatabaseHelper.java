package com.example.assignment4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "Creating database and initializing data.");
        db.execSQL(NoteContract.NoteEntry.CREATE_TABLE);

        db.execSQL("INSERT INTO " + NoteContract.NoteEntry.TABLE_NAME + " (" +
                NoteContract.NoteEntry.COLUMN_TITLE + ", " +
                NoteContract.NoteEntry.COLUMN_DESCRIPTION + ") VALUES ('Note 1', 'Description for note 1')," +
                "('Note 2', 'Description for note 2')," +
                "('Note 3', 'Description for note 3')");
        Log.d("DatabaseHelper", "Database created with initial data.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + NoteContract.NoteEntry.TABLE_NAME);
        onCreate(db); // Заново создаем таблицу
    }
}