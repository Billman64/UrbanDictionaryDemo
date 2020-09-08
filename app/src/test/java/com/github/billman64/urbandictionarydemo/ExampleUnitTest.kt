package com.github.billman64.urbandictionarydemo

import android.content.ContentValues.TAG
import android.util.Log

import com.github.billman64.urbandictionarydemo.ModelView.HTTPHandler
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import java.net.URL



/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun httpHandler_canConnect(){
        val handler = HTTPHandler()
        val result:String? = handler.startHttpRequest(URL("http://www.google.com")).toString()
        Log.d(TAG, "HTTPHandler result: "+ result)
        assertNotNull(result)
    }

}
