package com.example.kgs_app.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kgs_app.adapter.OrderAdapter
import com.example.kgs_app.adapter.TeacherRecyclerAdapter
import com.example.kgs_app.data.OrderData
import com.example.kgs_app.data.TeacherData
import com.example.kgs_app.databinding.ActivityControlBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class ControlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityControlBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: OrderAdapter
    private val dataList = mutableListOf<OrderData>()
    private val itemList = arrayListOf<OrderData>()      // 아이템 배열
    private val listAdapter = OrderAdapter(itemList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentDate = Calendar.getInstance().time
        val formattedDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(currentDate)

        // Adapter 초기화 및 RecyclerView에 설정
        adapter = OrderAdapter(dataList)
        binding.orderRecycler.layoutManager = LinearLayoutManager(this)
        binding.orderRecycler.adapter = adapter

        // Firestore에서 데이터를 가져와서 RecyclerView에 설정
        binding.refresh.setOnClickListener {
            fetchData(formattedDate)
        }

    }
    private fun fetchData(formattedDate: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val result = db.collection("order")
                    .whereEqualTo("date", formattedDate)
                    .get().await()
                val newList = mutableListOf<OrderData>()

                for (document in result.documents) {
                    val chocoCount = document.getLong("choco")?.toInt() ?: 0
                    val straw = document.getLong("straw")?.toInt() ?: 0

                    // 데이터 변형
                    val dateString = formatDate(document.getString("date") ?: "")
                    val timeString = formatTime(document.getString("time") ?: "")

                    val item = OrderData(
                        document.getString("name") ?: "",
                        timeString,
                        dateString,
                        "초코 ${chocoCount}개, 딸기 ${straw}개"
                    )
                    newList.add(item)
                }


                // RecyclerView에 데이터 설정
                launch(Dispatchers.Main) {
                    dataList.clear()
                    dataList.addAll(newList)
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("ControlActivity", "에러에러에러: ", e)
                // 사용자에게 적절한 오류 메시지 표시
            }
        }
    }




    private fun formatDate(dateString: String): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val date = dateFormat.parse(dateString)
        val newDateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        return newDateFormat.format(date)
    }

    private fun formatTime(timeString: String): String {
        val timeFormat = SimpleDateFormat("HHmm", Locale.getDefault())
        val time = timeFormat.parse(timeString)
        val newTimeFormat = SimpleDateFormat("HH시 mm분", Locale.getDefault())
        return newTimeFormat.format(time)
    }
}
