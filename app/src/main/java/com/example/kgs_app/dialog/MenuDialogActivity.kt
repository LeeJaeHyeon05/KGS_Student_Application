package com.example.kgs_app.dialog

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.service.controls.Control
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import com.example.kgs_app.R
import com.example.kgs_app.activities.ControlActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

interface OnOrderClickListener {
    fun onOrderClicked(chocoCount: Int, strawCount: Int)
}

class MenuDialogActivity (context: Context) {
    private val menuDialog = Dialog(context)
    private var chocoCount = 0
    private var strawCount = 0
    private var onOrderClickListener: OnOrderClickListener? = null

    fun setOnOrderClickListener(listener: OnOrderClickListener) {
        this.onOrderClickListener = listener
    }

    fun menuDia(receivedName: String?) {
        menuDialog.show()
        menuDialog.setContentView(R.layout.activity_menu)
        menuDialog.window!!.setLayout( WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        menuDialog.setCanceledOnTouchOutside(true)
        menuDialog.setCancelable(true)


        val choco = menuDialog.findViewById<CardView>(R.id.choco)
        val straw = menuDialog.findViewById<CardView>(R.id.straw)
        val chocoOrder = menuDialog.findViewById<TextView>(R.id.chocoOrder)
        val strawOrder = menuDialog.findViewById<TextView>(R.id.strawOrder)
        val orderBtn = menuDialog.findViewById<Button>(R.id.orderButton)


        choco.setOnClickListener {
            chocoCount++
            chocoOrder.text = "초콜릿스무디 ${chocoCount}개"
            updateOrderButtonText(orderBtn)
        }

        straw.setOnClickListener {
            strawCount++
            strawOrder.text = "딸기스무디 ${strawCount}개 주문"
            updateOrderButtonText(orderBtn)
        }
        val getInfo = receivedName
        orderBtn.setOnClickListener {
            val db = Firebase.firestore

            // 현재 날짜 가져오기
            val currentDate = Calendar.getInstance().time
            val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val formattedDate = sdf.format(currentDate)

            val currentTime = Calendar.getInstance().time
            val formatter = SimpleDateFormat("HHmm")
            val formattedTime = formatter.format(currentTime)



            val docName = formattedDate+getInfo+formattedTime

            // 클릭된 뷰의 context를 가져와서 사용
            val context = it.context

            context.startActivity(Intent(context, ControlActivity::class.java))
            val isCompleted = false

            val docRef = hashMapOf(
                "choco" to chocoCount,
                "straw" to strawCount,
                "completed" to isCompleted,
                "name" to receivedName,
                "time" to formattedTime,
                "date" to formattedDate
            )



            val dataRef = receivedName?.let { it1 -> db.collection("student").document(it1) }

            dataRef?.get()
                ?.addOnSuccessListener { document ->
                    if (document.exists()) {
                        val assets = document.getLong("assets") ?: 0
                        val newAssets = assets - (chocoCount * 30000 + strawCount * 30000)

                        // 만약 newAssets가 0 이하이면 Firestore 문서를 업데이트하지 않고 다이얼로그를 닫음
                        if (newAssets <= 0) {
                            menuDialog.dismiss()
                            Toast.makeText(it.context, "돈이 없습니다", Toast.LENGTH_SHORT).show()
                        } else {
                            db.collection("order").document(docName)
                                .set(docRef, SetOptions.merge())
                                .addOnSuccessListener { documentReference ->
                                    menuDialog.dismiss()
                                    Toast.makeText(it.context, "총 ${chocoCount+strawCount}개의 주문이 완료되었습니다\n새로고침 버튼을 눌러주세요", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                }
                            dataRef.update("assets", newAssets)
                                .addOnSuccessListener {
                                    // 업데이트 성공
                                }
                                .addOnFailureListener { e ->
                                    // 실패 처리
                                }
                        }
                    }
                }
                ?.addOnFailureListener { exception ->
                    // 실패 처리
                }


        }
    }
    private fun updateOrderButtonText(orderBtn: Button) {
        val totalCount = chocoCount + strawCount
        // 총 만동의 가격 계산
        val totalPrice = (chocoCount * 30000 + strawCount * 30000).toLong()
        // 총 만동의 가격을 천 단위마다 쉼표를 추가하여 텍스트로 변환
        val priceText = NumberFormat.getNumberInstance(Locale.getDefault()).format(totalPrice)
        orderBtn.text = "총: ${priceText}만동, 주문 (${totalCount})"

    }
}