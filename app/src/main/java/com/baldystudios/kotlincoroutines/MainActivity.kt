package com.baldystudios.kotlincoroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val RESULT_1 = "Result #1"
    private val RESULT_2 = "Result #2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {

            //IO, Main, Default
            CoroutineScope(IO).launch {
                fakeApiRequest()
            }

        }

    }

    private fun setNewText(input: String) {

        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }

    private suspend fun setTextOnMainThread(input: String){

        withContext(Main){
            setNewText(input)
        }

    }

    private suspend fun fakeApiRequest() {
        val result1 = getResult1FromApi()
        println("debug: $result1")
        setTextOnMainThread(result1)


        val result2 = getResult2FromApi()
        println("debug: $result2")
        setTextOnMainThread(result2)
    }

    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(): String {
        logThread("getResult2FromApi")
        delay(1000)
        return RESULT_2
    }

    private fun logThread(methodName: String) {
        println("debug: $methodName ${Thread.currentThread().name}")
    }
}
