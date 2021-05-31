package com.sample.flask_retrofit_connection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sample.flask_retrofit_connection.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    val TAG = "TAG_MainActivity"//로그를 분류할 태그입니다.

    lateinit var mRetrofit : Retrofit // 사용할 레트로핏 객체입니다.
    lateinit var mRetrofitAPI: RetrofitAPI // 레트로핏 api객체입니다.
    lateinit var mCallTodoList : retrofit2.Call<JsonObject> // Json형식의 데이터를 요청하는 객체입니다.


    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setRetrofit()//레트로핏 세팅


        //버튼 클릭하면 가져오기
        binding.button1.setOnClickListener {
            binding.button1.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
            callTodoList()



        }
    }

    // 리스트를 불러온다.
    private fun callTodoList() {
        mCallTodoList = mRetrofitAPI.getTodoList()
        mCallTodoList.enqueue(mRetrofitCallback)//응답을 큐 대기열에 넣는다.
    }



    private fun setRetrofit(){
        //레트로핏으로 가져올 url설정하고 세팅
        mRetrofit = Retrofit
            .Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //인터페이스로 만든 레트로핏 api요청 받는 것 변수로 등록
        mRetrofitAPI = mRetrofit.create(RetrofitAPI::class.java)
    }


    //http요청을 보냈고 이건 응답을 받을 콜벡메서드
    private val mRetrofitCallback  = (object : retrofit2.Callback<JsonObject>{//Json객체를 응답받는 콜백 객체

        //응답을 가져오는데 실패
        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            t.printStackTrace()
            Log.d(TAG, "에러입니다. => ${t.message.toString()}")
            binding.textView.text = "에러\n" + t.message.toString()

            binding.progressBar.visibility = View.GONE
            binding.button1.visibility = View.VISIBLE
        }
        //응답을 가져오는데 성공 -> 성공한 반응 처리
        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            val result = response.body()
            Log.d(TAG, "결과는 => $result")

            var mGson = Gson()
            val dataParsed1 = mGson.fromJson(result, DataModel.TodoInfo1::class.java)
            val dataParsed2 = mGson.fromJson(result, DataModel.TodoInfo2::class.java)
            val dataParsed3 = mGson.fromJson(result, DataModel.TodoInfo3::class.java)

            binding.textView.text = "해야할 일\n" + dataParsed1.todo1.task+"\n"+dataParsed2.todo2.task +"\n"+dataParsed3.todo3.task

            binding.progressBar.visibility = View.GONE
            binding.button1.visibility = View.VISIBLE
        }
    })



}