package com.example.myapplication2

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication2.CheatActivity.Companion.newIntent
import com.example.myapplication2.CheatActivity.Companion.wasAnswerShown
import com.example.myapplication2.MainActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    private var mTrueButton: Button? = null
    private var mFalseButton: Button? = null
    private var mNextButton: ImageButton? = null
    private var mPrevButton: ImageButton? = null
    private var mCheatButton: Button? = null
    private var mQuestionTextView: TextView? = null
    private var mCheatCount: TextView? = null
    private val mQuestionBank = arrayOf(
            Question(R.string.question_australia, true, false),
            Question(R.string.question_oceans, true, false),
            Question(R.string.question_mideast, false, false),
            Question(R.string.question_africa, false, false),
            Question(R.string.question_americas, true, false),
            Question(R.string.question_asia, true, false))
    private var mCurrentIndex = 0
    private val mAnswerBank = ArrayList<Int>()
    private val mCheaterBank = ArrayList<Int>()
    private var mCorrectAnswer = 0
    private var mIsCheater = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle) called")
        setContentView(R.layout.activity_main)
        val mApiVersion = findViewById<View>(R.id.api_version) as TextView
        mApiVersion.text = "API Level " + Build.VERSION.SDK_INT.toString()
        mCheatCount = findViewById<View>(R.id.cheat_count) as TextView
        mCheatButton = findViewById<View>(R.id.cheat_button) as Button
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
            mAnswerBank.addAll(savedInstanceState.getIntegerArrayList(ANSWERS)!!)
            mCheaterBank.addAll(savedInstanceState.getIntegerArrayList(CHEAT_ANSWER)!!)
            if (mCheaterBank.size > 2) {
                mCheatButton!!.isEnabled = false
                mCheatCount!!.text = "0 cheat"
            } else if (mCheaterBank.size > 0) {
                mCheatCount!!.text = (3 - mCheaterBank.size).toString() + " cheat"
            }
        }
        mQuestionTextView = findViewById<View>(R.id.text_view) as TextView
        mTrueButton = findViewById<View>(R.id.true_button) as Button
        mTrueButton!!.setOnClickListener {
            checkAnswer(true)
            showResults()
        }
        mFalseButton = findViewById<View>(R.id.false_button) as Button
        mFalseButton!!.setOnClickListener {
            checkAnswer(false)
            showResults()
        }
        mNextButton = findViewById<View>(R.id.next_button) as ImageButton
        mNextButton!!.setOnClickListener {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.size
            mIsCheater = false
            updateQuestion()
            isAnswered
        }
        mPrevButton = findViewById<View>(R.id.prev_button) as ImageButton
        mPrevButton!!.setOnClickListener {
            mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.size
            if (mCurrentIndex < 0) mCurrentIndex = mQuestionBank.size - 1
            mIsCheater = false
            updateQuestion()
            isAnswered
        }
        mCheatButton!!.setOnClickListener {
            val answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue
            val intent = newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }
        isAnswered
        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return
            }
            mIsCheater = wasAnswerShown(data)
            if (mIsCheater) {
                mCheaterBank.add(mCurrentIndex)
                if (mCheaterBank.size > 2) {
                    mCheatButton!!.isEnabled = false
                    mCheatCount!!.text = "0 cheat"
                } else if (mCheaterBank.size > 0) {
                    mCheatCount!!.text = (3 - mCheaterBank.size).toString() + " cheat"
                }
            }
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex)
        savedInstanceState.putIntegerArrayList(ANSWERS, mAnswerBank)
        savedInstanceState.putIntegerArrayList(CHEAT_ANSWER, mCheaterBank)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart(Bundle) called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume(Bundle) called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause(Bundle) called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop(Bundle) called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy(Bundle) called")
    }

    private fun updateQuestion() {
        val questions = mQuestionBank[mCurrentIndex].textResId
        mQuestionTextView!!.setText(questions)
    }

    private fun checkAnswer(userPressedTrue: Boolean) {
        val answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue
        var messageResId = 0
        if (mCheaterBank.contains(mCurrentIndex)) {
            messageResId = R.string.judgment_toast
        } else if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast
            mCorrectAnswer++
        } else {
            messageResId = R.string.incorrect_toast
        }
        Toast.makeText(this@MainActivity, messageResId, Toast.LENGTH_SHORT).show()
        mAnswerBank.add(mCurrentIndex)
        isAnswered
    }

    private val isAnswered: Unit
        private get() {
            if (mAnswerBank.contains(mCurrentIndex)) {
                mFalseButton!!.isEnabled = false
                mTrueButton!!.isEnabled = false
            } else {
                mFalseButton!!.isEnabled = true
                mTrueButton!!.isEnabled = true
            }
        }

    private fun showResults() {
        if (mAnswerBank.size == mQuestionBank.size) {
            Toast.makeText(this@MainActivity, (100 * mCorrectAnswer / mQuestionBank.size).toString() + "%", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val KEY_INDEX = "index"
        private const val ANSWERS = "answers"
        private const val CHEAT_ANSWER = "cheat_answer"
        private const val REQUEST_CODE_CHEAT = 0
    }
}