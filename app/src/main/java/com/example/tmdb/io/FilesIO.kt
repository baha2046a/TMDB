package com.example.tmdb.io

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object FilesIO {
    fun <T> writeClassToFile(
        context: Context,
        content: T,
        fileName: String,
        gson: Gson = GsonService.defaultGson
    ) {
        writeStringToFile(context, gson.toJson(content), fileName)
    }

    fun writeStringToFile(context: Context, content: String, fileName: String): Boolean {
        return kotlin.runCatching {
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(content.toByteArray())
                true
            }
        }.onSuccess {
            Log.d("FileIO - Write $fileName", "Success")
        }.onFailure {
            Log.d("FileIO - Write $fileName - Fail", it.message.toString())
        }.getOrDefault(false)
    }

    inline fun <reified T> readClassFromFile(
        context: Context,
        fileName: String,
        whenEmpty: T,
        gson: Gson = GsonService.defaultGson
    ): T {
        val jsonString = readStringFromFile(context, fileName)
        if (jsonString.isEmpty()) return whenEmpty

        val type = object : TypeToken<T>() {}.type
        return kotlin.runCatching {
            gson.fromJson<T>(jsonString, type)
        }.getOrDefault(whenEmpty)
    }

    fun readStringFromFile(context: Context, fileName: String): String {
        return kotlin.runCatching {
            context.openFileInput(fileName).bufferedReader().useLines { lines ->
                lines.fold("") { some, text ->
                    "$some\n$text"
                }
            }
        }.onSuccess {
            Log.d("FileIO - Read $fileName", it)
        }.onFailure {
            Log.d("FileIO - Read $fileName - Fail", it.message.toString())
        }.getOrDefault("")
    }
}