package com.example.painter.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.painter.activity.DrawActivity;
import com.example.painter.adapter.ColorsAdapter;
import com.example.painter.fragments.DetailFragment;
import com.example.painter.fragments.ListFragment;
import com.example.painter.MyColor;
import com.example.painter.R;

public class SaveDialogFragment extends DialogFragment {
    public int choice;
    private DrawActivity drawActivity;

    public SaveDialogFragment(DrawActivity drawActivity){
        this.drawActivity = drawActivity;
    }

    int value;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        LayoutInflater inflater = getActivity().getLayoutInflater();
        /*detailFragment = (DetailFragment) fm.findFragmentById(R.id.detail_frag);
       // detailFragment = (DetailFragment) drawActivity.getFragmentManager().findFragmentById(R.id.detail_frag);
        detailFragment.setColor(0);*/
        View view = inflater.inflate(R.layout.save_dialog, null);

        EditText saveName = (EditText) view.findViewById(R.id.saveName);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.save)
                .setView(view)
                .setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                       drawActivity.getDrawView()
                               .savePicture(saveName.getText().toString());

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