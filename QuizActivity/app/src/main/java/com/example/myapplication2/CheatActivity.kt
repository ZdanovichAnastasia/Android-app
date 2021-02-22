package com.example.myapplication2

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CheatActivity : AppCompatActivity() {
    private var mAnswerIsTrue = false
    private var mIsAnswerShown = false
    private var mAnswerTextView: TextView? = null
    private var mShowButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        mAnswerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        mAnswerTextView = findViewById<View>(R.id.answer_text_view) as TextView
        mShowButton = findViewById<View>(R.id.show_answer_button) as Button
        if (savedInstanceState != null) {
            mIsAnswerShown = savedInstanceState.getBoolean(EXTRA_ANSWER_SHOWN, false)
            if (mAnswerIsTrue) {
                mAnswerTextView!!.setText(R.string.true_button)
            } else {
                mAnswerTextView!!.setText(R.string.false_button)
            }
            setAnswerShownResult()
        }
        mShowButton!!.setOnClickListener {
            if (mAnswerIsTrue) {
                mAnswerTextView!!.setText(R.string.true_button)
            } else {
                mAnswerTextView!!.setText(R.string.false_button)
            }
            mIsAnswerShown = true
            setAnswerShownResult()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val cx = mShowButton!!.width / 2
                val cy = mShowButton!!.height / 2
                val radius = mShowButton!!.width.toFloat()
                val anim = ViewAnimationUtils.createCircularReveal(mShowButton, cx, cy, radius, 0f)
                anim.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        mShowButton!!.visibility = View.INVISIBLE
                    }
                })
                anim.start()
            } else {
                mShowButton!!.visibility = View.INVISIBLE
            }
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putBoolean(EXTRA_ANSWER_SHOWN, mIsAnswerShown)
    }

    private fun setAnswerShownResult() {
        val data = Intent()
        data.putExtra(EXTRA_ANSWER_SHOWN, mIsAnswerShown)
        setResult(RESULT_OK, data)
    }

    companion object {
        private const val EXTRA_ANSWER_IS_TRUE = ".answer_is_true"
        private const val EXTRA_ANSWER_SHOWN = ".answer_shown"
        @JvmStatic
        fun newIntent(packageContext: Context?, answerIsTrue: Boolean): Intent {
            val intent = Intent(packageContext, CheatActivity::class.java)
            intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            return intent
        }

        @JvmStatic
        fun wasAnswerShown(result: Intent): Boolean {
            return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
        }
    }
}