package com.example.testandroid.yjj_demo.tools;

import javax.microedition.khronos.opengles.GL10;
/**
 * Created by apple on 17/3/1.
 */
public class Camera {

//    Point3         eye,look,up;
//    float          viewAngle, aspect, nearDist, farDist;
//    Vector3 u,v,n;

    Vector3f m_pos;
    Vector3f m_target;
    Vector3f m_up;
    Vector3f u,v,n;

    public Camera(){
    }

    public Camera(Vector3f pos, Vector3f target, Vector3f up)
    {
        m_pos = pos;
        m_target = target;
        m_up = up;
        n = new Vector3f( pos.x -target.x, pos.y-target.y, pos.z-target.z);
        u = new Vector3f(up.cross(n).x, up.cross(n).y, up.cross(n).z);
        v = new Vector3f(n.cross(u).x, n.cross(u).y, n.cross(u).z);


        n.normalize();
        u.normalize();
        v.normalize();

        setModelViewMatrix();
    }

    public void setModelViewMatrix()
    {
        float []m = new float[16];

        m[0]=u.x; m[4]=u.y; m[8]=u.z; m[12]=-m_pos.dot(u);
        m[1]=v.x; m[5]=v.y; m[9]=v.z; m[13]=-m_pos.dot(v);
        m[2]=n.x; m[6]=n.y; m[10]=n.z; m[14]=-m_pos.dot(n);
        m[3]=0;  m[7]=0;  m[11]=0;  m[15]=1.0f;

//        for(int i = 0; i < m.length; i++){
//            mValue.put(i, m[i]);
//        }
//
//
//        gl.glMatrixMode(gl.GL_MODELVIEW);
//        gl.glLoadMatrixf(mValue);     //用M矩阵替换原视点矩阵

//        FloatBuffer mValue;//顶点纹理数据缓冲
//        ByteBuffer tbb = ByteBuffer.allocateDirect(m.length*4);
//        tbb.order(ByteOrder.nativeOrder());//设置字节顺序
//        mValue = tbb.asFloatBuffer();//转换为int型缓冲
//        mValue.put(m);//向缓冲区中放入顶点着色数据
//        mValue.position(0);//设置缓冲区起始位置
//
//        gl.glMatrixMode(gl.GL_MODELVIEW);
//        gl.glLoadMatrixf(mValue);     //用M矩阵替换原视点矩阵

        MatrixState.setCamera(m, m_pos.x, m_pos.y, m_pos.z, m_target.x, m_target.y, m_target.z, m_up.x, m_up.y, m_up.z);

    }

    /* 设置摄像机的位置,朝向和向上向量 */
//    public void setCamera( float eyeX, float eyeY, float eyeZ,
//                            float lookX, float lookY, float lookZ,
//                            float upX, float upY, float upZ)
//    {
//
//        eye = new Point3(eyeX, eyeY, eyeZ);
//        look = new Point3(lookX, lookY, lookZ);
//        up = new Point3(upX, upY, upZ);
//        Vector3 upvec = new Vector3(up.x-eye.x,up.y-eye.y,up.z-eye.z);
//
//        /* 计算n、u、v并归一化*/
//        n = new Vector3(eye.x-look.x, eye.y-look.y, eye.z-look.z);
//        u = new Vector3(upvec.cross(n).x,upvec.cross(n).y,upvec.cross(n).z);
//        v = new Vector3(n.cross(u).x,n.cross(u).y,n.cross(u).z);
//
//        u.normalize();
//        v.normalize();
//        n.normalize();
//        setModelViewMatrix();
//
//    }

    public float getDist()
    {
        float dist= (float)(Math.pow(m_pos.x,2) + Math.pow(m_pos.y,2) + Math.pow(m_pos.z,2));
        return (float)Math.pow(dist, 0.5);
    }


    public void roll(float angle)

    {
        float cs=(float)Math.cos(angle*3.14159265/180);
        float sn=(float)Math.sin(angle * 3.14159265/180);
        Vector3f t = new Vector3f(u);
        Vector3f s = new Vector3f(v);
        u.set(cs*t.x-sn*s.x, cs*t.y-sn*s.y, cs*t.z-sn*s.z);
        v.set(sn*t.x+cs*s.x, sn*t.y+cs*s.y, sn*t.z+cs*s.z);
        setModelViewMatrix();          //每次计算完坐标轴变化后调用此函数更新视点矩阵
    }

    public void yaw(float angle)
    {
        float cs=(float)Math.cos(angle*3.14159265/180);
        float sn=(float)Math.sin(angle * 3.14159265 / 180);
        Vector3f t = new Vector3f(n);
        Vector3f s = new Vector3f(u);
        n.set(cs*t.x-sn*s.x, cs*t.y-sn*s.y, cs*t.z-sn*s.z);
        u.set(sn*t.x+cs*s.x, sn*t.y+cs*s.y, sn*t.z+cs*s.z);
        setModelViewMatrix();
    }

    public void pitch(float angle)

    {
        float cs=(float)Math.cos(angle*3.14159265/180);
        float sn=(float)Math.sin(angle*3.14159265/180);
        Vector3f t = new Vector3f(v);
        Vector3f s = new Vector3f(n);
        v.set(cs*t.x-sn*s.x, cs*t.y-sn*s.y, cs*t.z-sn*s.z);
        n.set(sn*t.x+cs*s.x, sn*t.y+cs*s.y, sn*t.z+cs*s.z);
        setModelViewMatrix();
    }
    /* 摄像机绕三个轴平移的计算函数*/
    public void slide(float du, float dv, float dn)
    {
        m_pos.x+=du*u.x+dv*v.x+dn*n.x;
        m_pos.y+=du*u.y+dv*v.y+dn*n.y;
        m_pos.z+=du*u.z+dv*v.z+dn*n.z;
        m_target.x+=du*u.x+dv*v.x+dn*n.x;
        m_target.y+=du*u.y+dv*v.y+dn*n.y;
        m_target.z+=du*u.z+dv*v.z+dn*n.z;
        setModelViewMatrix();
    }
    /* 摄像机初始化*/
    public void setShape(float left, float right,  float bottom, float top, float nearPlane, float farPlane)
    {
//        gl.glMatrixMode(gl.GL_PROJECTION);
//        gl.glLoadIdentity();                                   //设置当前矩阵模式为投影矩阵并归一化
//        gl.glFrustumf(left, right, bottom, top, nearPlane, farPlane);   //对投影矩阵进行透视变换


        MatrixState.setProjectFrustum(left, right, bottom, top, nearPlane, farPlane);
    }
}
