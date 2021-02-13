package com.example.myapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity

class DBManager(      //데이터베이스 생성자
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db!!.execSQL("CREATE TABLE person (name text, id text, pwd text, gender text, age INTEGER, tel text)")    //새로운 테이블 생성
        } catch (e: Exception) {
            e.printStackTrace()               // 에러 발생시 에러 메시지 출력
    }}


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {            //버전이 더 높다면 onUpgrade()를 통해 내용을 수정
    }
}