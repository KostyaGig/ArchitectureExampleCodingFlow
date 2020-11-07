package com.kostya_zinoviev.architectureexamplecodingflow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kostya_zinoviev.architectureexamplecodingflow.R;
import com.kostya_zinoviev.architectureexamplecodingflow.model.Note;

import java.util.List;

public class NoteRecAdapter extends RecyclerView.Adapter<NoteRecAdapter.ViewHolder> {

    private List<Note> noteList;
    private Context mContext;
    private OnItemClickListener listener;

    public NoteRecAdapter(List<Note> noteList, Context сontext) {
        this.noteList = noteList;
        this.mContext = сontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Note currentNote = noteList.get(position);

        holder.noteTitle.setText(currentNote.getTitle());
        holder.notePriority.setText(String.valueOf(currentNote.getPriority()));
        holder.noteDescription.setText(currentNote.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onClickNote(currentNote, holder.getAdapterPosition(), v);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView noteTitle,notePriority,noteDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            notePriority = itemView.findViewById(R.id.notePriority);
            noteDescription = itemView.findViewById(R.id.noteDescription);

        }

    }

    //Юзается для удаления текщуей Note (Mainactivity 78. стр. кода)
    public Note getNoteAt(int position){
        return noteList.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
