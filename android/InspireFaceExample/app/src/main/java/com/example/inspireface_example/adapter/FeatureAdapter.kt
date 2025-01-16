package com.example.inspireface_example.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.inspireface_example.FaceCompareActivity
import com.example.inspireface_example.FaceDetectActivity
import com.example.inspireface_example.FaceManagerActivity
import com.example.inspireface_example.R
import com.example.inspireface_example.bean.FeatureItem

class FeatureAdapter(private val context: Context, private val featureList: List<FeatureItem>) :
    RecyclerView.Adapter<FeatureAdapter.FeatureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feature_item, parent, false)
        return FeatureViewHolder(view)
    }

    override fun getItemCount(): Int {
        return featureList.size
    }

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        val featureItem = featureList[position]
        holder.title.text = featureItem.title
        holder.iv_feature.setImageResource(featureItem.image)

        // 设置背景颜色
        val color = ContextCompat.getColor(context, featureItem.color)
        val background = ColorDrawable(color)
        holder.ly_bg.background = background

        holder.itemView.setOnClickListener {
            when (position) {
                0 -> {
                    // 跳转到人脸识别页面
                    val intent = Intent(context, FaceDetectActivity::class.java)
                    context.startActivity(intent)
                }

                1 -> {
                    // 跳转到人脸识别页面
                    val intent = Intent(context, FaceCompareActivity::class.java)
                    context.startActivity(intent)
                }

                3 -> {
                    // 跳转到人脸识别页面
                    val intent = Intent(context, FaceManagerActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }


    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_feature_title)
        val iv_feature: ImageView = itemView.findViewById(R.id.iv_feature)
        val ly_bg: ConstraintLayout = itemView.findViewById(R.id.ly_bg)
    }
}