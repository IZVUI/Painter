package com.example.painter.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.painter.R;
import com.example.painter.SizeView;
import com.example.painter.activity.DrawActivity;
import com.google.android.material.slider.Slider;

public class BlurDialogFragment extends DialogFragment {


    public String choice;
    private DrawActivity drawActivity;
    private Slider slider;
    private Button inner, solid, outer, none, normal;
    private SizeView sizeShow;
    private BlurMaskFilter.Blur blurType;


    public BlurDialogFragment(DrawActivity drawActivity){
        this.drawActivity = drawActivity;
    }


    private void buttonsInit(View view) {
        inner = (Button) view.findViewById(R.id.inner_blur);
        solid = (Button) view.findViewById(R.id.solid_blur);
        outer = (Button) view.findViewById(R.id.outer_blur);
        none = (Button) view.findViewById(R.id.none_blur);
        normal = (Button) view.findViewById(R.id.normal_blur);

        inner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blurType = BlurMaskFilter.Blur.INNER;
                sizeShow.paint.setMaskFilter(new BlurMaskFilter(slider.getValue(), blurType));
                sizeShow.invalidate();
            }
        });

        solid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blurType = BlurMaskFilter.Blur.SOLID;
                sizeShow.paint.setMaskFilter(new BlurMaskFilter(slider.getValue(), blurType));
                sizeShow.invalidate();
            }
        });

        outer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blurType = BlurMaskFilter.Blur.OUTER;
                sizeShow.paint.setMaskFilter(new BlurMaskFilter(slider.getValue(), blurType));
                sizeShow.invalidate();
            }
        });

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blurType = BlurMaskFilter.Blur.NORMAL;
                sizeShow.paint.setMaskFilter(new BlurMaskFilter(slider.getValue(), blurType));
                sizeShow.invalidate();
            }
        });

        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blurType = null;
                sizeShow.paint.setMaskFilter(null);
                sizeShow.invalidate();
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.blur_dialog, null);


        buttonsInit(view);

        blurType = null;
        sizeShow = (SizeView) view.findViewById(R.id.blur_show_image);
        sizeShow.paint.setColor(drawActivity.getDrawView().getCurrentColor());
        sizeShow.radius = drawActivity.getDrawView().getStrokeWidth() / 1.6f;
        sizeShow.density = drawActivity.getDrawView().getmCanvas().getDensity();

        if(drawActivity.getDrawView().getCurrentColor() == drawActivity.getDrawView().getBackgroundColor()) {
            sizeShow.color = Color.BLACK;
        } else
            sizeShow.color = drawActivity.getDrawView().getBackgroundColor();

        slider = view.findViewById(R.id.blur_radius_slider);
        slider.setValueFrom(1);
        slider.setValueTo(50);
        slider.setStepSize(1f);
        slider.setValue(1);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                if(blurType != null)
                sizeShow.paint.setMaskFilter(new BlurMaskFilter(slider.getValue(), blurType));
                else sizeShow.paint.setMaskFilter(null);

                sizeShow.invalidate();
            }
        });



        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_size)
                .setView(view)
                .setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        if(blurType != null)
                        drawActivity.getDrawView()
                                .setBlur(new BlurMaskFilter(slider.getValue(), blurType));
                        else
                            drawActivity.getDrawView()
                                    .removeBlur();
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