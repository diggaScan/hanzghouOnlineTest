package com.sunland.jwyxy.quiz;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "QUIZ")
public class Quiz {
    public Quiz(){

    }
    @PrimaryKey
    @ColumnInfo(name = "ID")
    public int Id;

    @ColumnInfo(name = "TITLE")
    public String title;

    @Embedded
    public Choice choice;

    @ColumnInfo(name = "KIND")
    public int kind;

    @Ignore
    public Quiz(int id, String title, Choice choice, int kind){
        this.Id=id;
        this.title=title;
        this.choice=choice;

        this.kind=kind;
    }


}
