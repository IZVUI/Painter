package com.example.painter.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.painter.activity.DrawActivity;
import com.example.painter.adapter.ColorsAdapter;
import com.example.painter.fragments.DetailFragment;
import com.example.painter.fragments.ListFragment;
import com.example.painter.MyColor;
import com.example.painter.R;

public class ColorDialogFragment extends DialogFragment
        implements ListFragment.ListListener{
    public int choice;
    private DrawActivity drawActivity;
    DetailFragment detailFragment;

    public ColorDialogFragment(DrawActivity drawActivity){
        this.drawActivity = drawActivity;
    }

    int value;

    @Override
    public void itemClicked(long id) {
        detailFragment.setColor(id);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        LayoutInflater inflater = getActivity().getLayoutInflater();
        /*detailFragment = (DetailFragment) fm.findFragmentById(R.id.detail_frag);
       // detailFragment = (DetailFragment) drawActivity.getFragmentManager().findFragmentById(R.id.detail_frag);
        detailFragment.setColor(0);*/
        View view = inflater.inflate(R.layout.color_dialog, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.color_list);
        ColorsAdapter colorsAdapter = new ColorsAdapter(getActivity(), MyColor.getCOLORS());

        recyclerView.setAdapter(colorsAdapter);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_color)
                //.setView(view)
                .setSingleChoiceItems(MyColor.getCOLORSNames(), drawActivity.getCurrentColorIndex(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice = which;
                    }
                })
                .setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        drawActivity.getDrawView().setCurrentColor(
                                MyColor.getCOLORS()
                                .get(choice)
                                .getColor());

                        /*drawActivity.getDrawView().setCurrentColor(
                                detailFragment.getColor()
                        );*/

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
