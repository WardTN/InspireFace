package com.example.inspireface_example.render;

import static com.insightface.inspireface_demo.utils.ListUtilKt.listIsEmp;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.inspireface_example.bean.FaceRectAndLine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class TriangleRender extends AbsObjectRender {

    private FloatBuffer vertexBuffer;
    private int program;
    private float[] mvpMatrix = new float[16];
    private float[] tempMatrix = new float[16];

    // 动态位置和尺寸
    private float positionX = 0f; // 中心点 X
    private float positionY = 0f; // 中心点 Y
    private float width = 0.2f; // 矩形宽度
    private float height = 0.2f; // 矩形高度

    // 顶点数据模板（单位矩形，中心为 (0, 0)）
    private float[] baseVertices = {
            -0.5f, 0.5f, 0.0f,   // 左上
            -0.5f, -0.5f, 0.0f,  // 左下
            0.5f, 0.5f, 0.0f,    // 右上
            0.5f, -0.5f, 0.0f    // 右下
    };

    // 初始化 OpenGL 程序
    String vertexShaderCode = "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}";
    String fragmentShaderCode = "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    @Override
    public void initProgram() {

        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode);
        program = GLES30.glCreateProgram();
        GLES30.glAttachShader(program, vertexShader);
        GLES30.glAttachShader(program, fragmentShader);
        GLES30.glLinkProgram(program);

        // 初始化顶点缓冲
        vertexBuffer = ByteBuffer.allocateDirect(baseVertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        Matrix.setIdentityM(mvpMatrix, 0);
        updateVertexBuffer();
    }


    // 更新顶点缓冲
    private void updateVertexBuffer() {
        vertexBuffer.clear();
        vertexBuffer.put(baseVertices);
        vertexBuffer.position(0);
    }

    // 动态设置矩形位置和尺寸
    public void setRect(float screenWidth,float screenHeight,float x, float y, float width, float height) {
        this.positionX = x;
        this.positionY = y;
        this.width = width;
        this.height = height;

        //将位置 （x,y） 转换为归一化坐标系 【-1,1】
        float normalizedX = (2 * (x / screenWidth)) - 1;;
        float normalizedY = 1 - (2 * (y / screenHeight));;

        // 将矩形的宽度和高度转换为归一化坐标
        float normalizedWidth = 2 * (width / screenWidth);  // 宽度归一化
        float normalizedHeight = 2 * (height / screenHeight); // 高度归一化

        // 更新矩形的位置和尺寸
        this.positionX = normalizedX;
        this.positionY = normalizedY;
        this.width = normalizedWidth;
        this.height = normalizedHeight;

    }


    @Override
    public void onDrawFrame(GL10 gl) {

        GLES30.glUseProgram(program);

        // 更新矩阵：平移 + 缩放
        Matrix.setIdentityM(tempMatrix, 0);
        Matrix.translateM(tempMatrix, 0, positionX, positionY, 0f);
        Matrix.scaleM(tempMatrix, 0, this.width, this.height, 1f);
        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(program, "uMVPMatrix"), 1, false, tempMatrix, 0);

        // 设置顶点数据
        int positionHandle = GLES30.glGetAttribLocation(program, "vPosition");
        GLES30.glEnableVertexAttribArray(positionHandle);
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);

        // 设置颜色
        int colorHandle = GLES30.glGetUniformLocation(program, "vColor");
        GLES30.glUniform4fv(colorHandle, 1, new float[]{0.0f, 0.5f, 1.0f, 1.0f}, 0);

        // 绘制矩形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);

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

    public void setPointFS(FaceRectAndLine faceRectAndLine, int width, int height){

    }



    private ArrayList<Float> addFaceRegionPos(List<List<List<Integer>>> sixRegion, int pos, int width, int height) {
        ArrayList<Float> pointS = new ArrayList();
        String str = "";
        if (!listIsEmp(sixRegion)) {
            List<List<Integer>> pos1 = sixRegion.get(pos - 1);

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
        }
        return pointS;
    }

}