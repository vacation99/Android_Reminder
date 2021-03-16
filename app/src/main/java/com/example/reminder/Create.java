package com.example.reminder;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.reminder.db.DataBaseInfo;

public class Create extends AppCompatActivity {

    private TextView title, des, date;
    private Switch switchDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        title = findViewById(R.id.titleCreate);
        des = findViewById(R.id.desCreate);
        date = findViewById(R.id.dateCreate);
        switchDate = findViewById(R.id.switchDate);

        switchDate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                choseDate();
                date.setVisibility(View.VISIBLE);
            } else {
                date.setText("");
                date.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void sendInfo(View view) {
        String string_1 = title.getText().toString();
        String string_2 = des.getText().toString();
        String string_3 = date.getText().toString();

        if (string_1.equals("")) {
            Toast.makeText(this, "Заполните название", Toast.LENGTH_SHORT).show();
        } else {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(DataBaseInfo.DB_NAME, MODE_PRIVATE, null);
            ContentValues contentValues = new ContentValues();

            db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseInfo.DB_TABLE + " (title TEXT, des TEXT, date TEXT)");
            contentValues.put("title", string_1);
            contentValues.put("des", string_2);
            contentValues.put("date", string_3);
            db.insert(DataBaseInfo.DB_TABLE, null, contentValues);
            db.close();

            Toast.makeText(this, "Данные успешно добавленны", Toast.LENGTH_SHORT).show();
        }
    }

    private void choseDate () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ConstraintLayout constraintLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.date, null);
        builder.setView(constraintLayout);

        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            AlertDialog alertDialog = (AlertDialog) dialog;
            DatePicker datePicker = alertDialog.findViewById(R.id.datePicker);
            StringBuffer stringBuffer = new StringBuffer();

            stringBuffer.append(datePicker.getDayOfMonth()).append(".");
            int m = datePicker.getMonth() + 1;
            stringBuffer.append(m).append(".");
            stringBuffer.append(datePicker.getYear());

            date.setText(stringBuffer.toString());
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> {});
        builder.show();
    }

    public void closeActivity(View view) {
        setResult(RESULT_OK);
        finish();
    }
}