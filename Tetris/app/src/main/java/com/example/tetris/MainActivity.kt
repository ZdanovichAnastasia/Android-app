package com.example.tetris

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tetris.storage.AppPreferences
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    var tHighScore: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val btnNewGame: Button = findViewById(R.id.btn_new_game)
        val btnResetScore: Button = findViewById(R.id.btn_reset_score)
        val btnExit: Button = findViewById(R.id.btn_exit)
        btnNewGame.setOnClickListener(this::onBtnNewGameClick)
        btnResetScore.setOnClickListener(this::onBtnResetScoreClick)
        btnExit.setOnClickListener(this::onBtnExitClick)
    }
    private fun onBtnNewGameClick(view: View){
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }
    private fun onBtnResetScoreClick(view: View){
        val preferences = AppPreferences(this)
        preferences.clearHighScore()
        Snackbar.make(view, "Score successfully reset", Snackbar.LENGTH_SHORT).show()
        tHighScore?.text = "High score: ${preferences.getHighScore()}"
    }

    private fun onBtnExitClick(view: View) {
        System.exit(0)
    }
}