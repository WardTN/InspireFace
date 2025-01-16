package com.example.inspireface_example

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import java.io.InputStream

abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity() {
    lateinit var databinding: DB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootView = View.inflate(this, getViewId(), null)
        setContentView(rootView)
        databinding = DataBindingUtil.bind(rootView)!!
        databinding.lifecycleOwner = this
        initView()
        initClick()
    }

    abstract fun getViewId(): Int

    open fun initView() {}

    open fun initClick() {}

    open fun getImageUri(uri: Uri?) {}




    protected fun jumpAc(cls: Class<*>?) {
        var intent = Intent(this, cls)
        startActivity(intent)
    }



}