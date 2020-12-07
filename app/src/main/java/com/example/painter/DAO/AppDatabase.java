package com.example.painter.DAO;

import androidx.room.RoomDatabase;
import androidx.room.Database;

import com.example.painter.model.Picture;
import com.example.painter.model.User;

@Database(entities = {User.class, Picture.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract PictureDao pictureDao();
}
