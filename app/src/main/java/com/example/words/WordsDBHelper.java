package com.example.words;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class WordsDBHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "note";//数据库名
    private final static int DATABASE_VERSION = 1;//数据库版本
    //建表SQL
    private final static String SQL_CREATE_DATABASE = "CREATE TABLE "
            + words.Word.T + " (" +
    words.Word._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            words.Word.W+ " TEXT" + "," +
            words.Word.M + " TEXT" + ","
            + words.Word.L + " TEXT" + " )";
    private final static String SQL_DELETE_DATABASE = "DROP TABLE IF EXISTS " + words.Word.T;

    public WordsDBHelper(Context context, String words, int i) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}