package com.example.assignment4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteDataSource dataSource;
    private RecyclerView recyclerViewNotes;
    private NoteAdapter adapter;
    private EditText etSearch;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new NoteDataSource(this);
        dataSource.open();

        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        Button btnAddNote = findViewById(R.id.btnAddNote);
        Button btnDeleteAll = findViewById(R.id.btnDeleteAll);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNoteDialog();
            }
        });

        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteAllConfirmationDialog();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = etSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    List<Note> notes = dataSource.searchNotesByTitle(query);
                    adapter.updateData(notes);
                } else {
                    updateRecyclerView();
                }
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        List<Note> notes = dataSource.getAllNotes();
        adapter = new NoteAdapter(notes, new NoteAdapter.OnItemClickListener() {
            @Override
            public void onUpdateClick(Note note) {
                showUpdateNoteDialog(note);
            }

            @Override
            public void onDeleteClick(Note note) {
                showDeleteConfirmationDialog(note);
            }
        });
        recyclerViewNotes.setAdapter(adapter);
    }

    private void showAddNoteDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_note, null);
        final EditText etTitle = dialogView.findViewById(R.id.etTitle);
        final EditText etDescription = dialogView.findViewById(R.id.etDescription);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Add New Note")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = etTitle.getText().toString().trim();
                        String description = etDescription.getText().toString().trim();
                        if (!title.isEmpty() && !description.isEmpty()) {
                            dataSource.insertNote(title, description);
                            updateRecyclerView();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void showUpdateNoteDialog(final Note note) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_note, null);
        final EditText etTitle = dialogView.findViewById(R.id.etTitle);
        final EditText etDescription = dialogView.findViewById(R.id.etDescription);

        etTitle.setText(note.getTitle());
        etDescription.setText(note.getDescription());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Update Note")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = etTitle.getText().toString().trim();
                        String description = etDescription.getText().toString().trim();
                        if (!title.isEmpty() && !description.isEmpty()) {
                            note.setTitle(title);
                            note.setDescription(description);
                            dataSource.updateNote(note.getId(), title, description);
                            updateRecyclerView();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void showDeleteConfirmationDialog(final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataSource.deleteNote(note.getId());
                        updateRecyclerView();
                    }
                })
                .setNegativeButton("No", null)
                .create()
                .show();
    }

    private void showDeleteAllConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All Notes")
                .setMessage("Are you sure you want to delete all notes?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataSource.deleteAllNotes();
                        updateRecyclerView();
                    }
                })
                .setNegativeButton("No", null)
                .create()
                .show();
    }

    private void updateRecyclerView() {
        Log.d("MainActivity", "Updating RecyclerView with new data.");
        List<Note> notes = dataSource.getAllNotes();
        adapter.updateData(notes);
    }

    @Override
    protected void onDestroy() {
        Log.d("MainActivity", "Destroying activity and closing data source.");
        super.onDestroy();
        dataSource.close();
    }
}