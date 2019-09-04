package com.example.apporm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apporm.R;
import com.example.apporm.db.AppDatabase;
import com.example.apporm.db.NoteDAO;
import com.example.apporm.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class NoteActivity extends AppCompatActivity {

    private FloatingActionButton fabSave;
    private EditText editTextTitle, editTextContent;

    private NoteDAO dao;

    private Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        getSupportActionBar().setTitle("Nova Nota");

        fabSave = findViewById(R.id.fab_save);
        editTextTitle = findViewById(R.id.editText_title);
        editTextContent = findViewById(R.id.editText_content);

        dao = AppDatabase.getDatabase(getApplicationContext()).noteDAO();

        selectedNote = (Note) getIntent().getSerializableExtra("SelectedNote");

        if (selectedNote != null) {
            getSupportActionBar().setTitle("Editar Nota");
            editTextTitle.setText(selectedNote.getTitle());
            editTextContent.setText(selectedNote.getContent());
        }

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedNote != null) {
                    String title = editTextTitle.getText().toString();
                    String content = editTextContent.getText().toString();

                    if (!title.isEmpty()) {
                        Note note = new Note(title, content);
                        note.setId(selectedNote.getId());

                        dao.update(note);
                        Toast.makeText(NoteActivity.this, "Nota atualizada!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else {
                    String title = editTextTitle.getText().toString();
                    String content = editTextContent.getText().toString();

                    if (!title.isEmpty()) {
                        if (!content.isEmpty()) {
                            Note note = new Note(title, content);

                            dao.insert(note);

                            Toast.makeText(NoteActivity.this, "Nota Salva!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(NoteActivity.this, "Conteúdo vazio", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(NoteActivity.this, "Título vazio", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
