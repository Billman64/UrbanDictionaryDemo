package com.github.billman64.urbandictionarydemo

import android.content.ContentValues.TAG
import android.content.Context
import android.content.res.Resources
import android.util.Log

import com.github.billman64.urbandictionarydemo.ViewModel.HTTPHandler
import com.github.billman64.urbandictionarydemo.ViewModel.RetrofitClient
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import retrofit2.Retrofit
import java.net.URL



/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {

//    @Mock
//    var mContext: Context?=null

    @Mock
    var resources: Resources? = null

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

//    @Test
//    fun app_hasURL(){
//        var resources: Resources? = null
//        assertNotNull(resources?.getString(R.string.api_url).toString())
//    }

//    @Test
//    fun retrofitClient_hasURL(){
////        var resources: Resources? = null
//        val rfc:RetrofitClient? = null
//        val result = rfc?.getUrl()  // alternative: rfc?.getRetrofit()?.baseUrl().toString()
//        assertEquals(resources?.getString(R.string.api_url), result)
//    }


//    @Test
//    fun retrofitClient_canCheckURL(){
//
//        var resources: Resources? = null
//
//        val rfc:RetrofitClient?=RetrofitClient    //TODO: initialize RetrofitClient
////        rfc?.getRetrofit()
//        rfc?.checkURL("cnn.com/")
//        val result = rfc?.getUrl()
//        assertEquals( "http://cnn.com/",result.toString())
////        assertNotNull(result)
//    }


}
