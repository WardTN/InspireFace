package com.example.inspireface_example

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inspireface_example.adapter.FaceManagerAdapter
import com.example.inspireface_example.bean.FaceManagerItem
import com.example.inspireface_example.databinding.LayoutFaceManageBinding


class FaceManagerActivity: BaseActivity<LayoutFaceManageBinding>() {
    override fun getViewId(): Int {
        return R.layout.layout_face_manage
    }

    override fun initView() {
        super.initView()
        var list = mutableListOf<FaceManagerItem>()
        list.add(FaceManagerItem("张三","",""))
        list.add(FaceManagerItem("张三","",""))
        list.add(FaceManagerItem("张三","",""))
        list.add(FaceManagerItem("张三","",""))
        list.add(FaceManagerItem("张三","",""))
        list.add(FaceManagerItem("张三","",""))

       databinding.recy.apply {
           layoutManager = GridLayoutManager(this@FaceManagerActivity, 2) // 每行显示两个项目
           addItemDecoration(GridSpacingItemDecoration(2, 16, true)) // 2 列，16dp 间距，包含边缘
           adapter = FaceManagerAdapter(list) // 设置你的适配器
       }
    }



    // 自定义 ItemDecoration
    class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f / spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }


}