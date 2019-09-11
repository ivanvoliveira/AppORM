package com.example.apporm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apporm.R;
import com.example.apporm.db.AppDatabase;
import com.example.apporm.db.NoteDAO;
import com.example.apporm.model.Note;

public class NoteActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextContent;

    private NoteDAO dao;

    private Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        getSupportActionBar().setTitle("Nova Nota");

        editTextTitle = findViewById(R.id.editText_title);
        editTextContent = findViewById(R.id.editText_content);

        dao = AppDatabase.getDatabase(getApplicationContext()).noteDAO();

        selectedNote = (Note) getIntent().getSerializableExtra("SelectedNote");

        if (selectedNote != null) {
            getSupportActionBar().setTitle("Editar Nota");
            editTextTitle.setText(selectedNote.getTitle());
            editTextContent.setText(selectedNote.getContent());
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_logout).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                saveNote();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
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
}
