package com.example.painter.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.painter.DrawView;
import com.example.painter.MyColor;
import com.example.painter.R;
import com.example.painter.dialogFragments.BlurDialogFragment;
import com.example.painter.dialogFragments.ColorDialogFragment;
import com.example.painter.dialogFragments.ColorPickerDialogFragment;
import com.example.painter.dialogFragments.SaveDialogFragment;
import com.example.painter.dialogFragments.SizeDialogFragment;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class DrawActivity extends AppCompatActivity {

    DrawView drawView;
    DialogFragment sizeDialogFragment;
    DialogFragment blurDialogFragment;
    DialogFragment colorPickerDialogFragment;
    DialogFragment saveDialogFragment;
    private final int Pick_image = 1;
    public static Bitmap preBitmap;


    public DrawView getDrawView() {
        return drawView;
    }

    public int getCurrentColorIndex() {
        for (MyColor my: MyColor.getCOLORS()
             ) {
            if(my.getColor() == getDrawView().getCurrentColor()) {
                return MyColor.getCOLORS().indexOf(my);
            }
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);


        drawView = findViewById(R.id.DrawView);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        drawView.init(metrics);
        if(preBitmap != null)
            drawView.setmBitmap(preBitmap);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.rubber:
                //drawView.setCurrentColor(drawView.getBackgroundColor());
                drawView.setErase(true);
                return true;
            case R.id.color:
                drawView.setErase(false);
                /*colorDialogFragment = new ColorDialogFragment(this);
                colorDialogFragment.show(getSupportFragmentManager(), "missless");*/
                colorPickerDialogFragment = new ColorPickerDialogFragment(this);
                colorPickerDialogFragment.show(getSupportFragmentManager(), "missless");

                return true;
            case R.id.size:
                sizeDialogFragment = new SizeDialogFragment(this);
                sizeDialogFragment.show(getSupportFragmentManager(), "missless");
                return true;
            case R.id.clear:
                drawView.clear();
                return true;
            case R.id.back:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                return true;
            case R.id.save:
                saveDialogFragment = new SaveDialogFragment(this);
                saveDialogFragment.show(getSupportFragmentManager(), "missless");
                return true;
            case R.id.scaling:
                if(drawView.isScaling()){
                    drawView.setScaling(false);
                }
                else drawView.setScaling(true);

                return true;
            case R.id.blur:

                blurDialogFragment = new BlurDialogFragment(this);
                blurDialogFragment.show(getSupportFragmentManager(), "missless");

                return true;
            case R.id.pickImage:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                //Тип получаемых объектов - image:
                photoPickerIntent.setType("image/*");
                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                startActivityForResult(photoPickerIntent, Pick_image);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_draw, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case Pick_image:
                if(resultCode == RESULT_OK){
                    try {

                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        Bitmap selectedImage = Bitmap.createBitmap(BitmapFactory.decodeStream(imageStream))
                                .copy(Bitmap.Config.ARGB_8888, true);
                        if(drawView.getmBitmap().getWidth() < selectedImage.getWidth()
                        || drawView.getmBitmap().getHeight() < selectedImage.getHeight()) {
                            float kW = drawView.getmBitmap().getWidth() / ((float) selectedImage.getWidth());
                            float kH = drawView.getmBitmap().getHeight() / ((float) selectedImage.getHeight());
                            if(kW < kH)
                                selectedImage = Bitmap.createScaledBitmap(selectedImage,
                                        (int) (selectedImage.getWidth() * kW),
                                        (int) (selectedImage.getHeight() * kW),
                                        false);
                            else
                                selectedImage = Bitmap.createScaledBitmap(selectedImage,
                                        (int) (selectedImage.getWidth() * kH),
                                        (int) (selectedImage.getHeight() * kH),
                                        false);
                        }
                        drawView.setmBitmapImage(selectedImage);
                       // drawView.setmBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }}
}