package com.sunland.jwyxy.quiz_result;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface QuizResultDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QuizResult quizResult);

    @Query("SELECT * FROM QUIZ_RESULT")
    QuizResult[] getAllResults();

    @Query("SELECT SUM(TOTAL_QUIZ) FROM QUIZ_RESULT ")
    int getQuestionNums();

    @Query("SELECT COUNT(*) FROM QUIZ_RESULT")
    int getPaperNums();

    @Query("SELECT SUM(MARK) FROM QUIZ_RESULT")
    float getTotalMarks();

    @Query("SELECT SUM(WRONG_NUMS) FROM QUIZ_RESULT")
    int getWrongNums();

    @Query("SELECT SUM(CORRECT_NUMS)FROM QUIZ_RESULT")
    int getCorrectNums();


}
