package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.net.URLEncoder

open class MovieSearch : AppCompatActivity() {

    //API 연동
    val clientId = "yNz2rAgBD7KMMfeVeL7C"
    val clientSecret = "ITIJHWfc25"


    lateinit var button_search: Button          //검색 버튼
    lateinit var calenderButton: Button         //나만의 캘린더 페이지로 이동 버튼
    lateinit var editText_keyward: EditText     //영화 제목 입력 edittext
    lateinit var recyclerView: RecyclerView     //검색 결과 나오는 recyclerView
    lateinit var webView: WebView               //영화 예매 사이트 webView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.moviesearch)
        setTitle("Movie Inside") //앱 이름

        button_search = findViewById(R.id.button_search)          //검색 버튼
        calenderButton = findViewById(R.id.calenderButton)        //나만의 캘린더 페이지로 이동 버튼
        editText_keyward = findViewById(R.id.editText)            //영화 제목 입력 edittext
        recyclerView = findViewById(R.id.recyclerView)            //검색 결과 나오는 recyclerView
        webView = findViewById(R.id.webView)                       //영화 예매 사이트 webView

        //
        webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        }

        //캘린더 버튼 클릭 시 캘린더 페이지로 이동
        calenderButton.setOnClickListener {
            val nextIntent = Intent (this, CalenderActivity::class.java)        //CalenderActivity로 이동
            startActivity(nextIntent)                                                          //인텐트 실행
        }

        //검색 버튼 클릭 시 API 연동 후 조회
        button_search.setOnClickListener({
            //아무것도 입력하지 않았다면
            if (editText_keyward.text.isEmpty()) {
                return@setOnClickListener
            }

            //리사이클러뷰를 리니어 레이아웃으로 배치
            recyclerView.layoutManager = LinearLayoutManager(this)

            //레이아웃매니저 설정
            recyclerView.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.setHasFixedSize(true)

            //API
            fetchJson(editText_keyward.text.toString())

            //메인 엑티비티가 로드될 때 키보드 숨기기
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText_keyward.windowToken, 0)
        })
    }

    //옵션메뉴 리소스 지정
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true                             //액티비티에 메뉴 있음
    }

    //옵션 메뉴 클릭 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {                       //분기 수행
            R.id.action_lotte -> {                  //롯데시네마 메뉴 클릭 시, 롯데시네마 예매 페이지 연결
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://www.lottecinema.co.kr/NLCHS")
                startActivity(i)
            }
            R.id.action_cgv -> {                    //cgv 메뉴 클릭 시, cgv 예매 페이지 연결
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://www.cgv.co.kr/")
                startActivity(i)
                return true
            }
            R.id.action_megabox -> {                    //메가박스 메뉴 클릭 시, 메가박스 예매 페이지 연결
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://www.megabox.co.kr/")
                startActivity(i)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //OKHTTP 통신
    fun fetchJson(vararg p0: String) {
        //OkHttp로 요청하기
        val text = URLEncoder.encode("${p0[0]}", "UTF-8")

        //URL 만들기
        val url = URL("https://openapi.naver.com/v1/search/movie.json?query=${text}&display=10&start=1&genre=")

        //데이터를 담아 보낼 body 생성
        val formBody = FormBody.Builder()
                .add("query", "${text}")
                .add("display", "10")
                .add("start", "1")
                .add("genre", "1")
                .build()

        //OKHttp Request 생성
        val request = Request.Builder()
                .url(url)
                .addHeader("X-Naver-Client-Id", clientId)
                .addHeader("X-Naver-Client-Secret", clientSecret)
                .method("GET", null)
                .build()

        //클라이언트 생성
        val client = OkHttpClient()

        //요청 전송
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {         //요청 성공
                val body = response?.body()?.string()                           //응답 받음

                //Gson을 Kotlin에서 사용 가능한 object로 만든다.
                val gson = GsonBuilder().create()
                val homefeed = gson.fromJson(body, Homefeed::class.java)

                //어댑터 연결
                runOnUiThread {
                    recyclerView.adapter = RecyclerViewAdapter(homefeed)
                    editText_keyward.setText("")
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {          //요청 실패
                println("Failed to execute request")
            }
        })
    }
}

//데이터 클래스
data class Homefeed(val items: List<Item>)
data class Item(
        val title: String,
        val link: String,
        val image: String,
        val subtitle: String,
        val pubDate: String,
        val director: String,
        val actor: String,
        val usrRating: String
)

class RecyclerViewAdapter(val homefeed: Homefeed): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    //아이템의 갯수
    override fun getItemCount(): Int {
        return homefeed.items.count()
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): RecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_raw, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bindItems(homefeed.items.get(position))
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(data: Item) {
            var imageView: ImageView = view.findViewById(R.id.imageView)
            Glide.with(view.context).load(data.image)
                    .apply(RequestOptions().override(300, 450))
                    .apply(RequestOptions.centerCropTransform())
                    .into(imageView)
            itemView.findViewById<TextView>(R.id.textView_title).text = data.title
            itemView.findViewById<TextView>(R.id.textView_actor).text = "출연 ${data.actor}"
            itemView.findViewById<TextView>(R.id.textView_director).text = "감독 ${data.director}"

            //클릭시 웹사이트 연결
            itemView.setOnClickListener({
                val webpage = Uri.parse("${data.link}")
                val webIntent = Intent(Intent.ACTION_VIEW, webpage)
                view.getContext().startActivity(webIntent);
            })
        }
    }
}