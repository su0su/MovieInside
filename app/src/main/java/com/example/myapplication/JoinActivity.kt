package com.example.myapplication

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.DropBoxManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class JoinActivity : AppCompatActivity() {

    lateinit var dbManager:DBManager
    lateinit var sqlitedb:SQLiteDatabase
    lateinit var btnRegist: Button
    lateinit var edtName: EditText
    lateinit var edtId: EditText
    lateinit var edtPwd: EditText
    lateinit var edtAge: EditText
    lateinit var edtTel: EditText
    lateinit var rg_gender:RadioGroup
    lateinit var rb_gender_m:RadioButton
    lateinit var rb_gender_f:RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.joinpage)
        setTitle("Movie Inside")

        btnRegist=findViewById(R.id.btnRegister)
        edtName=findViewById(R.id.edtName)
        edtId=findViewById(R.id.edtId)
        edtPwd=findViewById(R.id.edtPwd)
        edtAge=findViewById(R.id.edtAge)
        edtTel=findViewById(R.id.edtTel)
        rg_gender=findViewById(R.id.gender)
        rb_gender_m=findViewById(R.id.male)
        rb_gender_f=findViewById(R.id.female)

        dbManager=DBManager(this,"personDB",null, 1)
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
            if(rg_gender.checkedRadioButtonId==R.id.female) {
                str_gender = rb_gender_f.text.toString()
            }
            sqlitedb=dbManager.writableDatabase
            sqlitedb.execSQL("INSERT INTO person VALUES ('"+str_name+"','"+str_id+"','"+str_pwd+"','"+str_gender+"',"+str_age+",'"+str_tel+"')")
            sqlitedb.close()
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)

        }


    }



}
