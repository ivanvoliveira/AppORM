package com.example.apporm.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apporm.R;
import com.example.apporm.activity.NoteActivity;
import com.example.apporm.db.AppDatabase;
import com.example.apporm.db.NoteDAO;
import com.example.apporm.model.Note;

import java.util.List;

public class AdapterNotes extends RecyclerView.Adapter<AdapterNotes.ViewHolderNotes> {

    private List<Note> notes;
    private Context context;

    public AdapterNotes(List<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public ViewHolderNotes onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_note, viewGroup, false);

        return new ViewHolderNotes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderNotes viewHolder, int position) {
        Note note = notes.get(position);

        viewHolder.title.setText(note.getTitle());
        viewHolder.content.setText(note.getContent());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = v.getContext();
                Note selectedNote = notes.get(viewHolder.getAdapterPosition());

                Intent intent = new Intent(v.getContext(), NoteActivity.class);
                intent.putExtra("SelectedNote", selectedNote);

                v.getContext().startActivity(intent);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                context = v.getContext();
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                Note selectedNote = notes.get(viewHolder.getAdapterPosition());

                dialog.setTitle("Confirmar exclusão");
                dialog.setMessage("Deseja excluir a nota: " + selectedNote.getTitle()+ "?");

                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Note selectedNote = notes.get(viewHolder.getAdapterPosition());

                        NoteDAO dao = AppDatabase.getDatabase(context).noteDAO();

                        dao.delete(selectedNote);
                        notes.remove(viewHolder.getAdapterPosition());
                        notifyItemRemoved(viewHolder.getAdapterPosition());
                        Toast.makeText(context, "Nota excluída!", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.setNegativeButton("Não", null);

                dialog.create().show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolderNotes extends RecyclerView.ViewHolder {

        private TextView content, title;

        public ViewHolderNotes(@NonNull View view) {
            super(view);

            content = view.findViewById(R.id.textView_content);
            title = view.findViewById(R.id.textView_title);
        }
    }
}
