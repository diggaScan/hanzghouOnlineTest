package com.sunland.jwyxy.quiz;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.sunland.jwyxy.quiz_result.QuizResult;
import com.sunland.jwyxy.quiz_result.QuizResultDAO;


@Database(entities = {Quiz.class, QuizResult.class},version = 1,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public abstract QuizDAO getQuizDao();
    public abstract QuizResultDAO getQuizResultDao();
}
