precision mediump float;
uniform vec4 lineColor;    //顶点颜色
varying vec3 vPosition;//接收从顶点着色器过来的顶点位置
void main() {
   gl_FragColor = lineColor;//给此片元颜色值
}