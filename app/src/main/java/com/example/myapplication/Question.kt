package com.example.myapplication

class Question(val id:Int,val type:QuestionType,val qText:String,
                val options : List<String>?,val answers:List<String> )

enum class QuestionType{
    Text,
    Radio,
    CheckBox
}