package com.example.reminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.R;
import com.example.reminder.object.Note;

import java.util.ArrayList;
import java.util.Collection;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.SimpleViewHolder> {

    private ArrayList<Note> arrayList = new ArrayList<>();
    private Context context;
    private OnNoteListener monNoteListener;

    public RecyclerViewAdapter(Context context, OnNoteListener onNoteListener) {
        this.context = context;
        this.monNoteListener = onNoteListener;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items, parent, false);
        return new SimpleViewHolder(view, monNoteListener);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.bind(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setItems(Collection<Note> notes) {
        arrayList.clear();
        arrayList.addAll(notes);
        notifyDataSetChanged();
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {
        private TextView title, des, date;
        private ImageView imageView;
        OnNoteListener onNoteListener;

        public SimpleViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            des = itemView.findViewById(R.id.textViewDes);
            date = itemView.findViewById(R.id.textViewDate);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                onNoteListener.onNoteClick(position);
            });
        }

        public void bind(Note note) {
            title.setText(note.getTitle());
            des.setText(note.getDes());
            date.setText(note.getDate());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
