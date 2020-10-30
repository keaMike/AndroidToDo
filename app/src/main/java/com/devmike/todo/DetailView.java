package com.devmike.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.devmike.todo.global.Global;
import com.devmike.todo.model.ToDo;
import com.devmike.todo.repo.Repo;

public class DetailView extends AppCompatActivity {

    private Intent intent;
    private EditText title;
    private EditText description;
    private ToDo toDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        intent = getIntent();
        toDo = (ToDo) Global.map.get(Global.TODO_KEY);

        title = findViewById(R.id.detailTitle);
        description = findViewById(R.id.description);

        title.setText(toDo.getTitle());
        description.setText(toDo.getDescription());
    }

    public void saveDescription(View view) {
        String newTitle = title.getText().toString();
        String newDescription = description.getText().toString();
        if (!newDescription.equals("") && !newTitle.equals("")) {
            toDo.setTitle(newTitle);
            toDo.setDescription(newDescription);
            Repo.r().updateToDo(toDo);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Please fill out both title and description", Toast.LENGTH_LONG).show();
        }


    }
}