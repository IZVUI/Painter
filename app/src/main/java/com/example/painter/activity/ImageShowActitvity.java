package com.example.painter.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.painter.DAO.DB;
import com.example.painter.R;
import com.example.painter.model.Picture;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class ImageShowActitvity extends AppCompatActivity {

    private ImageButton delete, edit, back;
    private ImageView image;
    private Bitmap bitmap;
    public static Picture shownPicture;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);

        Bundle args = getIntent().getExtras();

        delete = (ImageButton) findViewById(R.id.delete_button);
        edit = (ImageButton) findViewById(R.id.edit_button);
        back = (ImageButton) findViewById(R.id.back);

        image = (ImageView) findViewById(R.id.image_show);
            byte[] myBitmap = args.getByteArray("image");
            Bitmap bm = BitmapFactory.decodeByteArray(myBitmap,0, myBitmap.length );



            image.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //
                // Тут вы пишите ваш код, который завязан на знание размера View
                //

                bitmap = Bitmap.createScaledBitmap(
                        bm,
                        image.getWidth(),
                        image.getHeight(),
                        true
                );

                image.setImageBitmap(bitmap);

                // А тут отсоединяете OnGlobalLayoutListener
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    image.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    image.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });


        init();
    }


    private void init() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bitmap != null)
                DrawActivity.preBitmap = bitmap;

                Intent intent = new Intent(v.getContext(), DrawActivity.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setImageBitmap(BitmapFactory
                        .decodeResource(getResources(),
                                R.drawable.ic_image_placegolder));

                Completable.fromRunnable(new Runnable() {
                    @Override
                    public void run() {
                        DB.getDB().pictureDao().delete(shownPicture);
                    }
                }).subscribeOn(Schedulers.io())
                        .subscribe();

                goToHome();
            }
        });
    }


    private void goToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}