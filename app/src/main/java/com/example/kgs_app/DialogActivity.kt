package com.example.kgs_app

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class DialogActivity(context: Context) {
    private val dialog = Dialog(context)

    //학생증 1 보여주는 코드
    fun myDia(receivedName: String?) {
        dialog.show() // 보여주기
        dialog.setContentView(R.layout.activity_popup) // 레이아웃 연결
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        // 크기 설정
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        // ##
        val getInfo = receivedName
        val db = Firebase.firestore
        // 불러오기
        val flipButton = dialog.findViewById<View>(R.id.listenerView)
        val name = dialog.findViewById<TextView>(R.id.name)
        name?.let {
            db.collection("student")
                .document(getInfo.toString())
                .get()
                .addOnSuccessListener {document ->
                    if (document.exists()) {
                        val getName = document.getString("name")
                        // 텍스트 채우기
                        name.text = getName
                    }
                }
                //데이터베이스 조회 실패 했을때
                .addOnFailureListener {
                    dialog.dismiss()
                }
        }
        //다음 dialog 불러오기
        flipButton.setOnClickListener {
            dialog.dismiss()
            behindDia(userInfo = getInfo.toString())
        }

    }
    // 뒷면
    private fun behindDia(userInfo: String) {
        dialog.show()
        dialog.setContentView(R.layout.activity_info)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        val db = Firebase.firestore

        // 불러오기
        val name = dialog.findViewById<TextView>(R.id.name)
        val birth = dialog.findViewById<TextView>(R.id.birth)
        val studentClass = dialog.findViewById<TextView>(R.id.studentClass)

        db.collection("student")
            .document(userInfo)
            .get()
            .addOnSuccessListener {document ->
                if (document.exists()) {
                    val getName = document.getString("name")
                    val getBirth = document.getString("birth")
                    val getClass = document.getString("class")
                    val getGrade = document.getString("grade")
                    // 텍스트 채우기
                    name.text = "이름 : $getName"
                    birth.text = "생년월일 : $getBirth"
                    studentClass.text = "반-번호 : ${getGrade}반 ${getClass}번"
                }
            }
            //데이터베이스 조회 실패 했을때
            .addOnFailureListener {
                dialog.dismiss()
            }

        val flipBackButton = dialog.findViewById<View>(R.id.listenerView)
        flipBackButton.setOnClickListener {
            dialog.dismiss()
            myDia(receivedName = userInfo)
        }
    }
}