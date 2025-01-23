#version 300 es
precision mediump float;
in vec4 vColor;
out vec4 fragColor;

// 定义一个 uniform 变量来传递颜色值
uniform vec4 uColor;

void main() {
    fragColor = uColor;
}