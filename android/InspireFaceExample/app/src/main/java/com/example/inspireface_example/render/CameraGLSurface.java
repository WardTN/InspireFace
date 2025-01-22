package com.example.inspireface_example.render;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;



public class CameraGLSurface extends GLSurfaceView implements SurfaceTexture.OnFrameAvailableListener {

    private CameraQuarRender render;
    private FaceRectRender faceRectRender;
    private FacePointRender facePointRender;

    public CameraGLSurface(Context context) {
        super(context);
        init(context);
    }

    public CameraGLSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        setEGLContextClientVersion(3);
        render = new CameraQuarRender(this);
        setRenderer(render);
        setRenderMode(RENDERMODE_CONTINUOUSLY);

//        initTrain();
        //        initPoint();
    }


    private void initTrain() {
        faceRectRender = new FaceRectRender();
        render.setObjectRender(faceRectRender);
        //将三角形和 CameraRender连接
        render.setTrainRender(faceRectRender);
    }

//    private void initPoint() {
//        facePointRender = new FacePointRender();
//        render.setObjectRender(facePointRender);
//        render.setDotRener(facePointRender);
//    }


//    public void setObjectRender(AbsObjectRender absObjectRender) {
//        if (render != null) {
//            render.setObjectRender(absObjectRender);
//        }
//    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        requestRender();
    }

    public void changePos() {
        if (facePointRender != null) {
            facePointRender.changePos();
        }
    }

    public void chaneAlpha(int mode){
        if (facePointRender != null) {
            facePointRender.changeAlpha(mode);
        }
    }


    public void changeColor(boolean isGreen) {
        if (facePointRender != null) {
            facePointRender.changeColor(isGreen);
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        render.releaseCamera();
    }
}