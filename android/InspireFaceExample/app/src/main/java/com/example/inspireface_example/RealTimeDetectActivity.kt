package com.example.inspireface_example

import android.app.Activity
import android.os.Bundle
import com.example.inspireface_example.render.CameraGLSurface

class RealTimeDetectActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = CameraGLSurface(this);
        setContentView(view)
    }

}