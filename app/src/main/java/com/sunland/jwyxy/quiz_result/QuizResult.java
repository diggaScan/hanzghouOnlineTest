package com.sunland.jwyxy.quiz_result;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "QUIZ_RESULT")
public class QuizResult {

    @PrimaryKey
    public long time_stamp;

    @ColumnInfo(name = "PAPER_CODE")
    public int paper_code;

    @ColumnInfo(name = "TOTAL_QUIZ")
    public int total_quiz;

    @ColumnInfo(name = "CORRECT_NUMS")
    public int correct_nums;

    @ColumnInfo(name = "WRONG_NUMS")
    public int wrong_nums;

    @ColumnInfo(name = "CORRECT_QUIZ")
    public String correct_quiz;

    @ColumnInfo(name = "WRONG_QUIZ")
    public String wrong_quiz;

    @ColumnInfo(name = "MARK")
    public float mark;

    @Ignore
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(time_stamp+"\n"); sb.append(paper_code+"\n"); sb.append(total_quiz+"\n");
        sb.append(correct_nums+"\n"); sb.append(wrong_nums+"\n"); sb.append(correct_quiz+"\n");
        sb.append(wrong_quiz+"\n"); sb.append(mark+"\n");
        return sb.toString();
    }
}
