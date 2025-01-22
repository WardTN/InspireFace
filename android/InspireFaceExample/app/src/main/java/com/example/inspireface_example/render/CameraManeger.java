package com.example.inspireface_example.render;

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;


import com.example.inspireface_example.widget.CameraProxy;

import java.io.IOException;
import java.util.List;

/**
 * 摄像头控制类
 */
public class CameraManeger {

    private static final String TAG = "CameraManeger";

    private Camera mCamera;
    private Camera.Parameters mParameters;
    private Camera.CameraInfo mCameraInfo = new Camera.CameraInfo();
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private int mPreviewWidth = 1080;
    private int mPreviewHeight = 2311;
    private final float mPreviewScale;
    private Camera.PreviewCallback mPreviewCallback; // 相机预览的数据回调
    private int mLatestRotation = 0;

    private byte[] mPreviewBuffer;
    private Activity activity;


    public CameraManeger(Context context) {

        Log.e("CHEN", "mPreviewWidth=" + mPreviewWidth + ", mPreviewHeight=" + mPreviewHeight);

        mPreviewScale = mPreviewHeight * 1f / mPreviewWidth;
        this.activity = (Activity) context;
    }


    //开启前置
    public void OpenCamera(SurfaceTexture surfaceTexture, Camera.PreviewCallback callback) throws IOException {
        mCamera = Camera.open(CAMERA_FACING_FRONT);
        Camera.getCameraInfo(mCameraId, mCameraInfo);
//        mParameters = mCamera.getParameters();
        initConfig();
        setDisplayOrientation();
        mCamera.setPreviewTexture(surfaceTexture);
        mCamera.setPreviewCallback(callback);
        mCamera.startPreview();
    }

    public void openCamera(int cameraId) {
        mCameraId = cameraId;
        Log.d(TAG, "openCamera cameraId: " + mCameraId);
        mCamera = Camera.open(mCameraId);
        Camera.getCameraInfo(mCameraId, mCameraInfo);
        mParameters = mCamera.getParameters();
        initConfig();
        setDisplayOrientation();
        Log.d(TAG, "openCamera enable mOrientationEventListener");
    }

    public void openCamera() {
        Log.d(TAG, "openCamera cameraId: " + mCameraId);
        mCamera = Camera.open(mCameraId);
        Camera.getCameraInfo(mCameraId, mCameraInfo);
        initConfig();
        setDisplayOrientation();
        Log.d(TAG, "openCamera enable mOrientationEventListener");
    }

    public void releaseCamera() {
        if (mCamera != null) {
            Log.v(TAG, "releaseCamera");
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void startPreview(SurfaceHolder holder) {
        if (mCamera != null) {
            Log.v(TAG, "startPreview");
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();
            setPreviewCallback((bytes, camera) -> {
                if (onCameraPreviewCallBack != null) {
                    onCameraPreviewCallBack.onPreviewFrame(bytes, camera);
                }
            });
        }
    }

    public void startPreview(SurfaceTexture surface) {
        if (mCamera != null) {
            Log.v(TAG, "startPreview");
            try {
                mCamera.setPreviewTexture(surface);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();
        }
    }

    public void stopPreview() {
        if (mCamera != null) {
            Log.v(TAG, "stopPreview");
            mCamera.stopPreview();
        }
    }

    public boolean isFrontCamera() {
        return mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
    }

    private void initConfig() {
        Log.v(TAG, "initConfig");
        try {
            mParameters = mCamera.getParameters();
            // 如果摄像头不支持这些参数都会出错的，所以设置的时候一定要判断是否支持
            List<String> supportedFlashModes = mParameters.getSupportedFlashModes();
            if (supportedFlashModes != null && supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF); // 设置闪光模式
            }
            List<String> supportedFocusModes = mParameters.getSupportedFocusModes();
            if (supportedFocusModes != null && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO); // 设置聚焦模式
            }
            mParameters.setPreviewFormat(ImageFormat.NV21); // 设置预览图片格式
            mParameters.setPictureFormat(ImageFormat.JPEG); // 设置拍照图片格式
            mParameters.setExposureCompensation(0); // 设置曝光强度
            Camera.Size previewSize = getSuitableSize(mParameters.getSupportedPreviewSizes());
            mPreviewWidth = previewSize.width;
            mPreviewHeight = previewSize.height;
            mParameters.setPreviewSize(mPreviewWidth, mPreviewHeight); // 设置预览图片大小
            Log.d(TAG, "previewWidth: " + mPreviewWidth + ", previewHeight: " + mPreviewHeight);
            Camera.Size pictureSize;

            pictureSize = getSuitableSize(mParameters.getSupportedPictureSizes());
            mParameters.setPictureSize(pictureSize.width, pictureSize.height);
            Log.d(TAG, "pictureWidth: " + pictureSize.width + ", pictureHeight: " + pictureSize.height);
            mCamera.setParameters(mParameters); // 将设置好的parameters添加到相机里
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Camera.Size getMaxPixel(List<Camera.Size> sizes) {
        int index = 0;
        int maxPixel = 0;
        for (int i = 0; i < sizes.size(); i++) {
            if (sizes.get(i).width > maxPixel) {
                maxPixel = sizes.get(i).width;
                index = i;
            }
        }
        return sizes.get(index);
    }

    private Camera.Size getSuitableSize(List<Camera.Size> sizes) {
        int minDelta = Integer.MAX_VALUE; // 最小的差值，初始值应该设置大点保证之后的计算中会被重置
        int index = 0; // 最小的差值对应的索引坐标
        for (int i = 0; i < sizes.size(); i++) {
            Camera.Size size = sizes.get(i);
            Log.v(TAG, "SupportedSize, width: " + size.width + ", height: " + size.height);
            // 先判断比例是否相等
            if (size.width * mPreviewScale == size.height) {
                int delta = Math.abs(mPreviewWidth - size.width);
                if (delta == 0) {
                    return size;
                }
                if (minDelta > delta) {
                    minDelta = delta;
                    index = i;
                }
            }
        }
        return sizes.get(index);
    }

    /**
     * 设置相机显示的方向，必须设置，否则显示的图像方向会错误
     */
    private void setDisplayOrientation() {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Log.e("CHEN", "rotation:" + rotation);

        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (mCameraInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (mCameraInfo.orientation - degrees + 360) % 360;
        }
        Log.e("CHEN:", "当前旋转角为" + result);
        mCamera.setDisplayOrientation(result);
    }

    private void setPictureRotate(int orientation) {
//        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) return;
//        orientation = (orientation + 45) / 90 * 90;
//        int rotation;
//        if (mCameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
//            rotation = (mCameraInfo.orientation - orientation + 360) % 360;
//        } else {  // back-facing camera
//            rotation = (mCameraInfo.orientation + orientation) % 360;
//        }
//        Log.e("CHEN", "mLatestRotation = " + mLatestRotation);
//        mLatestRotation = rotation;
    }

    public int getLatestRotation() {
        return mLatestRotation;
    }

    public void setPreviewCallback(Camera.PreviewCallback previewCallback) {
        mPreviewCallback = previewCallback;
        if (mPreviewBuffer == null) {
            mPreviewBuffer = new byte[mPreviewWidth * mPreviewHeight * 3 / 2];
        }
        mCamera.addCallbackBuffer(mPreviewBuffer);
        mCamera.setPreviewCallback(mPreviewCallback); // 设置预览的回调
    }

    public void takePicture(Camera.PictureCallback pictureCallback) {
        if (mCamera != null) {
            mCamera.takePicture(null, null, pictureCallback);
        }
    }

    public void switchCamera() {
        mCameraId ^= 1; // 先改变摄像头朝向
        releaseCamera();
        openCamera();
    }

    public Camera getCamera() {
        return mCamera;
    }

    public int getPreviewWidth() {
        return mPreviewWidth;
    }

    public int getPreviewHeight() {
        return mPreviewHeight;
    }


    public CameraProxy.CameraPreviewCallback onCameraPreviewCallBack;

    public void setOnCameraPreviewCallBack(CameraProxy.CameraPreviewCallback onCameraPreviewCallBack) {
        this.onCameraPreviewCallBack = onCameraPreviewCallBack;
    }

    public interface CameraPreviewCallback {
        void onPreviewFrame(byte[] bytes, Camera camera);
    }
}
