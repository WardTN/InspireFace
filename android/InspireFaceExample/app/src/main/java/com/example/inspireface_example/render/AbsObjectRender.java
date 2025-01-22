package com.example.inspireface_example.render;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 增加摄像头预览添加其他绘制元素的抽象类
 */
public abstract class AbsObjectRender {

    //投影矩阵
    protected float[] projectMatrix = new float[16];
    //相机矩阵
    protected float[] cameraMatrix = new float[16];
    //顶点数组buffer
    private FloatBuffer vertexBuffer;
    //颜色数组buffer
    private FloatBuffer colorBuffer;
    //渲染程序
    public int mProgram = 0;
    //屏幕宽和高
    protected int mWidth = 0;
    protected int mHeight = 0;


    //在onSurfaceCreated中调用,program要在onSurfaceCreated中调用才能成功
    abstract public void initProgram();

    //在onSurfaceChanged中调用，保存投影矩阵和相机矩阵
    public void setProjAndCamMatrix(float[] projectMatrix, float[] cameraMatrix) {
        this.projectMatrix = projectMatrix;
        this.cameraMatrix = cameraMatrix;
    }

    public boolean isAlreadyInited() {
        return !(mProgram == 0);
    }

    abstract public void onDrawFrame(GL10 gl10);

    public void setScreenWidthHeight(int width, int heigt) {
        this.mWidth = width;
        this.mHeight = heigt;
    }

}