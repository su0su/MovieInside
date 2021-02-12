package com.example.myapplication

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    lateinit var signup: Button
    lateinit var join: Button
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var editId: EditText
    lateinit var editPwd: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("Movie Inside")

        signup = findViewById(R.id.signup)
        join = findViewById(R.id.join)
        editId = findViewById(R.id.editId)
        editPwd = findViewById(R.id.editPwd)

        join.setOnClickListener({
            val Joinintent = Intent(this, JoinActivity::class.java)
            startActivity(Joinintent)
        })

        signup.setOnClickListener({
            dbManager = DBManager(this, "personDB", null, 1)
            sqlitedb = dbManager.readableDatabase

            val str_id: String = editId.text.toString()
            val str_pwd: String = editPwd.text.toString()


            var cursor: Cursor
            cursor = sqlitedb.rawQuery(
                "SELECT * FROM person WHERE id = '" + str_id + "' AND pwd = '" + str_pwd + "';",
                null
            )

            if (cursor.getCount() == 1) {
                val Movieintent = Intent(this, MovieSearch::class.java)
                startActivity(Movieintent)


            } else {
                val dialog = AlertDialog.Builder(this@MainActivity)
                dialog.setTitle("로그인 오류")
                dialog.setMessage("아이디 혹은 비밀번호를 다시 확인해 주세요")
                dialog.show()
            }
            cursor.close()
        })
    }
}
