package com.github.billman64.urbandictionarydemo.ModelView

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HTTPHandler {
    val TAG:String="Nike demo"

    @Throws(IOException::class)
    fun startHttpRequest(url: URL):String?{
        var jsonResponse=""
        var uc:HttpURLConnection? =url.openConnection() as HttpURLConnection
        var `is`: InputStream? = null

        Log.d(TAG,"url before setting connection: $url")

        
        try{
            // Connection settings with handling
            uc = url.openConnection() as HttpURLConnection
            uc.requestMethod = "GET"
            uc.readTimeout = 5000
            uc.connectTimeout = 10000
            Log.d(TAG, "Handler about to connect. uc.toString: $uc")

            // Connect and stream
            uc.connect()
            Log.d(TAG, "Connected! Url: $url")
            `is` = uc.inputStream
            Log.d(TAG, "HttpHandler input stream: "+ `is`!!.toString())

            // Output stream to a variable
            jsonResponse = streamToString(`is`)
            Log.d(TAG, "HTTPHandler url connection is good!")


            // Error handling
        } catch(e:Exception){
            Log.e(TAG, "Error. Possibly in urlConnection. uc: "+ uc!!.toString() + "\n error message: "+ e)
//            null
        } finally {
            uc?.disconnect()
            `is`?.close()
        }

    return jsonResponse
    }

    // Creates a stream into a string
    private fun streamToString(`is`: InputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var inputLine:String? = ""


        // InputStream -> StringBuilder
        try{
            // while(inputLine = bufferedReader.readLine()){ // Kotlin does not support assignment within a condition of a while()

            // Append, one line of data at time from the bufferedReader
            while( {inputLine = bufferedReader.readLine(); inputLine}() !=null){
                Log.d(TAG, "HTTPHandler - inputLine: $inputLine")
                sb.append(inputLine).append("\n")
            }

        // Error handling
        } catch(e:Exception){
            e.printStackTrace()
        } finally{
            try{
                `is`.close()        // Close connection.
            } catch(e:IOException) {        // IO exception handling
                Log.e(TAG, "IO Exception")
                e.printStackTrace()
            }

        }

        return sb.toString()
    }


}
