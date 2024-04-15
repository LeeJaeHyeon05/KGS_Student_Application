package com.example.kgs_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.kgs_app.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Timer
import java.util.TimerTask
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedName = intent.getStringExtra("student-name")
        binding.welcomeTextView.text = "안녕하세요! ${receivedName}님"

        getData(receivedName)

        val viewpager = binding.viewpager
        viewpager.adapter = ViewPagerAdapter(getAespaMembers()) // 어댑터 생성
        viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로

    }
    private fun getAespaMembers(): ArrayList<Int> {
        return arrayListOf<Int>(
            R.drawable.ptwo,
            R.drawable.pthree,
            R.drawable.pfour,
            R.drawable.pfive)
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