package com.example.inspireface_example.opengl;

import android.graphics.SurfaceTexture;

public interface ICamera {

    boolean openCamera(int cameraId);

    void enablePreview(boolean enable);

    void setPreviewTexture(SurfaceTexture surfaceTexture);

    void closeCamera();
}