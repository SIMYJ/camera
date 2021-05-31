package com.sample.flask_retrofit_connection

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

class DataModel {
    data class TodoInfo1(val todo1 : TaskInfo)
    data class TodoInfo2(val todo2 : TaskInfo)
    data class TodoInfo3(val todo3 : TaskInfo)

    data class TaskInfo(val task : String)
}


interface RetrofitAPI {
    @GET("/todos")//서버에 GET요청을 할 주소를 입력
    fun getTodoList() : Call<JsonObject> //MainActivity에서 사용할 json파일 가져오는 메서드
}