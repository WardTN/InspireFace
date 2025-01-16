package com.example.inspireface_example.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.inspireface_example.R
import com.example.inspireface_example.bean.FaceBean
import com.example.inspireface_example.widget.InputTextView

class AddFaceDataDialog : DialogFragment() {
    var iv: ImageView? = null
    var path: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //设置背景透明
        val view: View = inflater.inflate(R.layout.dialog_add_item, container, false)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }


    private fun initView(view: View) {

        path = requireArguments().getParcelable("image_uri")
        iv = view.findViewById(R.id.iv)
        iv?.setImageURI(path)

        var et = view.findViewById<InputTextView>(R.id.input)
        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            dismiss()
        }

        view.findViewById<TextView>(R.id.tv_ok).setOnClickListener {
            var name = et.text.toString()
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(requireContext(), "Input Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            path?.let {
                val faceBean = FaceBean(name, it)
                listener?.addFaceData(faceBean)
            }
            dismiss()
        }
    }


    override fun show(manager: FragmentManager, tag: String?) {
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    override fun onStart() {
        super.onStart()
        val dm = DisplayMetrics()
        activity?.let {
            it?.windowManager?.defaultDisplay?.getMetrics(dm)
            dialog!!.window!!.setLayout(dm.widthPixels, dm.heightPixels)
        }
    }

    fun bindData( listener: AddFaceListener) {
        this.listener = listener
    }


    private var listener: AddFaceListener? = null

    interface AddFaceListener {
        fun addFaceData(bean: FaceBean)
    }

}