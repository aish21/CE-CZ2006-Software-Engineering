package com.example.trailx

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.net.URL
import java.nio.charset.Charset
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object WeatherForecast {
    @Throws(IOException::class)
    private fun readAll(rd:Reader):String {
        val sb = StringBuilder()
        var cp:Int
        while ((rd.read().also { cp = it }) != -1)
        {
            sb.append(cp.toChar())
        }
        return sb.toString()
    }
    @Throws(IOException::class, JSONException::class)
    fun readJsonFromUrl(url:String):JSONObject {
        val `is` = URL(url).openStream()
        try
        {
            val rd = BufferedReader(InputStreamReader(`is`, Charset.forName("UTF-8")))
            val jsonText = readAll(rd)
            val json = JSONObject(jsonText)
            return json
        }
        finally
        {
            `is`.close()
        }
    }
    @Throws(IOException::class, JSONException::class)
    @JvmStatic fun main(args:Array<String>) {
        val json = readJsonFromUrl("https://api.data.gov.sg/v1/environment/24-hour-weather-forecast")
        val items = json.get("items") as JSONArray //get all data with the key "items"
        //we're converting all JSON objects into arrays for ease of traversal and searching for elements
        val itemsElement = items.getJSONObject(0) //gets first element of item
        val general = itemsElement.getJSONObject("general")
        val forecast = general.getString("forecast")
        print(forecast)
    }
}