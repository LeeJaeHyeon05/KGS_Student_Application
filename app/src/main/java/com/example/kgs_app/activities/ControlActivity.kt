package com.example.kgs_app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kgs_app.adapter.OrderAdapter
import com.example.kgs_app.data.OrderData
import com.example.kgs_app.databinding.ActivityControlBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firestore.v1.StructuredQuery.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ControlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityControlBinding
    val db = Firebase.firestore
    // 리사이클러뷰
    private lateinit var adapter: OrderAdapter
    private val dataList = mutableListOf<OrderData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adapter 초기화 및 RecyclerView에 설정
        adapter = OrderAdapter(dataList)
        binding.orderRecycler.layoutManager = LinearLayoutManager(this)
        binding.orderRecycler.adapter = adapter

        // Firestore에서 데이터를 가져와서 RecyclerView에 설정
        binding.refresh.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val result = db.collection("order").get().await()
                    val newList = mutableListOf<OrderData>()
                    for (document in result.documents) {
                        val item = OrderData(
                            document.getString("name") ?: "",
                            document.getString("time") ?: "",
                            document.getString("date") ?: ""
                        )
                        newList.add(item)
                    }
                    // RecyclerView에 데이터 설정
                    launch(Dispatchers.Main) {
                        dataList.clear() // 기존 데이터를 비움
                        dataList.addAll(newList) // 새로운 데이터로 대체
                        adapter.notifyDataSetChanged() // RecyclerView에 변경 사항 반영
                    }
                } catch (e: Exception) {
                    Log.e("ControlActivity", "Error getting documents: ", e)
                }
            }
        }

    }
}