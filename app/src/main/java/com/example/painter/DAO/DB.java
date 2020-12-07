package com.example.painter.DAO;

import androidx.room.Room;

public class DB {
    private static AppDatabase db;

    public static AppDatabase getDB() {
        return db;
    }

    public static void setDB(AppDatabase db) {
        if(DB.db == null)
        DB.db = db;
    }
}
