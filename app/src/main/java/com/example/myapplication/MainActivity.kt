package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.*


import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


//For Setting 16dp margin
const val margin:Int = 16
const val EXTRA_SCORE = "score"
const val  EXTRA_TOTAL = "totalMCQ"
//Converting dp to pixel
 val Int.pixel
    get()  = (this*Resources.getSystem().displayMetrics.density).toInt()



class MainActivity : AppCompatActivity() {

    //Questions
   private var questions: MutableList<Question> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupQuestions()//Create all questions
        setupQuiz()//Setting dynamic layout
        setupSubmitButton()//check all answers
    }

    //Creating Questions
    private fun setupQuestions() {
        questions.add(Question(1,QuestionType.Radio,"Canada's birthday is on:",
                listOf("July 1st","July 10st","Jun 1st","Jun 10st"), listOf("July 1st")))

        questions.add(Question(2,QuestionType.Radio,"The capital city of Canada is:",
                listOf("Montreal","Ottawa","Toronto","Vancouver"), listOf("Ottawa")))

        questions.add(Question(3,QuestionType.CheckBox,"Canada's official languages.",
                listOf("English","French","Spanish","Hindi"), listOf("English","French")))

        questions.add(Question(3,QuestionType.Text, "Which is the world largest democratic country?",
                null,listOf("india")))

        questions.add(Question(4,QuestionType.Radio, "The capital city of India is:",
                listOf("Gujarat","Maharashtra","Delhi","Kashmir"),listOf("Delhi")))

    }

    private fun setupQuiz() {
        questions.forEachIndexed{index,element ->
            when(element.type){
                QuestionType.Text->{
                    setupTextQuestion(index,element)
                }
                QuestionType.Radio->{
                    setupRadioQuestion(index,element)
                }
                QuestionType.CheckBox->{
                    setupCheckBoxQuestion(index,element)
                }
            }
        }
    }

    //Creating Text Question UI
    private fun setupTextQuestion(counter: Int, q: Question) {
        val textView = getQuestionTextView(counter,q.qText)

        val editText = EditText(this)
        editText.id = q.id
        editText.setSingleLine(true)
        editText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

            quiz_container.apply {
                addView(textView)
                addView(editText)
            }
    }

    //Creating Radio Question UI
    private fun setupRadioQuestion(counter: Int, q: Question) {
        val textView = getQuestionTextView(counter,q.qText)

        val radioGroup = RadioGroup(this).apply {
            id = q.id
            orientation = RadioGroup.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        }

        q.options?.forEachIndexed{index,element->
            val radioButton = RadioButton(this)
            radioButton.text = element
            radioButton.id = (q.id.toString()+index.toString()).toInt()
            radioGroup.addView(radioButton)
        }
        quiz_container.addView(textView)
        quiz_container.addView(radioGroup)
    }

    //Creating CheckBox Question UI
    private fun setupCheckBoxQuestion(counter: Int, q: Question) {

        val textView = getQuestionTextView(counter,q.qText)
        quiz_container.addView(textView)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

        q.options?.forEachIndexed{index,element->
            val checkBox = CheckBox(this)
            checkBox.text = element
            checkBox.id = (q.id.toString()+index.toString()).toInt()
            checkBox.layoutParams = params
            quiz_container.addView(checkBox )
        }

    }

    //Setting question to UI
    private fun getQuestionTextView(counter: Int, question: String): TextView {
        val textView = TextView(this)
        textView.text = "Q${counter}. ${question}"
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { topMargin = margin.pixel }

        return textView
    }

    private fun setupSubmitButton() {
        val buttonParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = margin.pixel
            gravity = Gravity.CENTER_HORIZONTAL
        }

        val button= Button(this)
        button.layoutParams  = buttonParams
        button.text = "SUBMIT"
        button.setOnClickListener {
            evaluteQuiz()
        }
        quiz_container.addView(button)
    }

    //Check Answer When Button is Clicked
    @SuppressLint("SetTextI18n")
    private fun evaluteQuiz() {

        var score = 0

        questions.forEach { q->
            when(q.type)
            {
                QuestionType.Text->{
                    val editText = quiz_container.findViewById<EditText>(q.id)
                    if(q.answers[0].equals(editText.text.toString().toLowerCase()))
                    {
                        score++
                    }
                }
                QuestionType.Radio->{
                    val radioGroup = quiz_container.findViewById<RadioGroup>(q.id)
                    val answer = radioGroup.checkedRadioButtonId
                    if(answer>0)
                    {
                        val radioButton = quiz_container.findViewById<RadioButton>(answer)
                        if(radioButton.text == (q.answers[0]))
                        {
                            score++
                        }
                    }
                }

                QuestionType.CheckBox->{
                    var isQuestionCorrect = true
                    q.options?.forEachIndexed{index,element->
                        val checkBoxId = (q.id.toString()+index.toString()).toInt()
                        val checkBox = findViewById<CheckBox>(checkBoxId)
                        if(q.answers.contains(checkBox.text) )
                        {
                            if(!checkBox.isChecked)
                            {
                                isQuestionCorrect = false
                            }

                        }else{
                            if(checkBox.isChecked)
                            {
                                isQuestionCorrect = false
                            }
                        }

                    }
                    if(isQuestionCorrect) score++
                }
            }
        }

        val resultIntent = Intent(this, ShowResult::class.java).apply {
            putExtra(EXTRA_SCORE, score)
            putExtra(EXTRA_TOTAL,questions.size)
        }
//        finish()
        startActivity(resultIntent)

    }
}