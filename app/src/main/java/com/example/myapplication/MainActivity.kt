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

    lateinit var signin: Button                  //로그인 버튼
    lateinit var join: Button                    //회원가입 버튼
    lateinit var dbManager: DBManager            //데이터베이스
    lateinit var sqlitedb: SQLiteDatabase        //데이터베이스 서버
    lateinit var editId: EditText                //아이디 입력칸
    lateinit var editPwd: EditText               //비밀번호 입력칸

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("Movie Inside")

        signin = findViewById(R.id.signin)
        join = findViewById(R.id.join)
        editId = findViewById(R.id.editId)
        editPwd = findViewById(R.id.editPwd)


        try {
            //join 클릭시
            join.setOnClickListener({
                val Joinintent = Intent(this, JoinActivity::class.java)
                startActivity(Joinintent)           //가입창으로 이동
            })

            //SIGNIN 클릭시
            signin.setOnClickListener({
                dbManager = DBManager(this, "personDB", null, 1)  //db이름과 버전을 dbmanager 함수로 넘겨줌(person DB 생성 or 연결)
                sqlitedb = dbManager.readableDatabase                                           //데이터베이스에 저장되어 있는 내용 읽기 허용

                val str_id: String = editId.text.toString()
                val str_pwd: String = editPwd.text.toString()


                var cursor: Cursor
                cursor = sqlitedb.rawQuery(
                    "SELECT * FROM person WHERE id = '" + str_id + "' AND pwd = '" + str_pwd + "';",  //입력된 아이디, 비밀번호가 DB에 저장되어 있는지 확인하는 쿼리
                    null
                )

                if (cursor.getCount() == 1) {                 //해당하는 결과가 있다면
                    val Movieintent = Intent(this, MovieSearch::class.java)
                    startActivity(Movieintent)                 //앱의 기능 사용할 수 있음 = 영화 검색창으로 연결
                } else {                                       //결과가 없다면
                    val dlg: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                    dlg.setTitle("Login Error")                //LOGIN ERROR 제목의 다이얼로그 출력
                    dlg.setMessage("아이디 혹은 패스워드를 확인해 주세요")
                    dlg.setNegativeButton("확인",null)
                    dlg.show()
                }
                cursor.close()
            })
        } catch (e: Exception) {
            e.printStackTrace()              // 에러 발생시 에러 메시지 출력
        }
    }
}

