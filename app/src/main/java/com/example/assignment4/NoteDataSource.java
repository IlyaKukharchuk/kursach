package com.example.assignment4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NoteDataSource {
    private SQLiteDatabase database;
    private SQLiteOpenHelper dbHelper;

    public NoteDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
    }

    public void open() {
        Log.d("NoteDataSource", "Opening database connection.");
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        Log.d("NoteDataSource", "Closing database connection.");
        dbHelper.close();
    }

    public void insertNote(String title, String description) {
        Log.d("NoteDataSource", "Inserting note: Title='" + title + "', Description='" + description + "'");
        ContentValues values = new ContentValues();
        values.put(NoteContract.NoteEntry.COLUMN_TITLE, title);
        values.put(NoteContract.NoteEntry.COLUMN_DESCRIPTION, description);
        database.insert(NoteContract.NoteEntry.TABLE_NAME, null, values);
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        Cursor cursor = database.query(
                NoteContract.NoteEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(NoteContract.NoteEntry._ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_DESCRIPTION)));
                notes.add(note);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return notes;
    }

    public void updateNote(int id, String title, String description) {
        ContentValues values = new ContentValues();
        values.put(NoteContract.NoteEntry.COLUMN_TITLE, title);
        values.put(NoteContract.NoteEntry.COLUMN_DESCRIPTION, description);
        String selection = NoteContract.NoteEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};
        database.update(NoteContract.NoteEntry.TABLE_NAME, values, selection, selectionArgs);
    }
    public void deleteNote(int id) {
        Log.d("NoteDataSource", "Deleting note with ID: " + id);
        String selection = NoteContract.NoteEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};
        database.delete(NoteContract.NoteEntry.TABLE_NAME, selection, selectionArgs);
    }

    public void deleteAllNotes() {
        Log.d("NoteDataSource", "Deleting all notes.");
        database.delete(NoteContract.NoteEntry.TABLE_NAME, null, null);
    }
}