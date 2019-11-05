package com.baldystudios.kotlincoroutines

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {

    private val RESULT_1 = "Result #1"
    private val RESULT_2 = "Result #2"
    private val JOB_TIMEOUT = 1900L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        normalJobButton.setOnClickListener {

            //IO, Main, Default
            CoroutineScope(IO).launch {
                fakeApiRequest()
            }

        }

        jobWithTimeoutButton.setOnClickListener {
            CoroutineScope(IO).launch {
                fakeApiRequestWithTimeout()
            }

        }

        jobWithTimeoutButton.setBackgroundColor(-5301441)

    }

    private fun setNewText(input: String) {

        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {

        withContext(Main) {
            setNewText(input)
        }

    }


    private suspend fun fakeApiRequestWithTimeout() {

        withContext(IO) {

            val job = withTimeoutOrNull(JOB_TIMEOUT) {
                val result1 = getResult1FromApi()
                println("debug: $result1")
                setTextOnMainThread(result1)


                val result2 = getResult2FromApi()
                println("debug: $result2")
                setTextOnMainThread(result2)
            }

            if (job == null) {

                val cancelJob = "Cancelling job... job took longer than $JOB_TIMEOUT ms"
                setTextOnMainThread(cancelJob)

            }

        }
    }

    private suspend fun fakeApiRequest() {

        withContext(IO) {

            val job = launch {
                val result1 = getResult1FromApi()
                println("debug: $result1")
                setTextOnMainThread(result1)


                val result2 = getResult2FromApi()
                println("debug: $result2")
                setTextOnMainThread(result2)
            }

        }
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
