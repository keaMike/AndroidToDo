package com.devmike.todo.repo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.devmike.todo.Updatable;
import com.devmike.todo.model.ToDo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repo {

    private static Repo repo = new Repo();
    private Updatable activity;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private List<ToDo> toDoList = new ArrayList<>();
    private final static String TODOS = "toDos";
    private final static String TITLE = "title";
    private final static String DESCRIPTION = "description";

    public static Repo r() {
        return repo;
    }

    public void setActivity(Updatable activity) {
        this.activity = activity;
        startListener();
    }

    public void addToDo(ToDo toDo) {
        DocumentReference ref = db.collection(TODOS).document(toDo.getId());
        Map<String, String> map = new HashMap<>();
        map.put(TITLE, toDo.getTitle());
        map.put(DESCRIPTION, toDo.getDescription());
        ref.set(map).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
               Toast.makeText((Context) activity,"Failed to save item", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText((Context) activity, "Item saved", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void downloadBitMap(String fileName, Updatable activity) {
        StorageReference ref = storage.getReference(fileName);
        int max = 1024 * 1024;
        ref.getBytes(max).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            activity.update(bitmap);
        }).addOnFailureListener(System.out::println);
    }

    public void startListener() {
        db.collection(TODOS).addSnapshotListener(((values, error) -> {
            toDoList.clear();
            for (DocumentSnapshot snap : values.getDocuments()) {
                String id = snap.getId();
                String title = snap.get(TITLE).toString();
                String description = snap.get(DESCRIPTION).toString();
                toDoList.add(new ToDo(id, title, description));
            }
            activity.update();
        }));
    }

    public List<ToDo> getToDoList() {
        return toDoList;
    }

    public void updateToDo(ToDo toDo) {
        db.collection(TODOS).document(toDo.getId()).set(toDo).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText((Context) activity, "Failed to update item", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText((Context) activity, "Item updated", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deleteToDo(ToDo toDo) {
        db.collection(TODOS).document(toDo.getId()).delete().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText((Context) activity, "Failed to delete item", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText((Context) activity, "Item deleted", Toast.LENGTH_LONG).show();
            }
        });
    }

}
