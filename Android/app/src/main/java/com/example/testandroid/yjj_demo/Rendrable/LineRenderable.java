package com.example.testandroid.yjj_demo.Rendrable;

import android.graphics.Color;
import android.opengl.GLES20;


import com.anandmuralidhar.assimpandroid.MyGLSurfaceView;
import com.example.testandroid.yjj_demo.tools.BufferUtil;
import com.example.testandroid.yjj_demo.tools.MatrixState;
import com.example.testandroid.yjj_demo.tools.ShaderUtil;

import java.nio.FloatBuffer;
import java.util.Vector;

import static com.example.testandroid.yjj_demo.tools.Constant.COORDS_PER_VERTEX;

/**
 * Created by Administrator on 2017/4/7.
 */

public class LineRenderable implements Renderable{

    private int mProgram;// 自定义渲染管线着色器程序id
    private int muMVPMatrixHandle;// 总变换矩阵引用
    private int mPositionHandle; // 顶点位置属性引用
    private int lineColorHandle; // 顶点颜色属性引用
    private String mVertexShader;// 顶点着色器代码脚本
    private String mFragmentShader;// 片元着色器代码脚本

    private FloatBuffer vertexBuffer = null;// 顶点坐标数据缓冲
    private int mPointCount = 0;
    private int lineColor;
    private float lineWidth;
    private float[] color =  null;
    private FloatBuffer colorBuffer = null;

    private MyGLSurfaceView mv = null;
    private boolean isStrip = false;
    public LineRenderable(MyGLSurfaceView mv, Vector<Float> points, boolean isStrip){
        initVertexData(points);
        initShader(mv);
        this.isStrip = isStrip;
    }

    public void setLineColor(int color){
        this.lineColor = color;
    }

    public void setLineWidth(float width){
        this.lineWidth = width;
    }

    private void initVertexData(Vector<Float> points){
        mPointCount = points.size();
        float[]  mPointArr = new float[points.size()];
        for(int i=0;i<points.size();i++){
            mPointArr[i]=points.get(i);
        }

        vertexBuffer = BufferUtil.floatToBuffer(mPointArr);
    }

    // 初始化shader
    private void initShader(MyGLSurfaceView mv) {
        // 加载顶点着色器的脚本内容
        mVertexShader = ShaderUtil.loadFromAssetsFile("shaders/vertex_line.sh",
                mv.getResources());
        // 加载片元着色器的脚本内容
        mFragmentShader = ShaderUtil.loadFromAssetsFile("shaders/frag_line.sh",
                mv.getResources());
        // 基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        // 获取程序中顶点位置属性引用id
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //线条颜色
        lineColorHandle = GLES20.glGetUniformLocation(mProgram, "lineColor");
        // 获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

    }

    @Override
    public void render() {
        GLES20.glUseProgram(mProgram);

        GLES20.glUniform4f(lineColorHandle,  Color.red(this.lineColor) / 255.0f, Color.green(this.lineColor) / 255.0f,
                Color.blue(this.lineColor) / 255.0f, Color.alpha(this.lineColor) / 255.0f);

        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        GLES20.glLineWidth(lineWidth);
        if(isStrip){
            GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, mPointCount / 3);
        }else{
            GLES20.glDrawArrays(GLES20.GL_LINES, 0, mPointCount / 3);
        }

        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }


}
