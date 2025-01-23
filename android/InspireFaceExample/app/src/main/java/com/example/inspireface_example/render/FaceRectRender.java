package com.example.inspireface_example.render;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Log;

import com.example.inspireface_example.R;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class FaceRectRender extends AbsObjectRender {

    private static final String TAG = "TriangleColorRender";
    //3个定点，等腰直角

    private float vertexCoords[] = {
            // 第一个三角形
            -0.2f, 0.2f, 0.0f, // 左上
            -0.2f, 0, 0.0f, // 左下
            0f, 0.0f, 0.0f, // 右上

            // 第二个三角形
            0.6f, 0.6f, 0.0f, // 右上
            -0.6f, -0.6f, 0.0f, // 左下
            0.6f, -0.6f, 0.0f  // 右下

    };

    /**
     * 索引数据
     */
    short[] indices = {0, 1, 2, 3, 4, 5};


    private float colorCoords[] = {0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,

            0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f};

    //顶点数组buffer
    private FloatBuffer vertexBuffer;
    //颜色数组buffer
    private FloatBuffer colorBuffer;

    private ShortBuffer indexBuffer;

    //三角形变换临时矩阵
    private final float[] rotationMatrix = new float[16];
    private final float[] mTriangleTempMatrix = new float[16];
    private float[] mvpMatrix = new float[16];

    //旋转角度
    private float angle = 0;
    //位移
    private float distanceX = 0f;
    private float distanceY = 0f;

    //缩放系数
    private float scaleX = 1f;
    private float scaleY = 1f;


    @Override
    public void initProgram() {
        //三角形绘制相关初始化
        vertexBuffer = GLDataUtil.createFloatBuffer(vertexCoords);
        colorBuffer = GLDataUtil.createFloatBuffer(colorCoords);

        /**
         * 创建索引buffer
         */
        indexBuffer = GLDataUtil.createShortBuffer(indices);

        //编译顶点着色程序
        String verTriShaderStr = ResReadUtils.readResource(R.raw.vertex_base_matrix_shader);
        int verTriShaderId = ShaderUtils.compileVertexShader(verTriShaderStr);
        //编译片段着色程序
        String fragTriShaderStr = ResReadUtils.readResource(R.raw.fragment_base_common_shader);
        int fragTriShaderId = ShaderUtils.compileFragmentShader(fragTriShaderStr);
        //连接程序
        mProgram = ShaderUtils.linkProgram(verTriShaderId, fragTriShaderId);
        if (mProgram == 0) {
            Log.e(TAG, "initProgram: 初始化失败");
        } else {
            Log.e(TAG, "initProgram: 初始化成功" + mProgram);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES30.glUseProgram(mProgram);
        Matrix.setIdentityM(rotationMatrix, 0); //Matrix 清零

        Matrix.multiplyMM(mTriangleTempMatrix, 0, projectMatrix, 0, cameraMatrix, 0);
        Matrix.translateM(rotationMatrix, 0, distanceX, distanceY, 0); //设置位移
        Matrix.scaleM(rotationMatrix, 0, scaleX, scaleY, 0);
        Matrix.multiplyMM(mvpMatrix, 0, mTriangleTempMatrix, 0, rotationMatrix, 0);

        //左乘矩阵
        int uMaxtrixLocation = GLES30.glGetUniformLocation(mProgram, "vMatrix");
        // 将前面计算得到的mMVPMatrix(frustumM setLookAtM 通过multiplyMM 相乘得到的矩阵) 传入vMatrix中，与顶点矩阵进行相乘
        GLES30.glUniformMatrix4fv(uMaxtrixLocation, 1, false, mvpMatrix, 0);

        int aPositionLocation = GLES30.glGetAttribLocation(mProgram, "rectPostion");
        GLES30.glEnableVertexAttribArray(aPositionLocation);
        //x y z 所以数据size 是3
        GLES30.glVertexAttribPointer(aPositionLocation, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);

        int aColorLocation = GLES20.glGetAttribLocation(mProgram, "aColor");
        //准备颜色数据 rgba 所以数据size是 4
        GLES30.glVertexAttribPointer(aColorLocation, 4, GLES30.GL_FLOAT, false, 0, colorBuffer);
        //启用顶点颜色句柄
        GLES30.glEnableVertexAttribArray(aColorLocation);

        GLES30.glLineWidth(5.0f);
//        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCoords.length / 3);
//        GLES30.glDrawElements(GLES30.GL_TRIANGLES, indices.length, GLES30.GL_UNSIGNED_SHORT, indexBuffer);

        //禁止顶点数组的句柄
//        GLES30.glDisableVertexAttribArray(aPositionLocation);
//        GLES30.glDisableVertexAttribArray(aColorLocation);
//        GLES30.glUseProgram(mProgram);
    }

    /**
     * @param newPosX      中心点X
     * @param newPosY      中心点Y
     * @param widthRation
     * @param heightRation
     */
    public void setNewPos(float newPosX, float newPosY, float widthRation, float heightRation) {
        //得到当前矩阵需要放大的倍数
        scaleX = widthRation / 0.1f; //原始宽为 0.1f
        scaleY = heightRation / 0.1f; //原始高为 0.1f

        distanceX = (newPosX * 2f) - 1f;
        distanceY = 1f - (newPosY * 2f);
    }
}