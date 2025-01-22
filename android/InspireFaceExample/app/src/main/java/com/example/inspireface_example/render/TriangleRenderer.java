package com.example.inspireface_example.render;


import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleRenderer implements GLSurfaceView.Renderer {

    //    private Triangle mTriangle;
//    private Square mSquare;
    private CustomShape shape;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

//        // Initialize a triangle
//        mTriangle = new Triangle();

        // 初始化一个四边形
//        mSquare = new Square();
        shape = new CustomShape();

        // Enable blending
        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        // Draw triangle
//        mTriangle.draw();
        shape.draw();
//        mTriangle2.draw()
//

//        mSquare.draw();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }
}

class Triangle {

    private final String vertexShaderCode = "attribute vec4 vPosition;" + "void main() {" + "  gl_Position = vPosition;" + "}";

    private final String fragmentShaderCode = "precision mediump float;" + "uniform vec4 vColor;" + "void main() {" + "  gl_FragColor = vColor;" + "}";

    private final FloatBuffer vertexBuffer;
    private final int mProgram;

    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {   // in counterclockwise order:
            0.5574074f, 0.050000012f, 0.0f,
            0.5462963f, 0.08703703f, 0.0f,
            0.5277778f, 0.1259259f, 0.0f,
            0.5074074f, 0.15185183f, 0.0f,
            0.4814815f, 0.17407405f, 0.0f,
            0.45f, 0.19074076f, 0.0f,
            0.41111112f, 0.20185184f, 0.0f,
            0.3703704f, 0.20555556f, 0.0f,
            0.39074075f, 0.09629631f, 0.0f,
            0.4222222f, 0.09629631f, 0.0f,
            0.4537037f, 0.09444445f, 0.0f,
            0.4814815f, 0.08888888f, 0.0f,
            0.50555557f, 0.0796296f, 0.0f,
            0.5425926f, 0.053703725f, 0.0f,
            0.5555556f, 0.037037015f, 0.0f

    };

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 0.5f};

    public Triangle() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES30.glCreateProgram();
        GLES30.glAttachShader(mProgram, vertexShader);
        GLES30.glAttachShader(mProgram, fragmentShader);
        GLES30.glLinkProgram(mProgram);
    }

    public void draw() {
        GLES30.glUseProgram(mProgram);

        int positionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");
        GLES30.glEnableVertexAttribArray(positionHandle);
        GLES30.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES30.GL_FLOAT, false, vertexStride, vertexBuffer);

        int colorHandle = GLES30.glGetUniformLocation(mProgram, "vColor");
        GLES30.glUniform4fv(colorHandle, 1, color, 0);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, vertexCount);

        GLES30.glDisableVertexAttribArray(positionHandle);
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES30.glCreateShader(type);
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);
        return shader;
    }
}

class Square {

    private final String vertexShaderCode = "attribute vec4 vPosition;" + "void main() {" + "  gl_Position = vPosition;" + "}";

    private final String fragmentShaderCode = "precision mediump float;" + "uniform vec4 vColor;" + "void main() {" + "  gl_FragColor = vColor;" + "}";

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;

    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            -0.5f, 0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f, 0.5f, 0.0f    // top right
    };

    private final short drawOrder[] = {0, 1, 2, 0, 2, 3}; // 绘制顺序
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 0.5f};

    public Square() {
        // 初始化顶点字节缓冲区用于形状的坐标
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // 初始化绘制顺序字节缓冲区
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES30.glCreateProgram();
        GLES30.glAttachShader(mProgram, vertexShader);
        GLES30.glAttachShader(mProgram, fragmentShader);
        GLES30.glLinkProgram(mProgram);
    }

    public void draw() {
        GLES30.glUseProgram(mProgram);

        int positionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");
        GLES30.glEnableVertexAttribArray(positionHandle);
        GLES30.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES30.GL_FLOAT, false, vertexStride, vertexBuffer);

        int colorHandle = GLES30.glGetUniformLocation(mProgram, "vColor");
        GLES30.glUniform4fv(colorHandle, 1, color, 0);

        GLES30.glDrawElements(GLES30.GL_TRIANGLES, drawOrder.length, GLES30.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES30.glDisableVertexAttribArray(positionHandle);
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES30.glCreateShader(type);
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);
        return shader;
    }
}

class CustomShape {

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;

    static float triangleCoords[] = {

            0.19259262f, -0.4055556f, 0.0f,
            0.2537037f, -0.35740745f, 0.0f,
            0.27407408f, -0.38888884f, 0.0f,
            0.3f, -0.42407405f, 0.0f,
            0.34074074f, -0.44444442f, 0.0f,
            0.3888889f, -0.45740736f, 0.0f,
            0.35185188f, -0.54444444f, 0.0f,
            0.3111111f, -0.537037f, 0.0f,
            0.25f, -0.51851857f, 0.0f,
            0.21296299f, -0.5037037f, 0.0f,
            0.17592591f, -0.48703706f, 0.0f,
            0.17777777f, -0.4666667f, 0.0f,

            0.14444447f, -0.4388889f, 0.0f,

    };

    private final short drawOrder[];

    public CustomShape() {
        // 计算绘制顺序
        drawOrder = new short[(triangleCoords.length / 3 - 2) * 3];
        for (int i = 0; i < drawOrder.length / 3; i++) {
            drawOrder[i * 3] = (short) 0;
            drawOrder[i * 3 + 1] = (short) (i + 1);
            drawOrder[i * 3 + 2] = (short) (i + 2);
        }

        // 初始化顶点字节缓冲区用于形状的坐标
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        // 初始化绘制顺序字节缓冲区
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES30.glCreateProgram();
        GLES30.glAttachShader(mProgram, vertexShader);
        GLES30.glAttachShader(mProgram, fragmentShader);
        GLES30.glLinkProgram(mProgram);
    }

    public void draw() {
        GLES30.glUseProgram(mProgram);

        int positionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");
        GLES30.glEnableVertexAttribArray(positionHandle);
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, vertexBuffer);

        int colorHandle = GLES30.glGetUniformLocation(mProgram, "vColor");
        GLES30.glUniform4fv(colorHandle, 1, new float[]{0.63671875f, 0.76953125f, 0.22265625f, 0.5f}, 0);

        GLES30.glDrawElements(GLES30.GL_TRIANGLE_FAN, drawOrder.length, GLES30.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES30.glDisableVertexAttribArray(positionHandle);
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES30.glCreateShader(type);
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);
        return shader;
    }
}

