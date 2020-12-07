package com.example.painter.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.painter.MyColor;
import com.example.painter.R;
import com.example.painter.activity.DrawActivity;
import com.example.painter.adapter.ColorsAdapter;
import com.example.painter.fragments.DetailFragment;
import com.example.painter.fragments.ListFragment;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

public class ColorPickerDialogFragment extends DialogFragment {


    public String choice;
    private DrawActivity drawActivity;
    private Slider alphaSlider, redSlider, greenSlider, blueSlider;
    private ImageView color;
    private Bitmap colorBitmap;
    private ImageButton greenButton, blueButton, whiteButton,
            yellowButton, blackButton, redButton,
            brownButton, orangeButton, lightBlueButton,
            purpleButton, pinkButton, grayButton;

    public ColorPickerDialogFragment(DrawActivity drawActivity){
        this.drawActivity = drawActivity;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void slidersInit(View view) {
        alphaSlider = (Slider) view.findViewById(R.id.alpha_slider);
        redSlider = (Slider) view.findViewById(R.id.red_slider);
        greenSlider = (Slider) view.findViewById(R.id.green_slider);
        blueSlider = (Slider) view.findViewById(R.id.blue_slider);

        Slider.OnChangeListener listener = new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                colorBitmap.eraseColor(Color.argb((int) alphaSlider.getValue(),
                        (int) redSlider.getValue(),
                        (int) greenSlider.getValue(),
                        (int) blueSlider.getValue()));
            }
        };

        alphaSlider.setValueFrom(0);
        alphaSlider.setValueTo(255);
        alphaSlider.setStepSize(1);
        alphaSlider.setValue((int) (Color.valueOf(drawActivity.getDrawView().getCurrentColor()).alpha() * 255));
        alphaSlider.addOnChangeListener(listener);

        redSlider.setValueFrom(0);
        redSlider.setValueTo(255);
        redSlider.setStepSize(1);
        redSlider.setValue((int) (Color.valueOf(drawActivity.getDrawView().getCurrentColor()).red() * 255));
        redSlider.addOnChangeListener(listener);

        greenSlider.setValueFrom(0);
        greenSlider.setValueTo(255);
        greenSlider.setStepSize(1);
        greenSlider.setValue((int) (Color.valueOf(drawActivity.getDrawView().getCurrentColor()).green() * 255));
        greenSlider.addOnChangeListener(listener);

        blueSlider.setValueFrom(0);
        blueSlider.setValueTo(255);
        blueSlider.setStepSize(1);
        blueSlider.setValue((int) (Color.valueOf(drawActivity.getDrawView().getCurrentColor()).blue() * 255));
        blueSlider.addOnChangeListener(listener);
    }

    private void imageButtonsInit(View view) {
        greenButton = (ImageButton) view.findViewById(R.id.green_button);
        blueButton = (ImageButton) view.findViewById(R.id.blue_button);
        whiteButton = (ImageButton) view.findViewById(R.id.white_button);
        yellowButton = (ImageButton) view.findViewById(R.id.yellow_button);
        blackButton = (ImageButton) view.findViewById(R.id.black_button);
        redButton = (ImageButton) view.findViewById(R.id.red_button);
        brownButton = (ImageButton) view.findViewById(R.id.brown_button);
        orangeButton = (ImageButton) view.findViewById(R.id.orange_button);
        lightBlueButton = (ImageButton) view.findViewById(R.id.light_blue_button);
        purpleButton = (ImageButton) view.findViewById(R.id.purple_button);
        pinkButton = (ImageButton) view.findViewById(R.id.pink_button);
        grayButton = (ImageButton) view.findViewById(R.id.gray_button);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    String colorStr = button.getTag().toString();
                    Color color = Color.valueOf(Color.parseColor(colorStr));
                    alphaSlider.setValue((int) (color.alpha() * 255));
                    redSlider.setValue((int) (color.red() * 255));
                    greenSlider.setValue((int) (color.green() * 255));
                    blueSlider.setValue((int) (color.blue() * 255));
                }
            }
        };
        greenButton.setOnClickListener(listener);
        blueButton.setOnClickListener(listener);
        whiteButton.setOnClickListener(listener);
        yellowButton.setOnClickListener(listener);
        blackButton.setOnClickListener(listener);
        redButton.setOnClickListener(listener);
        brownButton.setOnClickListener(listener);
        orangeButton.setOnClickListener(listener);
        lightBlueButton.setOnClickListener(listener);
        purpleButton.setOnClickListener(listener);
        pinkButton.setOnClickListener(listener);
        grayButton.setOnClickListener(listener);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.color_picker, null);

        color = (ImageView) view.findViewById(R.id.picked_color);

        colorBitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
        colorBitmap.eraseColor(drawActivity.getDrawView().getCurrentColor());
        color.setImageBitmap(colorBitmap);


        slidersInit(view);
        imageButtonsInit(view);


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_color)
                .setView(view)
                .setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        drawActivity.getDrawView().setCurrentColor(colorBitmap.getPixel(0, 0));


                    }})
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        return builder.create();
    }

}
