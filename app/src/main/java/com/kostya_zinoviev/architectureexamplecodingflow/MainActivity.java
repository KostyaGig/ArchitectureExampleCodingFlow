package com.kostya_zinoviev.architectureexamplecodingflow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kostya_zinoviev.architectureexamplecodingflow.adapter.NoteRecAdapter;
import com.kostya_zinoviev.architectureexamplecodingflow.adapter.OnItemClickListener;
import com.kostya_zinoviev.architectureexamplecodingflow.db.NoteViewModel;
import com.kostya_zinoviev.architectureexamplecodingflow.model.Note;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_NOTE = 1;
    public static final int REQUEST_EDIT_NOTE = 2;

    private NoteViewModel noteViewModel;
    private Observer<List<Note>> observer;
    private RecyclerView noteRecyclerView;
    private NoteRecAdapter adapter;
    private FloatingActionButton addFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteRecyclerView = findViewById(R.id.noteRecyclerView);
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        addFab = findViewById(R.id.button_add_note);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNoteIntent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(addNoteIntent,REQUEST_ADD_NOTE);
            }
        });

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        observer = new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //updateRecyclerView

                adapter = new NoteRecAdapter(notes,MainActivity.this);
                noteRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                //install listener for adapter recyclerview
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClickNote(Note clickedNote, int position, View view) {
                        Intent editNoteIntent = new Intent(MainActivity.this, AddEditNoteActivity.class);

                        int id = clickedNote.getId();
                        String title = clickedNote.getTitle();
                        String description = clickedNote.getDescription();
                        int priority = clickedNote.getPriority();

                        editNoteIntent.putExtra(AddEditNoteActivity.EXTRA_ID,id);
                        editNoteIntent.putExtra(AddEditNoteActivity.EXTRA_TITLE,title);
                        editNoteIntent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION,description);
                        editNoteIntent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY,priority);

                        MainActivity.this.startActivityForResult(editNoteIntent,REQUEST_EDIT_NOTE);
                    }
                });
            }
        };

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(noteRecyclerView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        noteViewModel.getAllNotes().observe(this,observer);
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteViewModel.getAllNotes().removeObserver(observer);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_NOTE && resultCode == RESULT_OK && data != null){
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);

            Note note = new Note(title,description,priority);
            noteViewModel.insert(note);
        } else if (requestCode == REQUEST_EDIT_NOTE && resultCode == RESULT_OK && data != null){
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID,-1);

            if (id == -1){
                Toast.makeText(this, "Not cant't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);

            Note note = new Note(title,description,priority);
            note.setId(id);

            noteViewModel.update(note);
            Toast.makeText(this, "Note was updated!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Note note saved!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.deleteAllNotes:
                noteViewModel.deleteAllNotes();
                break;

        }
        return true;
    }

}
