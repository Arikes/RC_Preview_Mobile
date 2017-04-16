/*
 *    Copyright 2016 Anand Muralidhar
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.anandmuralidhar.assimpandroid;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.testandroid.yjj_demo.DataModels.PointNode;
import com.example.testandroid.yjj_demo.SceneRenderable.SceneRenderable;
import com.example.testandroid.yjj_demo.testQuad.SimpleQuad;
import com.example.testandroid.yjj_demo.tools.Camera;
import com.example.testandroid.yjj_demo.tools.MatrixState;
import com.example.testandroid.yjj_demo.tools.Vector3f;

import java.util.Timer;
import java.util.TimerTask;

public class MyGLSurfaceView extends GLSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;//角度缩放比例
    //JNI函数
    private native void DrawFrameNative(float[] matrix);
    private native void SurfaceCreatedNative();

    private boolean mModelCreated = false;
    Timer timer = new Timer();

    private SceneRenderer mRenderer;

    private SceneRenderable scene = null;

    private int mOperaMode = 0;  //0,俯视-默认;1,环绕;2,漫游
    private float mPreviousY;
    private float mPreviousX;
    private float mCameraHeight = 7;
    private float mCameraX, mCameraZ;
    private Vector3f mCameraCenter;

    private int texIndex = 0;

    private int curPathIndex = 0;

    public MyGLSurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        timer.schedule(task, 20, 20);
    }

    public void setOperaode(int modeID) {
        mOperaMode = modeID;
        Log.d("NSSay:", String.valueOf(modeID));
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (mModelCreated) {
                if (curPathIndex < AssimpActivity.pathData.Points.size() - 1)
                    curPathIndex++;
                else
                    curPathIndex = 0;
            }
        }
    };


    private float rotateX = 0;
    private float rotateY = 0;
    public boolean onTouchEvent(MotionEvent e) {

        float y = e.getY();
        float x = e.getX();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (mOperaMode == 0) {
                    float dy = y - mPreviousY;
                    if (mCameraHeight > 4 + dy / 4.0f)
                        mCameraHeight -= dy / 4.0f;
                    float dx = x - mPreviousX;

                    MatrixState.setCamera(
                            mCameraX, mCameraHeight, mCameraZ,
                            mCameraX, 0f, mCameraZ,
                            0f, 0.0f, 1.0f);

                } else if(mOperaMode == 1){
                    float dy = y - mPreviousY;
                    float dx = x - mPreviousX;

                    rotateX = dx * TOUCH_SCALE_FACTOR;//设置沿x轴旋转角度
                    rotateY = dy * TOUCH_SCALE_FACTOR;

                    rotateCameraAround(0, rotateX, 0);

                }else if (mOperaMode == 2) {
                    float dy = y - mPreviousY;
                    float dx = x - mPreviousX;

                    if (dy > 0)
                        mCameraZ += 0.01f;
                    else
                        mCameraZ -= 0.01f;

                    if (dx > 0)
                        mCameraX -= 0.01f;
                    else
                        mCameraX += 0.01f;

                    MatrixState.setCamera(
                            mCameraX, mCameraHeight, mCameraZ,
                            mCameraX, 0f, mCameraZ,
                            0f, 0.0f, 1.0f);
                }

        }

        mPreviousY = y;
        mPreviousX = x;

//        MatrixState.setCamera(
//                mCameraX, mCameraHeight, mCameraZ,
//                mCameraX, 0f, mCameraZ,
//                0f, 0.0f, 1.0f);
        return true;
    }

    private void setCameraByTwoPoint(float cameraX, float cameraY, float cameraZ,
                                     float targetX, float targetY, float targetZ){
        Vector3f cam = new Vector3f(cameraX, cameraY, cameraZ);
        Vector3f target = new Vector3f(targetX, targetY, targetZ);
        Vector3f up = cam.cross(target);
        MatrixState.setCamera(cameraX, cameraY, cameraZ, targetX, targetY, targetZ, up.x, up.y, up.z);
    }

    private void rotateCameraAround(float rotateX, float rotateY, float rotateZ){
        PointNode pathData = AssimpActivity.pathData.Points.get(0);

        float[] matrix = MatrixState.getOriginalMatrix();
       // float[] matrixCamera = MatrixState.getCaMatrix();
        float[] matrixCur  = MatrixState.getMMatrix();

        Matrix.rotateM(matrix, 0, rotateY, 0, 1 ,0);
//        Matrix.translateM(matrix, 0, mCameraX, 0, 0);
        //Matrix.rotateM(matrix, 0, rotateY, mCameraX, 1f, mCameraZ);

        float[] matrixCameraResult = new float[16];
        Matrix.multiplyMM(matrixCameraResult, 0, matrixCur, 0, matrix, 0);

        MatrixState.setMMMatrix(matrixCameraResult);
       //MatrixState.translate(pathData.x / 100, 0.0f, pathData.y / 100);
        //Matrix.multiplyMM(matrixCameraResult, 0, matrixCamera, 0, matrixCur, 0);
        //Matrix.multiplyMM(matrixCameraResult, 0, matrixCameraResult, 0, matrix, 0);
        //MatrixState.setCamera(mCameraX, mCameraHeight, mCameraZ, pathData.x, pathData.y, pathData.z, 0f, 0.0f, 1.0f);
        //MatrixState.setCaMatrix(matrixCameraResult);
    }

    private class SceneRenderer implements GLSurfaceView.Renderer {
        private SimpleQuad quad;

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            Log.e("set", "44444444444");
            //设置屏幕背景色RGBA
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

            //打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);

            //初始化系统分配的拉伸纹理id
//            quad = new SimpleQuad(MyGLSurfaceView.this);
//            texIndex= TextureUtils.initTexture(getResources(), R.drawable.grass);

            //关闭背面剪裁
            GLES20.glDisable(GLES20.GL_CULL_FACE);

            //场景创建
            scene = new SceneRenderable(MyGLSurfaceView.this);

            //汽车模型导入
            if (!mModelCreated) {
                //jni调用C++库绘制汽车
                SurfaceCreatedNative();
                mModelCreated = true;
            }

            //初始化变换矩阵
            MatrixState.setInitStack();

            mCameraCenter = scene.getCenterByName(AssimpActivity.pathData.AreaID);
            mCameraZ = mCameraCenter.z;
            mCameraZ = mCameraCenter.z;
            mCameraX = mCameraCenter.x;

        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int width, int height) {
//            //设置视窗大小及位置
//            GLES20.glViewport(0, 0, width, height);
//            //计算GLSurfaceView的宽高比
//            float ratio = (float) width / height;
//            //调用此方法计算产生透视投影矩阵
//            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 10);
//            //调用此方法产生摄像机9参数位置矩阵
//            MatrixState.setCamera(0,0,8f,0f,0f,3f,0f,1.0f,0.0f);

            GLES20.glViewport(0, 0, width, height);

            float ratio = (float) width / height;
            MatrixState.setProjectFrustum(ratio * 0.1f, -ratio * 0.1f, -1 * 0.1f, 1 * 0.1f, 4f, 1000);

            MatrixState.setCamera(
                    mCameraCenter.x, mCameraHeight, mCameraCenter.z,
                    mCameraCenter.x, 0f, mCameraCenter.z,
                    0f, 0.0f, 1.0f);
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            //清除深度缓冲与颜色缓冲
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

            if (mModelCreated) {
                MatrixState.pushMatrix();
                PointNode pathData = AssimpActivity.pathData.Points.get(curPathIndex);
                float rotateAngle = -pathData.CarDir;

                AssimpActivity.mPanelView.SetAngleFangXiangPan(rotateAngle);
                AssimpActivity.mPanelView.setYouMenValue(pathData.Accelerat);
                AssimpActivity.mPanelView.setShaCheValue(1-pathData.Brake);
                AssimpActivity.mPanelView.setLiHeValue(pathData.Clutch);
                AssimpActivity.mPanelView.UpdateView();

                MatrixState.translate(pathData.x / 100, 0.0f, pathData.y / 100);
                MatrixState.scale(0.01f, 0.01f, 0.01f);
                MatrixState.rotate(rotateAngle + 180.0f, 0.0f, 1.0f, 0.0f);
                float[] matrix = MatrixState.getFinalMatrix();

                DrawFrameNative(matrix);
                MatrixState.popMatrix();
            }

            MatrixState.pushMatrix();
            scene.render();
            MatrixState.popMatrix();

//            MatrixState.pushMatrix();
//            MatrixState.translate(0, 0, 0);
//            quad.drawSelf(texIndex);
//            MatrixState.popMatrix();
        }
    }

    static {
        System.loadLibrary("ModelAssimpNative");
    }

}
