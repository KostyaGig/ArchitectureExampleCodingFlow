package com.kostya_zinoviev.architectureexamplecodingflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.Objects;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.kostya_zinoviev.architectureexamplecodingflow.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.kostya_zinoviev.architectureexamplecodingflow.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.kostya_zinoviev.architectureexamplecodingflow.EXTRA_PRIORITY";
    public static final String EXTRA_ID = "com.kostya_zinoviev.architectureexamplecodingflow.EXTRA_ID";

    private EditText edTitle, edDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        edTitle = findViewById(R.id.ed_title);
        edDescription = findViewById(R.id.ed_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        //Если это edit note intent устанавливаем нашему тулбар такой титл
        Intent getIntent = getIntent();
        if (getIntent != null){
            setTitle("Edit note");

            //Приянли данные
            String title = getIntent.getStringExtra(EXTRA_TITLE);
            String description = getIntent.getStringExtra(EXTRA_DESCRIPTION);
            int priority = getIntent.getIntExtra(EXTRA_PRIORITY,1);

            //Устанавливаем данные
            edTitle.setText(title);
            edDescription.setText(description);
            numberPickerPriority.setValue(priority);
        }

        //Если это add note intent устанавливаем нашему тулбар такой титл
        if (getIntent == null) {
            setTitle("Add note");
        }

        //Этот код срабатыает в любом случаем,тоесть появится крестик (закрыть активити),
        // нажатие которого мы обрабатываем в onOptionsItemSelected
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.save_note:
                saveNote();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


    private void saveNote() {
        String title = edTitle.getText().toString().trim();
        String description = edDescription.getText().toString().trim();
        int priority = numberPickerPriority.getValue();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)){
            Toast.makeText(this, "Зпоните пустые поля!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE,title);
        data.putExtra(EXTRA_DESCRIPTION,description);
        data.putExtra(EXTRA_PRIORITY,priority);

        int id = getIntent().getIntExtra(EXTRA_ID,-1);
        if (id != -1){
            data.putExtra(EXTRA_ID,id);
        }
        setResult(RESULT_OK,data);
        finish();

    }

}
