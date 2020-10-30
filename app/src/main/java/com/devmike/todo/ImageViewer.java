package com.devmike.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.devmike.todo.repo.Repo;

import java.io.InputStream;

public class ImageViewer extends AppCompatActivity implements Updatable {

    private Switch aSwitch;
    private Intent intent;
    private EditText fileName;
    private ImageView imageView;
    private Bitmap currentBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        aSwitch = findViewById(R.id.switch2);
        aSwitch.setChecked(true);
        aSwitch.setOnCheckedChangeListener(onCheckedChangeListener);

        fileName = findViewById(R.id.fileName);
        imageView = findViewById(R.id.imageView);
    }

    public void downloadImage(View view) {
        String file = fileName.getText().toString();
        System.out.println(file);
        if (file.equals("")) {
            Toast.makeText(this, "Please enter a file name", Toast.LENGTH_LONG).show();
        } else {
            fileName.setText("");
            Repo.r().downloadBitMap(file, this);
        }
    }

    // Hent billede fra Album
    public void galleryBtnPressed(View view){
        Intent intent = new Intent(Intent.ACTION_PICK); // make an implicit intent, which will allow
        // the user to choose among different services to accomplish this task.
        intent.setType("image/*"); // we need to set the type of content to pick
        startActivityForResult(intent, 1); // start the activity, and in this case
        // expect an answer
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("back from image picking ");
        if(requestCode == 1) { // gallery
            backFromGallery(data);
        }
        if(requestCode == 2) { // camera
            backFromCamera(data);
        }
    }

    private void backFromGallery(@Nullable Intent data) {
        Uri uri = data.getData();
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            currentBitmap = BitmapFactory.decodeStream(is);
            imageView.setImageBitmap(currentBitmap);

        }catch (Exception e){

        }
    }

    private void backFromCamera(@Nullable Intent data) {
        try {
            currentBitmap = (Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(currentBitmap);
        }catch (Exception e){

        }
    }

    private final CompoundButton.OnCheckedChangeListener onCheckedChangeListener = (@NonNull CompoundButton buttonView, boolean isChecked) -> {
        if (!isChecked) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            aSwitch.setChecked(true);
        }
    };


    @Override
    public void update() {

    }

    @Override
    public void update(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}