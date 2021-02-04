package com.haitrvn.htlivedata

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

data class User(val name: String)

class MainActivity : AppCompatActivity() {
    private val testLiveData = SingleStateLiveData<User>() //observe 1 lần
//    private val testLiveData = StateLiveData<User>() //Observe 1 nhiều lần

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            testLiveData.postLoading(true, 50)
            testLiveData.postFailed(R.string.app_name)
            testLiveData.postSuccess(User("haitrvn"))
    }

    override fun onResume() {
        super.onResume()
        testLiveData.observeSuccess(this) { user ->
            Log.d("MainActivity", "onResume (line 25): $user")
        }.observeFailed { errorCode ->
            Log.d("MainActivity", "onResume (line 27): $errorCode")
        }.observeLoading { isLoading, progress ->
            Log.d("MainActivity", "onResume (line 29): $isLoading $progress")
        }
    }
}