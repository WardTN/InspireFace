package com.example.inspireface_example.widget;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class CameraHolderSurfaceView extends SurfaceView {

    private CameraProxy mCameraProxy;

    public CameraHolderSurfaceView(Context context) {
        this(context, null);
    }

    public CameraHolderSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraHolderSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CameraHolderSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        getHolder().addCallback(mSurfaceHolderCallback);
        mCameraProxy = new CameraProxy();
    }

    private final SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mCameraProxy.openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mCameraProxy.startPreview(holder);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mCameraProxy.releaseCamera();
        }
    };


    public OnSurfaceViewListener onSurfaceViewListener;

    public interface OnSurfaceViewListener {
        void setAspectRatio(float xScale, float yScale);
    }

    public void setOnSurfaceViewListener(OnSurfaceViewListener onSurfaceViewListener) {
        this.onSurfaceViewListener = onSurfaceViewListener;
    }

    public CameraProxy getCameraProxy() {
        return mCameraProxy;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      /*  int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }*/
    }


    private static float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }


    /**
     * 打开闪光灯
     */
    private void openFlash() {
        mCameraProxy.openFlash();
    }

    /**
     * 关闭闪光灯
     */
    private void closeFlash() {
        mCameraProxy.closeFlash();
    }

}
