package com.example.myapplication

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.DropBoxManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class JoinActivity : AppCompatActivity() {

    lateinit var dbManager:DBManager                 //데이터베이스
    lateinit var sqlitedb:SQLiteDatabase             //mysql 데이터베이스 서버
    lateinit var btnRegist: Button                   //정보 등록 버튼
    lateinit var idCheck:Button                      //아이디 중복 체크 버튼
    lateinit var edtName: EditText                   //이름 입력칸
    lateinit var edtId: EditText                     //아이디 입력칸
    lateinit var edtPwd: EditText                    //비밀번호 입력칸
    lateinit var edtAge: EditText                    //나이 입력칸
    lateinit var edtTel: EditText                    //전화번호 입력칸
    lateinit var rg_gender:RadioGroup                //성별 라디오그룹
    lateinit var rb_gender_m:RadioButton             //남성 라디오 버튼
    lateinit var rb_gender_f:RadioButton             //여성 라디오 버튼


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.joinpage)
        setTitle("Movie Inside")      //앱 이름

        btnRegist=findViewById(R.id.btnRegister)
        edtName=findViewById(R.id.edtName)
        edtId=findViewById(R.id.edtId)
        idCheck=findViewById(R.id.idCheck)
        edtPwd=findViewById(R.id.edtPwd)
        edtAge=findViewById(R.id.edtAge)
        edtTel=findViewById(R.id.edtTel)
        rg_gender=findViewById(R.id.gender)
        rb_gender_m=findViewById(R.id.male)
        rb_gender_f=findViewById(R.id.female)


        dbManager=DBManager(this,"personDB",null, 1)     //db이름과 버전을 dbmanager 함수로 넘겨줌(person DB 생성 or 연결)


        //아이디 중복 체크 클릭시
        idCheck.setOnClickListener{
            sqlitedb = dbManager.readableDatabase          //데이터베이스에 저장되어 있는 내용 읽기 허용
            val str_id:String=edtId.text.toString()

            var cursor: Cursor
            cursor = sqlitedb.rawQuery(
                "SELECT * FROM person WHERE id = '" + str_id +"';",        //아이디 존재하는지 확인하는 쿼리
                null
            )

            if (cursor.getCount() > 0) {         //아이디 존재할 시
                Toast.makeText(this@JoinActivity, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
            } else {                             //아이디 존재하지 않을 시
                    Toast.makeText(this@JoinActivity, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
                }
            cursor.close()
        }


        //등록(JOIN) 버튼 누를시
        btnRegist.setOnClickListener{
            val str_name:String=edtName.text.toString()
            val str_id:String=edtId.text.toString()
            val str_pwd:String=edtPwd.text.toString()
            var str_age:String=edtAge.text.toString()
            var str_tel:String=edtTel.text.toString()

            var str_gender:String=""
            if(rg_gender.checkedRadioButtonId==R.id.male){
                str_gender=rb_gender_m.text.toString()
            }
            if(rg_gender.checkedRadioButtonId==R.id.female) {23423
                str_gender = rb_gender_f.text.toString()
            }


            sqlitedb=dbManager.writableDatabase                                 //데이터베이스에 저장되어 있는 내용 쓰기 허용
            sqlitedb.execSQL("INSERT INTO person VALUES ('"+str_name+"','"+str_id+"','"+str_pwd+"','"+str_gender+"',"+str_age+",'"+str_tel+"')") //입력받은 정보를 테이블에 넣어주는 쿼리
            sqlitedb.close()
            Toast.makeText(this@JoinActivity, "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)                                               //가입 완료 후 로그인창으로 복귀
        }
    }
}
