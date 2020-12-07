package com.example.painter.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.painter.MyColor;
import com.example.painter.R;
import com.example.painter.activity.ImageShowActitvity;
import com.example.painter.model.Picture;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.BiFunction;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Picture> pictures;
    private Context context;

    public ImagesAdapter(Context context, List<Picture> pictures) {
        this.pictures = pictures;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item_image, parent, false);
        context = parent.getContext();
        return new ImagesAdapter.ViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ImagesAdapter.ViewHolder holder, int position) {
        Picture picture = pictures.get(position);
        holder.name.setText(picture.getName());
        try {
            Bitmap image_Bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(picture.getPath()));
            holder.image.setImageBitmap(Bitmap
                    .createScaledBitmap(image_Bitmap,
                            image_Bitmap.getWidth() / 2,
                            image_Bitmap.getHeight() / 2,
                            true));

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ImageShowActitvity.shownPicture = pictures.get(position);

                    Intent intent = new Intent(context, ImageShowActitvity.class);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image_Bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    intent.putExtra("image", byteArray );
                    context.startActivity(intent);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final ImageButton image;

        ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.image_name);
            image = (ImageButton) view.findViewById(R.id.image_Butt);
        }
    }
}
