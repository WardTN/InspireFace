package com.example.inspireface_example.render;

import static com.insightface.inspireface_demo.utils.ListUtilKt.isInListRange;
import static com.insightface.inspireface_demo.utils.ListUtilKt.listIsEmp;
import static javax.microedition.khronos.opengles.GL10.GL_BLEND;
import static javax.microedition.khronos.opengles.GL10.GL_ONE_MINUS_SRC_ALPHA;
import static javax.microedition.khronos.opengles.GL10.GL_SRC_ALPHA;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Log;


import com.example.inspireface_example.R;
import com.example.inspireface_example.bean.FaceRectAndLine;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class FacePointRender extends AbsObjectRender {
    private static final String TAG = "TriangleColorRender";


    ArrayList<Float> pointFList = new ArrayList<>();

    private float colorCoords[] = {0.0f, 1.0f, 0.0f, 1.0f,};

    //顶点数组buffer
    private FloatBuffer vertexBuffer;
    //颜色数组buffer
    private FloatBuffer colorBuffer;

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

    public FacePointRender() {
    }

    private List<Integer> postions = new ArrayList<>();


    @Override
    public void initProgram() {
        //三角形绘制相关初始化
        colorBuffer = GLDataUtil.createFloatBuffer(colorCoords);
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

        pointFList.add(0.1f);
        pointFList.add(0.1f);
        pointFList.add(0f);

        pointFList.add(-0.1f);
        pointFList.add(0.1f);
        pointFList.add(0f);

        pointFList.add(-0.1f);
        pointFList.add(-0.1f);
        pointFList.add(0f);

        pointFList.add(0.1f);
        pointFList.add(-0.1f);
        pointFList.add(0f);

        postions.add(2);
        postions.add(1);
        postions.add(4);
        postions.add(3);
        postions.add(6);
        postions.add(5);
    }

    private final int STATE_UN = 0;
    private final int STATE_LEAVE = 1;
    private final int STATE_CARING = 2;
    private int state = STATE_UN;

    private float ALPHA_1 = 0.2f;
    private float ALPHA_2 = 0.6f;
    private float ALPHA_3 = 0.8f;


    private float curModeAlpha = ALPHA_2;




    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES30.glUseProgram(mProgram);

        GLES30.glEnable(GL_BLEND);
        GLES30.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Matrix.setIdentityM(rotationMatrix, 0); //Matrix 清零

        Matrix.multiplyMM(mTriangleTempMatrix, 0, projectMatrix, 0, cameraMatrix, 0);
        Matrix.translateM(rotationMatrix, 0, distanceX, distanceY, 0); //设置位移
        Matrix.scaleM(rotationMatrix, 0, scaleX, scaleY, 0);
        Matrix.multiplyMM(mvpMatrix, 0, mTriangleTempMatrix, 0, rotationMatrix, 0);

        //左乘矩阵
        int uMaxtrixLocation = GLES30.glGetUniformLocation(mProgram, "vMatrix");

        int colorHandle = GLES30.glGetUniformLocation(mProgram, "uColor");

        if (isCheck) {
            // 绿色
            GLES30.glUniform4f(colorHandle, 0.0f, 1.0f, 0.0f, curModeAlpha);
        } else {
            // 红色
            GLES30.glUniform4f(colorHandle, 1.0f, 0.749f, 0.0f, curModeAlpha);
        }

        // 将前面计算得到的mMVPMatrix(frustumM setLookAtM 通过multiplyMM 相乘得到的矩阵) 传入vMatrix中，与顶点矩阵进行相乘
        GLES30.glUniformMatrix4fv(uMaxtrixLocation, 1, false, mvpMatrix, 0);

        int aPositionLocation = GLES30.glGetAttribLocation(mProgram, "vPosition");

        vertexBuffer = GLDataUtil.createFloatBuffer(pointFList);

        GLES30.glEnableVertexAttribArray(aPositionLocation);
        //x y z 所以数据size 是3
        GLES30.glVertexAttribPointer(aPositionLocation, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);

        int aColorLocation = GLES20.glGetAttribLocation(mProgram, "aColor");
        //准备颜色数据 rgba 所以数据size是 4
        GLES30.glVertexAttribPointer(aColorLocation, 4, GLES30.GL_FLOAT, false, 0, colorBuffer);
        //启用顶点颜色句柄
        GLES30.glEnableVertexAttribArray(aColorLocation);

        GLES30.glLineWidth(5.0f);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, pointFList.size() / 3);

        //禁止顶点数组的句柄
        GLES30.glDisableVertexAttribArray(aPositionLocation);
        GLES30.glDisableVertexAttribArray(aColorLocation);
        GLES30.glUseProgram(0);
    }

    private int curPos = 0;
    private boolean isCheck = false;

    public void changePos() {
        curPos++;
        if (curPos >= 6) {
            curPos = 0;
        }
    }

    public void changeAlpha(int mode) {
        switch (mode){
            case 1:
            case 2: {
                curModeAlpha = ALPHA_2;
                break;
            }
            case 3:{
                curModeAlpha = ALPHA_3;
                break;
            }
        }
    }


    public void changeColor(boolean isGreen) {
        isCheck = isGreen;
    }


    public void setPointFS(FaceRectAndLine faceRectAndLine, int width, int height) {
        pointFList.clear();
        ArrayList<Float> pointS = new ArrayList();
        if (!listIsEmp(faceRectAndLine.getFace_six_region())) {
            List<Float> pos1 = addFaceRegionPos(faceRectAndLine.getFace_six_region(), postions.get(curPos), width, height);
            pointS.addAll(pos1);
        }
        pointFList.addAll(pointS);

    }

    private ArrayList<Float> addFaceRegionPos(List<List<List<Integer>>> sixRegion, int pos, int width, int height) {
        ArrayList<Float> pointS = new ArrayList();
        String str = "";
        if (!listIsEmp(sixRegion)) {
            if (isInListRange(sixRegion, pos - 1)) {
                List<List<Integer>> pos1 = sixRegion.get(pos - 1);

                if (pos <= 2) {
                    for (int i = 2; i < pos1.size(); i++) {
                        int dotX = pos1.get(i).get(0);
                        int dotY = pos1.get(i).get(1);

                        float changeX = -((dotX / (float) width * 2f) - 1f);
                        float changeY = 1f - (dotY / (float) height * 2f);

                        pointS.add(changeX);
                        pointS.add(changeY);
                        str = str + "（X" + changeX + ",Y = " + changeY + ")";
                        pointS.add(0f);
                    }

                    for (int i = 0; i < 2; i++) {
                        int dotX = pos1.get(i).get(0);
                        int dotY = pos1.get(i).get(1);

                        float changeX = -((dotX / (float) width * 2f) - 1f);
                        float changeY = 1f - (dotY / (float) height * 2f);

                        pointS.add(changeX);
                        pointS.add(changeY);
                        str = str + "（X" + changeX + ",Y = " + changeY + ")";
                        pointS.add(0f);
                    }
                } else if (pos <= 4) {
                    for (int i = pos1.size() - 10; i < pos1.size(); i++) {
                        int dotX = pos1.get(i).get(0);
                        int dotY = pos1.get(i).get(1);

                        float changeX = -((dotX / (float) width * 2f) - 1f);
                        float changeY = 1f - (dotY / (float) height * 2f);

                        pointS.add(changeX);
                        pointS.add(changeY);
                        str = str + "（X" + changeX + ",Y = " + changeY + ")";
                        pointS.add(0f);
                    }

                    for (int i = 0; i < pos1.size() - 10; i++) {
                        int dotX = pos1.get(i).get(0);
                        int dotY = pos1.get(i).get(1);

                        float changeX = -((dotX / (float) width * 2f) - 1f);
                        float changeY = 1f - (dotY / (float) height * 2f);

                        pointS.add(changeX);
                        pointS.add(changeY);
                        str = str + "（X" + changeX + ",Y = " + changeY + ")";
                        pointS.add(0f);
                    }
                } else {
                    for (int i = 1; i < pos1.size(); i++) {
                        int dotX = pos1.get(i).get(0);
                        int dotY = pos1.get(i).get(1);

                        float changeX = -((dotX / (float) width * 2f) - 1f);
                        float changeY = 1f - (dotY / (float) height * 2f);

                        pointS.add(changeX);
                        pointS.add(changeY);
                        str = str + "（X" + changeX + ",Y = " + changeY + ")";
                        pointS.add(0f);
                    }

                    for (int i = 0; i < 1; i++) {
                        int dotX = pos1.get(i).get(0);
                        int dotY = pos1.get(i).get(1);
                        float changeX = -((dotX / (float) width * 2f) - 1f);
                        float changeY = 1f - (dotY / (float) height * 2f);
                        pointS.add(changeX);
                        pointS.add(changeY);
                        str = str + "（X" + changeX + ",Y = " + changeY + ")";
                        pointS.add(0f);
                    }
                }
            }
        }
        return pointS;
    }

    public void setEmpFS() {
        pointFList.clear();
    }

    // 位置 1 从2 开始
    // 位置 2 从2 开始
    // 位置 3 从


}