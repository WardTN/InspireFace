package com.example.inspireface_example.widget;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.inspireface_example.render.CameraManeger;


public class CameraSurfaceView extends SurfaceView {
    private CameraManeger mCameraProxy;

    public CameraSurfaceView(Context context) {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        getHolder().addCallback(mSurfaceHolderCallback);
        mCameraProxy = new CameraManeger(context);
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


    public CameraHolderSurfaceView.OnSurfaceViewListener onSurfaceViewListener;

    public interface OnSurfaceViewListener {
        void setAspectRatio(float xScale, float yScale);
    }

    public void setOnSurfaceViewListener(CameraHolderSurfaceView.OnSurfaceViewListener onSurfaceViewListener) {
        this.onSurfaceViewListener = onSurfaceViewListener;
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
//        mCameraProxy.openFlash();
    }

    /**
     * 关闭闪光灯
     */
    private void closeFlash() {
//        mCameraProxy.closeFlash();
    }


}
