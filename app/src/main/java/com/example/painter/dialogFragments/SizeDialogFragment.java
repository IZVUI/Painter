package com.example.painter.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.painter.DrawView;
import com.example.painter.R;
import com.example.painter.SizeView;
import com.example.painter.activity.DrawActivity;
import com.google.android.material.slider.Slider;

public class SizeDialogFragment extends DialogFragment {


    public String choice;
    private DrawActivity drawActivity;
    private Slider slider;
    private SizeView sizeShow;


    public SizeDialogFragment(DrawActivity drawActivity){
        this.drawActivity = drawActivity;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.size_dialog, null);



        sizeShow = (SizeView) view.findViewById(R.id.size_show_image);
        sizeShow.paint.setColor(drawActivity.getDrawView().getCurrentColor());
        sizeShow.radius = drawActivity.getDrawView().getStrokeWidth() / 1.6f;
        sizeShow.density = drawActivity.getDrawView().getmCanvas().getDensity();

        if(drawActivity.getDrawView().getCurrentColor() == drawActivity.getDrawView().getBackgroundColor()) {
            sizeShow.color = Color.BLACK;
        } else
            sizeShow.color = drawActivity.getDrawView().getBackgroundColor();

        slider = view.findViewById(R.id.size_slider);
        slider.setValueFrom(0);
        slider.setValueTo(300);
        slider.setStepSize(0.5f);
        slider.setValue(drawActivity.getDrawView().getStrokeWidth());
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
               sizeShow.paint.setStrokeWidth(1);
               sizeShow.radius = value / 1.6F;

               sizeShow.invalidate();
            }
        });



        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_size)
                .setView(view)
                .setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                        drawActivity.getDrawView()
                                .setStrokeWidth((int) slider.getValue());

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
