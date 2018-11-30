package com.sunland.jwyxy.quiz;

import android.arch.persistence.room.ColumnInfo;

public class Choice {

    @ColumnInfo(name = "A")
    public String a;

    @ColumnInfo(name = "B")
    public String b;

    @ColumnInfo(name = "C")
    public String c;

    @ColumnInfo(name = "D")
    public String d;

    @ColumnInfo(name = "ANSWER")
    public int answer;


}
