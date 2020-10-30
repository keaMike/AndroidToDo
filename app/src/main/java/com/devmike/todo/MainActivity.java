package com.devmike.todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.devmike.todo.global.Global;
import com.devmike.todo.model.ToDo;
import com.devmike.todo.repo.Repo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Updatable {

    private ArrayAdapter<ToDo> arrayAdapter;
    private ListView listView;
    private EditText input;
    private Intent intent;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Repo.r().setActivity(this);

        builder = new AlertDialog.Builder(this);
        listView = findViewById(R.id.listView);
        input = findViewById(R.id.input);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Repo.r().getToDoList());
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(onItemClickListener);
        listView.setOnItemLongClickListener(onItemLongClickListener);
    }

    private final AdapterView.OnItemClickListener onItemClickListener = (parent, v, position, id) -> {
        intent = new Intent(this, DetailView.class);
        Global.map.put(Global.TODO_KEY, Repo.r().getToDoList().get(position));
        startActivity(intent);
    };

    private final AdapterView.OnItemLongClickListener onItemLongClickListener = (parent, v, position, id) -> {
        builder.setTitle("Warning!");
        builder.setMessage("Are you sure you want to delete item?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Repo.r().deleteToDo(Repo.r().getToDoList().get(position));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        return true;
    };

    public void addToDo(View view) {
        String title = input.getText().toString();
        if (!title.equals("")) {
            Repo.r().addToDo(new ToDo(null, title, ""));
            input.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void update() {
        runOnUiThread(() -> {
            arrayAdapter.notifyDataSetChanged();
        });
    }
}