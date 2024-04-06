package com.example.kgs_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.example.kgs_app.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedName = intent.getStringExtra("student-name")
        binding.welcomeTextView.text = "안녕하세요! ${receivedName}님 \n아래를 눌러서 학생증을 확인해 보세요"

        getData(receivedName)
    }

    private fun getData(receivedName: String?) {
        val nameFrom = receivedName.toString()
        db.collection("student")
            .document(nameFrom)
            .get()
            .addOnSuccessListener {document ->
                if (document.exists()) {
                    val studentName = document.getString("name")
                    val studentClass= document.getString("class")
                    val studentGrade = document.getString("grade")
                    val studentBirth = document.getString("birth")

                    if (studentName != null) {
                        Log.d("dfd", studentName)
                    }
                    if (studentClass != null) {
                        Log.d("dfd", studentClass)
                    }
                    if (studentGrade != null) {
                        Log.d("dfd", studentGrade)
                    }
                    if (studentBirth != null) {
                        Log.d("dfd", studentBirth)
                    }

                }

            }
    }
}