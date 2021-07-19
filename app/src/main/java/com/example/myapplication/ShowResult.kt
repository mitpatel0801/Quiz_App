package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Layout
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_show_result2.*

class ShowResult : AppCompatActivity() {

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_result2)
        setSupportActionBar(findViewById(R.id.toolbar))

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            topMargin = margin.pixel*10
            }

        val score =  intent.getIntExtra(EXTRA_SCORE,-1)
        val total = intent.getIntExtra(EXTRA_TOTAL,-1)
        val textView = TextView(this).apply {
            text = "Your correct answer is ${score} out of ${total}"
            gravity = Gravity.CENTER_HORIZONTAL
            layoutParams = params
        }


        val resetButton = Button(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                topMargin = margin.pixel*2
                }

            text = "RESET QUIZ"

        }
        resetButton.setOnClickListener{
            intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent);
        }


        result_container.addView(textView )
        result_container.addView(resetButton)



    }
}