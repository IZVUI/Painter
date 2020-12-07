package com.example.painter.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.painter.model.Picture;
import com.example.painter.model.User;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface PictureDao {
    @Query("SELECT * FROM picture")
    Flowable<List<Picture>> getAll();

    @Query("SELECT * FROM picture WHERE id = :id")
    Maybe<Picture> getPictureByID(long id);

    @Query("SELECT * FROM picture WHERE user_id = :user_id")
    Flowable<List<Picture>> getPictureByUserId(long user_id);

    @Insert
    long insert(Picture picture);

    @Update
    void update(Picture picture);

    @Delete
    void delete(Picture picture);
}
