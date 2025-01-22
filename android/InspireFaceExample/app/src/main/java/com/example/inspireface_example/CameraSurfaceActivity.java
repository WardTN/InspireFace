package com.example.inspireface_example;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.example.inspireface_example.opengl.CameraSurfaceRenderer;

public class CameraSurfaceActivity extends Activity {

    private static final int PERMISSION_CODE = 100;

    private GLSurfaceView mGLSurfaceView;

    public static void intentStart(Context context) {
        Intent intent = new Intent(context, CameraSurfaceActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyPermission();
    }

    private void applyPermission() {
            setupView();
    }


    private void setupView() {
        //实例化一个GLSurfaceView
        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setEGLContextClientVersion(3);
        mGLSurfaceView.setRenderer(new CameraSurfaceRenderer(mGLSurfaceView));
        setContentView(mGLSurfaceView);
    }

}
