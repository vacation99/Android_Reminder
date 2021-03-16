package com.example.reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reminder.db.DataBaseInfo;

public class Edit extends AppCompatActivity {

    private TextView textView_1, textView_2;
    private String oldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        textView_1 = findViewById(R.id.titleEdit);
        textView_2 = findViewById(R.id.desEdit);

        Bundle bundle = getIntent().getExtras();

        textView_1.setText(bundle.getString("Title"));
        textView_2.setText(bundle.getString("Des"));

        oldText = textView_1.getText().toString();
    }

    public void updateNoteFromDB(View view) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(DataBaseInfo.DB_NAME, MODE_PRIVATE, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", textView_1.getText().toString());
        contentValues.put("des", textView_2.getText().toString());
        db.update(DataBaseInfo.DB_TABLE, contentValues, "title = ?", new String[] {oldText});
        db.close();

        Toast.makeText(this, "Данные успешно изменились", Toast.LENGTH_SHORT).show();
    }

    public void closeActivity(View view) {
        setResult(RESULT_OK);
        finish();
    }
}