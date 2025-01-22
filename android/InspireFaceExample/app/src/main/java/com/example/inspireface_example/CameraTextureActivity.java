package com.example.inspireface_example;

import android.app.Activity;
import android.os.Bundle;
import android.view.TextureView;

import com.example.inspireface_example.opengl.CameraV1Pick;

public class CameraTextureActivity extends Activity {

    private TextureView mTextureView;

    private CameraV1Pick mCameraPick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyPermission();
    }

    private void applyPermission() {
        setupView();
    }


    private void setupView() {
        mTextureView = new TextureView(this);
        setContentView(mTextureView);

        mCameraPick = new CameraV1Pick();
        mCameraPick.bindTextureView(mTextureView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraPick != null) {
            mCameraPick.onDestroy();
            mCameraPick = null;
        }
    }
}