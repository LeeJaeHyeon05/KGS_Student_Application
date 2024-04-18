package com.example.kgs_app

import android.app.Dialog
import android.content.Context
import android.view.WindowManager

class DialogActivity(context: Context) {
    private val dialog = Dialog(context)

    fun myDia() {
        dialog.show() // 보여주기
        dialog.setContentView(R.layout.activity_popup) // 레이아웃 연결
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        // 크기 설정
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        // 외부 터치 캔슬 막리

    }
}