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

    //영화 검색 페이지
    lateinit var button_search: Button
    lateinit var calenderButton: Button
    lateinit var editText_keyward: EditText
    lateinit var recyclerView: RecyclerView
    lateinit var webView: WebView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.moviesearch)
        setTitle("Movie Inside")

        //영화 검색 페이지
        button_search = findViewById(R.id.button_search)
        calenderButton = findViewById(R.id.calenderButton)
        editText_keyward = findViewById(R.id.editText)
        recyclerView = findViewById(R.id.recyclerView)
        webView = findViewById(R.id.webView)


        /**********************************영화 검색 페이지****************************************/
        //웹뷰
        webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        }

        calenderButton.setOnClickListener {
            val nextIntent = Intent (this, CalenderActivity::class.java)
            startActivity(nextIntent)
        }

        button_search.setOnClickListener({
            if (editText_keyward.text.isEmpty()) {
                return@setOnClickListener
            }

            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)

            //레이아웃매니저 설정
            recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.setHasFixedSize(true)

            //API
            fetchJson(editText_keyward.text.toString())

            //키보드를 내린다.
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText_keyward.windowToken, 0)

        })
    }

    /*********************영화 검색 페이지*********************************/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.action_lotte -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://www.lottecinema.co.kr/NLCHS")
                startActivity(i)
            }
            R.id.action_cgv -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://www.cgv.co.kr/")
                startActivity(i)
                return true
            }
            R.id.action_megabox -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://www.megabox.co.kr/")
                startActivity(i)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun fetchJson(vararg p0: String) {
        //OkHttp로 요청하기
        val text = URLEncoder.encode("${p0[0]}", "UTF-8")
        println(text)
        val url = URL("https://openapi.naver.com/v1/search/movie.json?query=${text}&display=10&start=1&genre=")
        val formBody = FormBody.Builder()
            .add("query", "${text}")
            .add("display", "10")
            .add("start", "1")
            .add("genre", "1")
            .build()
        val request = Request.Builder()
            .url(url)
            .addHeader("X-Naver-Client-Id", clientId)
            .addHeader("X-Naver-Client-Secret", clientSecret)
            .method("GET", null)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                println("Success to execute request : $body")

                //Gson을 Kotlin에서 사용 가능한 object로 만든다.
                val gson = GsonBuilder().create()
                //아! 이렇게 하는구나
                val homefeed = gson.fromJson(body, Homefeed::class.java)
                //println(homefeed)

                //어답터를 연결하자. 메인쓰레드 변경하기 위해 이 메소드 사용
                runOnUiThread {
                    recyclerView.adapter = RecyclerViewAdapter(homefeed)
                    editText_keyward.setText("")
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute request")
            }
        })
    }
}

//data class
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