package com.sunland.jwyxy.quiz;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface QuizDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Quiz quiz);

    @Query("SELECT * FROM QUIZ")
    public Quiz[] queryAll();

    @Query("SELECT count(*) FROM QUIZ")
    public int queryRecordNum();
}
