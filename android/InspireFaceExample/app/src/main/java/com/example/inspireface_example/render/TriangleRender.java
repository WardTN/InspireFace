package com.example.inspireface_example.render;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class TriangleRender extends AbsObjectRender {

    private static final String VERTEX_SHADER_CODE =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    // 三角形的顶点坐标
    private final float[] triangleCoords = {
            0.0f,  0.5f, 0.0f, // 顶点
            -0.5f, -0.5f, 0.0f, // 左下角
            0.5f, -0.5f, 0.0f  // 右下角
    };

    // 颜色 (RGBA)
    private final float[] color = {0.0f, 1.0f, 0.0f, 1.0f}; // 绿色

    private FloatBuffer vertexBuffer;

    private int positionHandle;
    private int colorHandle;

    @Override
    public void initProgram() {
        // 初始化顶点字节缓冲
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        // 加载着色器并创建程序
        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, VERTEX_SHADER_CODE);
        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE);

        mProgram = GLES30.glCreateProgram();
        GLES30.glAttachShader(mProgram, vertexShader);
        GLES30.glAttachShader(mProgram, fragmentShader);
        GLES30.glLinkProgram(mProgram);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
//        // 清除颜色缓冲区
//        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        // 使用编译好的程序
        GLES30.glUseProgram(mProgram);

        // 获取位置句柄
        positionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");

        // 启用顶点属性
        GLES30.glEnableVertexAttribArray(positionHandle);

        // 设置顶点数据
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);

        // 获取颜色句柄
        colorHandle = GLES30.glGetUniformLocation(mProgram, "vColor");

        // 设置颜色
        GLES30.glUniform4fv(colorHandle, 1, color, 0);

        // 绘制三角形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);

        // 禁用顶点数组
        GLES30.glDisableVertexAttribArray(positionHandle);
    }

    // 加载着色器方法
    private int loadShader(int type, String shaderCode) {
        int shader = GLES30.glCreateShader(type);
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);
        return shader;
    }
}