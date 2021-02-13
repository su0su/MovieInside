package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
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
import java.io.IOException
import java.net.URL
import java.net.URLEncoder

open class MovieSearch : AppCompatActivity() {

    //API 연동
    val clientId = "yNz2rAgBD7KMMfeVeL7C"
    val clientSecret = "ITIJHWfc25"


    lateinit var button_search: Button          //검색 버튼
    lateinit var button_sr: Button              //음성인식 버튼
    lateinit var button_sad: Button             //아쉬워요 버튼
    lateinit var button_good: Button            //재밌어요 버튼
    lateinit var button_seen: Button            //봤어요 버튼
    lateinit var button_zzim: Button            //찜하기 버튼
    lateinit var button_netflex: Button         //넷플릭스 버튼
    lateinit var button_watcha: Button          //왓챠 버튼
    lateinit var button_youtube: Button         //유튜브 버튼
    lateinit var calenderButton: Button         //나만의 캘린더 페이지로 이동 버튼

    lateinit var editText_keyward: EditText     //영화 제목 입력 edittext
    lateinit var recyclerView: RecyclerView     //검색 결과 나오는 recyclerView
    lateinit var webView: WebView               //영화 예매 사이트 webView
    lateinit var textview6: TextView            //지금보러갈까 textView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.moviesearch)
        setTitle("Movie Inside")        //앱 이름

        button_search = findViewById(R.id.button_search)
        button_sr = findViewById(R.id.button_sr)
        button_sad = findViewById(R.id.button_sad)
        button_good = findViewById(R.id.button_good)
        button_seen = findViewById(R.id.button_seen)
        button_zzim = findViewById(R.id.button_zzim)
        button_netflex = findViewById(R.id.button_netflex)
        button_watcha = findViewById(R.id.button_watcha)
        button_youtube = findViewById(R.id.button_youtube)
        calenderButton = findViewById(R.id.calenderButton)
        editText_keyward = findViewById(R.id.editText)
        recyclerView = findViewById(R.id.recyclerView)
        webView = findViewById(R.id.webView)
        textview6=findViewById(R.id.textView6)



        //웹뷰 기본 설정
        webView.apply {
            settings.javaScriptEnabled = true                  //javaScriptEnabled 기능 키기
            webViewClient = WebViewClient()                    //WebViewClient 클래스 지정 (웹뷰에 페이지 표시)
        }



        //검색 버튼 클릭 시 API 연동 후 조회
        button_search.setOnClickListener({
            if (editText_keyward.text.length <= 0) {         //아무것도 입력하지 않았다면
                Toast.makeText(applicationContext, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            //처음 페이지에서 버튼들과 텍스트뷰 안보이게
            button_sad.visibility = View.VISIBLE
            button_good.visibility = View.VISIBLE
            button_zzim.visibility = View.VISIBLE
            button_seen.visibility = View.VISIBLE
            button_netflex.visibility = View.VISIBLE
            button_watcha.visibility = View.VISIBLE
            button_youtube.visibility = View.VISIBLE
            calenderButton.visibility = View.VISIBLE
            textview6.visibility=View.VISIBLE


            recyclerView.layoutManager = LinearLayoutManager(this)        //리사이클러뷰를 리니어 레이아웃으로 배치

            //레이아웃매니저 설정
            recyclerView.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.setHasFixedSize(true)


            fetchJson(editText_keyward.text.toString())                           //API

            //메인 엑티비티가 로드될 때 키보드 숨기기
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText_keyward.windowToken, 0)

        })


        //음성인식 버튼 클릭 시
        button_sr.setOnClickListener{
            Toast.makeText(applicationContext, "아직 준비중이에용", Toast.LENGTH_SHORT).show()      //토스트 메시지 출력
        }


        //아쉬워요 버튼 클릭 시
        button_sad.setOnClickListener{
            Toast.makeText(applicationContext, "마음에 들지 않으셨군요 ㅠㅠ", Toast.LENGTH_SHORT).show()        //토스트 메시지 출력
        }


        //재밌어요 버튼 클릭 시
        button_good.setOnClickListener{
            Toast.makeText(applicationContext, "이 영화가 마음에 드셨군요! \n나만의 캘린더에 작성해보는건 어때요?", Toast.LENGTH_SHORT).show()         //토스트 메시지 출력
        }


        var a =0;
        //봤어요 버튼 클릭 시
        button_seen.setOnClickListener{
            if (a % 2 == 0 ) {
                button_seen.setBackgroundColor(Color.BLUE)                                      //백그라운드 컬러를 파란색으로 지정
                a++
            } else {
                button_seen.setBackgroundColor(Color.parseColor("#13264e"))            //다시 클릭하면 원래 백그라운드 컬러로
                a++
            }
        }


        var b = 0;
        //찜하기 버튼 클릭 시
        button_zzim.setOnClickListener {
            if (b % 2 ==0 ) {
                button_zzim.setBackgroundColor(Color.RED)                                           //백그라운드 컬러를 빨간색으로 지정
                b++
            } else {
                button_zzim.setBackgroundColor(Color.parseColor("#13264e"))            //다시 클릭하면 원래 백그라운드 컬러로
                b++
            }
        }


        //캘린더 버튼 클릭 시 캘린더 페이지로 이동
        calenderButton.setOnClickListener {
            val nextIntent = Intent(this, CalenderActivity::class.java)        //CalenderActivity로 이동
            startActivity(nextIntent)                                                          //인텐트 실행
        }


        //넷플릭스 버튼 클릭 시
        button_netflex.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://www.netflix.com")       //넷플릭스 홈페이지로 이동
            startActivity(i)
        }


        //왓챠 버튼 클릭 시
        button_watcha.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://watcha.com/")           //왓챠 홈페이지로 이동
            startActivity(i)
        }


        //유튜브 버튼 클릭 시
        button_youtube.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://www.youtube.com/")     //유튜브 홈페이지로 이동
            startActivity(i)
        }
    }



    //옵션메뉴 리소스 지정
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true                                  //액티비티에 메뉴 있음
    }


    //옵션 메뉴 클릭 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {                        //분기 수행
            R.id.action_lotte -> {                   //롯데시네마 메뉴 클릭 시, 롯데시네마 예매 페이지 연결
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://www.lottecinema.co.kr/NLCHS")
                startActivity(i)
            }
            R.id.action_cgv -> {                     //cgv 메뉴 클릭 시, cgv 예매 페이지 연결
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://www.cgv.co.kr/")
                startActivity(i)
                return true
            }
            R.id.action_megabox -> {                 //메가박스 메뉴 클릭 시, 메가박스 예매 페이지 연결
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

        val text = URLEncoder.encode("${p0[0]}", "UTF-8")            //OkHttp로 요청하기
        val url = URL("https://openapi.naver.com/v1/search/movie.json?query=${text}&display=10&start=1&genre=")    //URL 만들기

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


        val client = OkHttpClient()                 //클라이언트 생성


        //요청 전송
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {         //요청 성공
                val body = response?.body()?.string()                           //응답 받음

                //Gson을 Kotlin에서 사용 가능한 object로 변경
                val gson = GsonBuilder().create()
                val homefeed = gson.fromJson(body, Homefeed::class.java)

                //어댑터 연결
                runOnUiThread {
                    recyclerView.adapter = RecyclerViewAdapter(homefeed)
                    editText_keyward.setText("")
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {          //요청 실패시
                println("Failed to execute request")
            }
        })
    }
}



//API에서 제공해주는 데이터 클래스
data class Homefeed(val items: List<Item>)
data class Item(
        val title: String,
        val link: String,
        val image: String,
        val subtitle: String,
        val pubDate: String,
        val director: String,
        val actor: String,
        val userRating: String
)



class RecyclerViewAdapter(val homefeed: Homefeed): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    //아이템의 갯수
    override fun getItemCount(): Int {
        return homefeed.items.count()
    }


    //각 뷰를 보관하는 Holder 객체
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): RecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_raw, parent, false)
        return ViewHolder(v)
    }


    //this method is binding the data on the list
    //리사이클러뷰가 어뎁터에 전달
    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bindItems(homefeed.items.get(position))
    }


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(data: Item) {
            var imageView: ImageView = view.findViewById(R.id.imageView)

            //Glide 형태로 검색된 영화 배치
            Glide.with(view.context).load(data.image)
                    .apply(RequestOptions().override(270, 400))
                    .apply(RequestOptions.centerCropTransform())
                    .into(imageView)
            itemView.findViewById<TextView>(R.id.textView_rating).text = data.userRating     //평점
            itemView.findViewById<TextView>(R.id.textView_subtitle).text = data.subtitle     //영문 제목
            itemView.findViewById<TextView>(R.id.textView_date).text = data.pubDate          //개봉년도
            itemView.findViewById<TextView>(R.id.textView_title).text = data.title           //영화 제목
            itemView.findViewById<TextView>(R.id.textView_actor).text = data.actor           //출연진
            itemView.findViewById<TextView>(R.id.textView_director).text = data.director     //감독


            //이미지 클릭시 영화 웹사이트 연결
            itemView.setOnClickListener({
                val webpage = Uri.parse("${data.link}")
                val webIntent = Intent(Intent.ACTION_VIEW, webpage)
                view.getContext().startActivity(webIntent);
            })
        }
    }
}