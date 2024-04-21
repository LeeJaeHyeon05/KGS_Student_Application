package com.example.kgs_app.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kgs_app.adapter.TeacherRecyclerAdapter
import com.example.kgs_app.data.TeacherData
import com.example.kgs_app.databinding.ActivityTeachersBinding

class TeachersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeachersBinding

    private val itemList = arrayListOf<TeacherData>()      // 아이템 배열
    private val listAdapter = TeacherRecyclerAdapter(itemList)
    private val REQUEST_CALL_PHONE_PERMISSION = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeachersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 레이아웃 매니저와 어댑터 설정
        binding.recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = listAdapter

        // 아이템 추가
        itemList.add(TeacherData("천은숙", "090123456"))
        itemList.add(TeacherData("이주연", "923840123"))
        itemList.add(TeacherData("최진성", "923840123"))
        // 리스트가 변경됨을 어댑터에 알림
        listAdapter.notifyDataSetChanged()

        // 권한 요청
        requestCallPhonePermission()
    }

    // 전화 걸기 권한 요청
    private fun requestCallPhonePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 전화 걸기 권한이 없는 경우 권한을 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CALL_PHONE_PERMISSION
            )
        } else {
            // 전화 걸기 권한이 있는 경우
            setItemClickListener()
        }
    }

    // 전화 걸기 권한 요청 결과 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PHONE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setItemClickListener()
            }
        }
    }

    // RecyclerView 아이템 클릭 리스너 설정
       private fun setItemClickListener() {
        listAdapter.setItemClickListener(object : TeacherRecyclerAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:${itemList[position].number}")
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        })
    }
}
