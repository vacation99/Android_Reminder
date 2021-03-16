package com.example.reminder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.adapter.RecyclerViewAdapter;
import com.example.reminder.db.DataBaseInfo;
import com.example.reminder.object.Note;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnNoteListener {

    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private ImageView imageView;
    private TextView textView;
    private ArrayList<Note> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.mainImage);
        textView = findViewById(R.id.mainText);
        initRecyclerView();
        loadNotes();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter(this, this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private Collection<Note> getNotes() {
        arrayList = new ArrayList<>();

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(DataBaseInfo.DB_NAME, MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseInfo.DB_TABLE + " (title TEXT, des TEXT, date TEXT)");
        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBaseInfo.DB_TABLE + ";", null);

        if (cursor.getCount() == 0) {
            recyclerView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }

        while (cursor.moveToNext()) {
            String title = cursor.getString(0);
            String des = cursor.getString(1);
            String date = cursor.getString(2);
            Note note = new Note(title, des, date);
            arrayList.add(note);
        }
        cursor.close();
        db.close();

        return arrayList;
    }

    private void loadNotes() {
        Collection<Note> notes = getNotes();
        recyclerViewAdapter.setItems(notes);
    }

    public void addNewInfo(View view) {
        Intent intent = new Intent(".Info");
        startActivityForResult(intent, 1);
    }

    public void clearDB(View view) {
        if (imageView.getVisibility() == View.VISIBLE)
            Toast.makeText(this, "Таблица уже пустая", Toast.LENGTH_SHORT).show();
        else {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(DataBaseInfo.DB_NAME, MODE_PRIVATE, null);
            db.execSQL("DROP TABLE " + DataBaseInfo.DB_TABLE);
            db.close();
            loadNotes();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK)
                loadNotes();
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNoteClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Выберить действие");

        builder.setPositiveButton("Изменить", (dialog, which) -> {
            Intent intent = new Intent(".Edit");
            intent.putExtra("Title", arrayList.get(position).getTitle());
            intent.putExtra("Des", arrayList.get(position).getDes());
            startActivityForResult(intent, 1);
        });

        builder.setNegativeButton("Удалить", (dialog, which) -> {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(DataBaseInfo.DB_NAME, MODE_PRIVATE, null);
            db.delete(DataBaseInfo.DB_TABLE, "title = ?", new String[] {arrayList.get(position).getTitle()});
            db.close();
            loadNotes();

            Toast.makeText(this, "Данные успешно удалены", Toast.LENGTH_SHORT).show();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}