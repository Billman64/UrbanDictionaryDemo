package com.github.billman64.urbandictionarydemo.ViewModel


import android.content.res.Resources
import android.util.Log
import com.github.billman64.urbandictionarydemo.Model.ApiService
import com.github.billman64.urbandictionarydemo.R
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object RetrofitClient{
    private var mURL:String = "https://rapidapi.com/community/api/urban-dictionary/"   // Get resource string (while not in an activity)
    private const val TAG:String = "Nike demo"


    @Provides
    @Singleton
    fun getRetrofit():Retrofit{
        Log.d(TAG, "getRetrofit")
//        var mURL = Resources.getSystem().getString(R.string.api_url)
        checkURL(mURL)

        return Retrofit.Builder()
            .baseUrl(mURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)



    @Provides
    @Singleton
    fun checkURL(url:String):Boolean{
        // Error-trapping url - making sure http protocol is present in url
        if((url.substring(0,6) !="http://") && (url.substring(0,7) !="https://")){
            mURL = "https://$url"
        }
        return true
    }

    fun setUrl(url:String):Retrofit{

        checkURL(url)
        mURL = url

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun getUrl():String{
        return mURL
    }
}