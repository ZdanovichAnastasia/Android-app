package com.example.tetris.storage

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(ctx: Context){
    val appPreferences = "APP_PREFERENCES"
    val highScore = "HIGH_SCORE"
    var data: SharedPreferences = ctx.getSharedPreferences(appPreferences, Context.MODE_PRIVATE)

    fun saveHighScore(highScore: Int){
        data.edit().putInt(this.highScore, highScore).apply()
    }
    fun getHighScore():Int{
        return data.getInt(highScore, 0)
    }
    fun clearHighScore(){
        data.edit().putInt(highScore, 0).apply()
    }
}