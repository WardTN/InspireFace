package com.example.inspireface_example

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inspireface_example.adapter.FeatureAdapter
import com.example.inspireface_example.bean.FeatureItem

/**
 * 功能列表页面
 */
class FeatureListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_feature_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.feature_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val recyclerView: RecyclerView = findViewById(R.id.recy)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val featureList = listOf(
            FeatureItem("人脸分析", "识别并验证用户身份", R.drawable.icon_take_pic, R.color.color_1),
            FeatureItem("活体比对", "检测用户是否为真实人脸" ,R.drawable.icon_take_pic, R.color.color_2),
            FeatureItem("特征管理", "提取人脸特征", R.drawable.icon_take_pic, R.color.color_3),
            FeatureItem("人脸搜索", "在数据库中搜索匹配的人脸", R.drawable.icon_take_pic, R.color.color_4)
        )

        val adapter = FeatureAdapter(this,featureList)
        recyclerView.adapter = adapter
    }
}