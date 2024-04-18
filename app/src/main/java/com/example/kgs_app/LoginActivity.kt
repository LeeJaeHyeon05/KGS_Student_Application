package com.example.kgs_app

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import com.example.kgs_app.databinding.ActivityLoginBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class LoginActivity : AppCompatActivity() {


    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //로그인 애니메이션 함수
        loginAnimation()
        // 로그인 함수
        checkLogin()
        binding.testBtn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

    }
    //로그인 코드
    private fun checkLogin() {
        binding.loginButton.setOnClickListener {
            val editName = binding.studentName.text.toString().trim()
            val editNum = binding.studentPassword.text.toString().trim()

            db.collection("student")
                .document(editName)
                .get()
                .addOnSuccessListener {document ->
                    if (document.exists()) {
                        val correctPassword = document.getString("code")
                        if (editNum == correctPassword) {
                            startActivity(Intent(this, MainActivity::class.java).apply {
                                putExtra("student-name", editName)
                            })
                            finish()
                        } else {
                            // 비밀번호 틀렸을때
                            Toast.makeText(this, getString(R.string.passwordFail), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // 해당하는 이름의 계정이 없을때
                        Toast.makeText(this, getString(R.string.noName), Toast.LENGTH_SHORT).show()
                    }
                }
                //데이터베이스 조회 실패 했을때
                .addOnFailureListener {
                    Toast.makeText(this, getString(R.string.failCheckDatabase), Toast.LENGTH_SHORT).show()
                }
        }
    }

    //로그인 애니메이션
    private fun loginAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        binding.mainImage.startAnimation(animation)


        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val sName = binding.studentName
        val sNum = binding.studentPassword

        val editList = listOf<EditText>(sName, sNum)
        editList.forEach { editText ->
            editText.startAnimation(fadeInAnimation)
        }
    }
}