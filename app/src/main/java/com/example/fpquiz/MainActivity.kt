package com.example.fpquiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fpquiz.ui.quiz.QuizFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, QuizFragment())
                .commit()
        }
    }
}