package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.FileInputStream
import java.io.FileOutputStream

class CalenderActivity : MainActivity() {
    var fname: String = ""
    var str: String = ""
    var fname2: String = ""
    var str2: String = ""
    var flt: Float = 0.0f
    var fname3: String = ""
    var str3: String = ""

    lateinit var calendarView: CalendarView
    lateinit var diaryTextView: TextView
    lateinit var save_Btn: Button
    lateinit var contextEditText: EditText
    lateinit var textView2: TextView
    lateinit var cha_Btn: Button
    lateinit var del_Btn: Button
    lateinit var ratingBar: RatingBar
    lateinit var ratingText: TextView
    lateinit var titleEditText: EditText
    lateinit var titleText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calender)
        setTitle("Movie Inside")

        //캘린더 페이지
        calendarView = findViewById(R.id.calendarView)
        diaryTextView = findViewById(R.id.diaryTextView)
        save_Btn = findViewById(R.id.save_Btn)
        contextEditText = findViewById(R.id.contextEditText)
        textView2 = findViewById(R.id.textView2)
        cha_Btn = findViewById(R.id.cha_Btn)
        del_Btn = findViewById(R.id.del_Btn)
        ratingBar = findViewById(R.id.ratingBar)
        ratingText = findViewById(R.id.ratingText)
        titleEditText = findViewById(R.id.titleEditText)
        titleText = findViewById(R.id.titleText)

        /********************************************캘린더 페이지*****************************************/

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->  // 달력 날짜가 선택되면

            diaryTextView.visibility = View.VISIBLE // 해당 날짜가 뜨는 textView가 Visible
            save_Btn.visibility = View.VISIBLE // 저장 버튼이 Visible
            contextEditText.visibility = View.VISIBLE // EditText가 Visible
            textView2.visibility = View.INVISIBLE // 저장된 일기 textView가 Invisible
            cha_Btn.visibility = View.INVISIBLE // 수정 Button이 Invisible
            del_Btn.visibility = View.INVISIBLE // 삭제 Button이 Invisible
            ratingBar.visibility = View.VISIBLE
            ratingText.visibility = View.INVISIBLE
            titleEditText.visibility = View.VISIBLE
            titleText.visibility = View.INVISIBLE

            diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)// 날짜를 보여주는 텍스트에 해당 날짜를 넣는다.

            contextEditText.setText("") // EditText에 공백값 넣기
            ratingBar.setRating(0.0f)
            titleEditText.setText("")

            checkedDay(year, month, dayOfMonth) // checkedDay 메소드 호출


        }

        save_Btn.setOnClickListener { // 저장 Button이 클릭되면
            saveDiary(fname) // saveDiary 메소드 호출
            saveRating(fname2)
            saveTitle(fname3)
            Toast.makeText(this@CalenderActivity, fname + "데이터를 저장했습니다.", Toast.LENGTH_SHORT).show()// 토스트 메세지
            str = contextEditText.getText().toString() // str 변수에 edittext내용을 toString형으로 저장
            str2 = ratingBar.rating.toString()
            flt = ratingBar.rating
            str3 = titleEditText.getText().toString()

            textView2.text = "${str}" // textView에 str 출력
            ratingText.text = "${str2}점"
            titleText.text = "${str3}"
            save_Btn.visibility = View.INVISIBLE
            cha_Btn.visibility = View.VISIBLE
            del_Btn.visibility = View.VISIBLE
            contextEditText.visibility = View.INVISIBLE
            textView2.visibility = View.VISIBLE
            ratingBar.visibility = View.INVISIBLE
            ratingText.visibility = View.VISIBLE
            titleEditText.visibility = View.INVISIBLE
            titleText.visibility = View.VISIBLE
        }
    }

    fun checkedDay(cYear: Int, cMonth: Int, cDay: Int) {
        fname = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt" // 저장할 파일 이름 설정. Ex) 2019-01-20.txt
        fname2 = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + " rating.txt"
        fname3 = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + " title.txt"

        var fis: FileInputStream? = null // FileStream fis 변수 설정
        var fis2: FileInputStream? = null
        var fis3: FileInputStream? = null

        try {
            fis = openFileInput(fname) // fname 파일 오픈!!
            fis2 = openFileInput(fname2)
            fis3 = openFileInput(fname3)

            val fileData = ByteArray(fis.available()) // fileData에 파이트 형식으로 저장
            val fileData2 = ByteArray(fis2.available())
            val fileData3 = ByteArray(fis3.available())

            fis.read(fileData) // fileData를 읽음
            fis.close()
            fis2.read(fileData2)
            fis2.close()
            fis3.read(fileData3)
            fis3.close()

            str = String(fileData) // str 변수에 fileData를 저장
            str2 = String(fileData2)
            str3 = String(fileData3)

            contextEditText.visibility = View.INVISIBLE
            textView2.visibility = View.VISIBLE
            textView2.text = "${str}" // textView에 str 출력

            ratingBar.visibility = View.INVISIBLE
            ratingText.visibility = View.VISIBLE
            ratingText.text = "${str2}점"

            titleEditText.visibility = View.INVISIBLE
            titleText.visibility = View.VISIBLE
            titleText.text = "${str3}"

            save_Btn.visibility = View.INVISIBLE
            cha_Btn.visibility = View.VISIBLE
            del_Btn.visibility = View.VISIBLE

            cha_Btn.setOnClickListener { // 수정 버튼을 누를 시
                contextEditText.visibility = View.VISIBLE
                textView2.visibility = View.INVISIBLE
                contextEditText.setText(str) // editText에 textView에 저장된 내용을 출력

                ratingBar.visibility = View.VISIBLE
                ratingText.visibility = View.INVISIBLE
                ratingBar.setRating(flt)

                titleEditText.visibility = View.VISIBLE
                titleText.visibility = View.INVISIBLE
                titleEditText.setText(str3)

                save_Btn.visibility = View.VISIBLE
                cha_Btn.visibility = View.INVISIBLE
                del_Btn.visibility = View.INVISIBLE
                textView2.text = "${contextEditText.getText()}"
                ratingText.text = "${ratingBar.rating}"
                titleText.text = "${titleEditText.getText()}"
            }

            del_Btn.setOnClickListener {
                textView2.visibility = View.INVISIBLE
                ratingText.visibility = View.INVISIBLE
                titleText.visibility = View.INVISIBLE
                contextEditText.setText("")
                ratingText.setText("")
                titleEditText.setText("")
                contextEditText.visibility = View.VISIBLE
                ratingBar.visibility = View.VISIBLE
                titleEditText.visibility = View.VISIBLE
                save_Btn.visibility = View.VISIBLE
                cha_Btn.visibility = View.INVISIBLE
                del_Btn.visibility = View.INVISIBLE
                removeDiary(fname)
                removeRating(fname2)
                removeTitle(fname3)
                Toast.makeText(this@CalenderActivity, fname + "데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show()
            }

            ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                ratingBar.rating = rating

            }

            if (textView2.getText() == "") {
                textView2.visibility = View.INVISIBLE
                ratingText.visibility = View.INVISIBLE
                titleText.visibility = View.INVISIBLE
                diaryTextView.visibility = View.VISIBLE
                save_Btn.visibility = View.VISIBLE
                cha_Btn.visibility = View.INVISIBLE
                del_Btn.visibility = View.INVISIBLE
                contextEditText.visibility = View.VISIBLE
                ratingBar.visibility = View.VISIBLE
                titleEditText.visibility = View.VISIBLE
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    fun saveDiary(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = openFileOutput(readyDay, AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS)
            var content: String = contextEditText.getText().toString()
            fos.write(content.toByteArray())
            fos.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("WrongConstant")
    fun saveRating(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = openFileOutput(readyDay, AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS)
            var content: String = ratingBar.rating.toString()
            fos.write(content.toByteArray())
            fos.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("WrongConstant")
    fun saveTitle(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = openFileOutput(readyDay, AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS)
            var content: String = titleEditText.getText().toString()
            fos.write(content.toByteArray())
            fos.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("WrongConstant")
    fun removeDiary(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = openFileOutput(readyDay, AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS)
            var content: String = ""
            fos.write(content.toByteArray())
            fos.close()

        } catch (e: Exception) {
            e.printStackTrace()


        }

    }

    @SuppressLint("WrongConstant")
    fun removeRating(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = openFileOutput(readyDay, AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS)
            var content: String = ""
            fos.write(content.toByteArray())
            fos.close()

        } catch (e: Exception) {
            e.printStackTrace()


        }

    }

    @SuppressLint("WrongConstant")
    fun removeTitle(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = openFileOutput(readyDay, AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS)
            var content: String = ""
            fos.write(content.toByteArray())
            fos.close()

        } catch (e: Exception) {
            e.printStackTrace()


        }

    }
}