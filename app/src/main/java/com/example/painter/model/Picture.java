package com.example.painter.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "picture",
foreignKeys = @ForeignKey(entity = User.class,
parentColumns = "id",
childColumns = "user_id",
onDelete = CASCADE))
public class Picture {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String path;
    private long user_id;
    private String name;

    public Picture() {
        path = "";
    }

    public Picture(String path, String name, long userId) {
        setPath(path);
        setUser_id(userId);
        setName(name);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        if (this.id == 0)
            this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
