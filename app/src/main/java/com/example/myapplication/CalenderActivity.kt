package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.FileInputStream
import java.io.FileOutputStream

class CalenderActivity : MovieSearch() {
    var fname: String = ""          //소감 파일 이름
    var str: String = ""            //소감 내용
    var fname2: String = ""         //별점 파일 이름
    var str2: String = ""           //별점 내용(String)
    var flt: Float = 0.0f           //별점 내용(Float)
    var fname3: String = ""         //제목 파일 이름
    var str3: String = ""           //제목 내용

    lateinit var calendarView: CalendarView             // 캘린더 뷰
    lateinit var diaryTextView: TextView                // 날짜 표시 textView
    lateinit var save_Btn: Button                       // 저장 버튼
    lateinit var contextEditText: EditText              // 소감 입력 editText
    lateinit var textView2: TextView                    // 소감 textView
    lateinit var cha_Btn: Button                        // 수정 버튼
    lateinit var del_Btn: Button                        // 삭제 버튼
    lateinit var ratingBar: RatingBar                   // 별점 입력 ratingBar
    lateinit var ratingText: TextView                   // 별점 textView
    lateinit var titleEditText: EditText                // 제목 입력 editText
    lateinit var titleText: TextView                    // 제목 textView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calender)
        setTitle("Movie Inside")        // 앱 이름


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



        //달력 날짜 선택시
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->

            diaryTextView.visibility = View.VISIBLE              //해당 날짜가 뜨는 textView가 Visible
            save_Btn.visibility = View.VISIBLE                   //저장 버튼이 Visible
            contextEditText.visibility = View.VISIBLE            //소감 EditText가 Visible
            textView2.visibility = View.INVISIBLE                //저장된 소감 textView가 Invisible
            cha_Btn.visibility = View.INVISIBLE                  //수정 Button이 Invisible
            del_Btn.visibility = View.INVISIBLE                  //삭제 Button이 Invisible
            ratingBar.visibility = View.VISIBLE                  //별점 ratingBar가 Visible
            ratingText.visibility = View.INVISIBLE               //저장된 별점 textView가 Invisible
            titleEditText.visibility = View.VISIBLE              //제목 EditText가 Invisible
            titleText.visibility = View.INVISIBLE                //저장된 제목 textView가 Invisible

            // 날짜를 보여주는 텍스트에 해당 날짜를 넣기
            diaryTextView.text = String.format(
                "%d / %d / %d",
                year,
                month + 1,
                dayOfMonth
            )


            contextEditText.setText("")                         //소감 EditText에 공백값 넣기
            ratingBar.setRating(0.0f)                           //별점 ratingBar에 0값 주기
            titleEditText.setText("")                           //제목 EditText에 공백값 넣기

            checkedDay(year, month, dayOfMonth)       //checkedDay 메소드 호출
        }



        //저장 Button이 클릭시
        save_Btn.setOnClickListener {
            saveDiary(fname)                               //saveDiary 메소드 호출
            saveRating(fname2)                             //saveRating 메소드 호출
            saveTitle(fname3)                              //saveTitle 메소드 호출
            Toast.makeText(this@CalenderActivity, "데이터를 저장했습니다.", Toast.LENGTH_SHORT)
                .show()// 토스트 메세지
            str = contextEditText.getText().toString()     //str 변수에 소감 edittext내용을 toString형으로 저장
            str2 = ratingBar.rating.toString()             //str2 변수에 ratingBar의 별점을 toString형으로 저장
            flt = ratingBar.rating                         //flt 변수에 ratingBar의 별점을 Float형식으로 저장
            str3 = titleEditText.getText().toString()      //str3 변수에 제목 edittext내용을 toString형으로 저장

            textView2.text = "${str}"                      //textView에 소감 str 출력
            ratingText.text = "${str2}점"                  //textView에 별점 str2 출력
            titleText.text = "${str3}"                     //textView에 제목 str3 출력
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


    //checkDay 메소드가 호출될 시
    fun checkedDay(cYear: Int, cMonth: Int, cDay: Int) {
        fname =
            "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt"             //저장할 소감파일 이름 설정. Ex) 2019-01-20.txt
        fname2 =
            "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + " rating.txt"      //저장할 별점파일 이름 설정.
        fname3 = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + " title.txt"  //저장할 제목파일 이름 설정.

        var fis: FileInputStream? = null                                           //FileStream fis 변수 설정
        var fis2: FileInputStream? = null                                          //FileStream fis2 변수 설정
        var fis3: FileInputStream? = null                                          //FileStream fis3 변수 설정

        try {
            fis = openFileInput(fname)                             //fname 파일 오픈
            fis2 = openFileInput(fname2)                           //fname2 파일 오픈
            fis3 = openFileInput(fname3)                           //fname3 파일 오픈

            val fileData = ByteArray(fis.available())              //fileData에 파이트 형식으로 저장
            val fileData2 = ByteArray(fis2.available())
            val fileData3 = ByteArray(fis3.available())

            fis.read(fileData)                    //fileData를 읽음
            fis.close()
            fis2.read(fileData2)                  //fileData2를 읽음
            fis2.close()
            fis3.read(fileData3)                  //fileData3를 읽음
            fis3.close()

            str = String(fileData)               //str 변수에 fileData를 저장
            str2 = String(fileData2)             //str2 변수에 fileData를 저장
            str3 = String(fileData3)             //str3 변수에 fileData를 저장

            contextEditText.visibility = View.INVISIBLE
            textView2.visibility = View.VISIBLE
            textView2.text = "${str}"            //textView에 str 출력

            ratingBar.visibility = View.INVISIBLE
            ratingText.visibility = View.VISIBLE
            ratingText.text = "${str2}점"        //textView에 str2 출력

            titleEditText.visibility = View.INVISIBLE
            titleText.visibility = View.VISIBLE
            titleText.text = "${str3}"          //textView에 str3 출력

            save_Btn.visibility = View.INVISIBLE
            cha_Btn.visibility = View.VISIBLE
            del_Btn.visibility = View.VISIBLE



            //수정 버튼 클릭시
            cha_Btn.setOnClickListener {
                contextEditText.visibility = View.VISIBLE
                textView2.visibility = View.INVISIBLE
                contextEditText.setText(str)              //editText에 textView에 저장된 내용 출력

                ratingBar.visibility = View.VISIBLE
                ratingText.visibility = View.INVISIBLE
                ratingBar.setRating(flt)                  //ratingBar에 flt에 저장된 내용 출력

                titleEditText.visibility = View.VISIBLE
                titleText.visibility = View.INVISIBLE
                titleEditText.setText(str3)               //editText에 textView에 저장된 내용 출력

                save_Btn.visibility = View.VISIBLE
                cha_Btn.visibility = View.INVISIBLE
                del_Btn.visibility = View.INVISIBLE
                textView2.text = "${contextEditText.getText()}"
                ratingText.text = "${ratingBar.rating}"
                titleText.text = "${titleEditText.getText()}"
            }


            //삭제 버튼 클릭시
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


                //저장된 정보 삭제
                removeDiary(fname)
                removeRating(fname2)
                removeTitle(fname3)

                Toast.makeText(this@CalenderActivity, "데이터를 삭제했습니다.", Toast.LENGTH_SHORT)
                    .show()
            }


            //ratingBar 드래그 시 별점 나타내기
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
            e.printStackTrace()         //에러 발생시 에러 메시지 출력
        }
    }



    @SuppressLint("WrongConstant")

    //saveDiary 정의
    fun saveDiary(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = openFileOutput(readyDay, AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS)
            var content: String = contextEditText.getText().toString()
            fos.write(content.toByteArray())
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()        //에러 발생시 에러 메시지 출력
        }
    }


    @SuppressLint("WrongConstant")

    //saveRating 정의
    fun saveRating(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = openFileOutput(readyDay, AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS)
            var content: String = ratingBar.rating.toString()
            fos.write(content.toByteArray())
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()         //에러 발생시 에러 메시지 출력
        }
    }


    @SuppressLint("WrongConstant")

    //saveTitle 정의
    fun saveTitle(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = openFileOutput(readyDay, AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS)
            var content: String = titleEditText.getText().toString()
            fos.write(content.toByteArray())
            fos.close()

        } catch (e: Exception) {
            e.printStackTrace()         //에러 발생시 에러 메시지 출력
        }
    }


    @SuppressLint("WrongConstant")

    //removeDiary 정의
    fun removeDiary(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = openFileOutput(readyDay, AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS)
            var content: String = ""
            fos.write(content.toByteArray())
            fos.close()

        } catch (e: Exception) {
            e.printStackTrace()         //에러 발생시 에러 메시지 출력
        }
    }


    @SuppressLint("WrongConstant")

    //removeRating 정의
    fun removeRating(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = openFileOutput(readyDay, AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS)
            var content: String = ""
            fos.write(content.toByteArray())
            fos.close()

        } catch (e: Exception) {
            e.printStackTrace()         //에러 발생시 에러 메시지 출력
        }
    }


    @SuppressLint("WrongConstant")

    //removeTitle 정의
    fun removeTitle(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = openFileOutput(readyDay, AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS)
            var content: String = ""
            fos.write(content.toByteArray())
            fos.close()

        } catch (e: Exception) {
            e.printStackTrace()         //에러 발생시 에러 메시지 출력
        }
    }
}