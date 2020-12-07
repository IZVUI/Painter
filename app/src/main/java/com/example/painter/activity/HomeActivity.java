package com.example.painter.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.painter.DAO.DB;
import com.example.painter.MyColor;
import com.example.painter.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.painter.R;
import com.example.painter.adapter.ColorsAdapter;
import com.example.painter.adapter.ImagesAdapter;
import com.example.painter.dialogFragments.ColorDialogFragment;
import com.example.painter.dialogFragments.SaveDialogFragment;
import com.example.painter.dialogFragments.SizeDialogFragment;
import com.example.painter.model.Picture;
import com.example.painter.model.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {

    private Button openDraw;
    private TextView hello_text;
    HomeActivity such = this;
    public static User user;
    private List<Picture> pictures;
    private RecyclerView picturesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initOpenDraw();
    }


    private void initOpenDraw() {
        openDraw = (Button) findViewById(R.id.open_draw);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(such, DrawActivity.class);
                startActivity(intent);
            }
        };




        DB.getDB().pictureDao().getPictureByUserId(user.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Picture>>() {
                               @Override
                               public void accept(@NonNull List<Picture> pictures) throws Exception {
                                   setPictures(pictures);

                                   ImagesAdapter imagesAdapter = new ImagesAdapter(such, pictures);

                                   picturesList.setAdapter(imagesAdapter);
                               }
                           });


        picturesList = (RecyclerView) findViewById(R.id.images);




                        hello_text = findViewById(R.id.hello_text);
        if(user != null)
            hello_text.setText(hello_text.getText() + user.getLogin());
        else hello_text.setText("Hello");

        openDraw.setOnClickListener(listener);
    }

    private void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public void showImage(Intent intent) {
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.log_out:
                //drawView.setCurrentColor(drawView.getBackgroundColor());
                HomeActivity.user = null;
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_home, menu);
        return true;
    }
}