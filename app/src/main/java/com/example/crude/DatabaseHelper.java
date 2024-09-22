package com.example.crude;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "skills.db";
    private static final String TABLE_NAME = "skills_table";
    private static final String COL_2 = "SKILL";
    private static final String COL_3 = "DATE";
    private static final String COL_4 = "TIME";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SKILL TEXT, DATE TEXT, TIME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String skill, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, skill);
        contentValues.put(COL_3, date);
        contentValues.put(COL_4, time);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }
    public boolean updateDataBySkill(String oldSkill, String newSkill, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("SKILL", newSkill);
        contentValues.put("DATE", date);
        contentValues.put("TIME", time);
        return db.update(TABLE_NAME, contentValues, "SKILL = ?", new String[]{oldSkill}) > 0;
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT ID as _id, SKILL, DATE, TIME FROM " + TABLE_NAME, null);
    }
    public boolean deleteDataBySkill(String skill) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "skill = ?", new String[]{skill}) > 0;
    }

}
