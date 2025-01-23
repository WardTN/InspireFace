package com.example.inspireface_example.render;

import static com.insightface.inspireface_demo.utils.ListUtilKt.listIsEmp;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.example.inspireface_example.bean.FaceRectAndLine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

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

    // 矩形的顶点坐标
    ArrayList<Float> pointFList = new ArrayList<>();


    // 索引数据
    private final short[] drawOrder = {
            0, 1, 2, // 第一个三角形
            0, 2, 3  // 第二个三角形
    };

    // 颜色 (RGBA)
    private final float[] color = {0.0f, 1.0f, 0.0f, 1.0f}; // 绿色

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    private int positionHandle;
    private int colorHandle;

    @Override
    public void initProgram() {

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


        // 初始化顶点字节缓冲
//        ByteBuffer bb = ByteBuffer.allocateDirect(rectangleCoords.length * 4);
//        bb.order(ByteOrder.nativeOrder());
//        vertexBuffer = bb.asFloatBuffer();
//        vertexBuffer.put(rectangleCoords);
//        vertexBuffer.position(0);


        // 初始化索引字节缓冲
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

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
        // 使用编译好的程序
        GLES30.glUseProgram(mProgram);

        // 获取位置句柄
        positionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");

        // 启用顶点属性
        GLES30.glEnableVertexAttribArray(positionHandle);

        vertexBuffer = GLDataUtil.createFloatBuffer(pointFList);

        // 设置顶点数据
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);

        // 获取颜色句柄
        colorHandle = GLES30.glGetUniformLocation(mProgram, "vColor");

        // 设置颜色
        GLES30.glUniform4fv(colorHandle, 1, color, 0);

        // 绘制矩形
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, drawOrder.length, GLES30.GL_UNSIGNED_SHORT, drawListBuffer);

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
        ArrayList<Float> pointS = new ArrayList();
        if (!listIsEmp(faceRectAndLine.getFace_six_region())) {
            List<Float> pos1 = addFaceRegionPos(faceRectAndLine.getFace_six_region(), 1, width, height);
            pointS.addAll(pos1);
        }
        pointFList.clear();
        pointFList.addAll(pointS);
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