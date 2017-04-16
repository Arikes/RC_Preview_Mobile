package com.example.testandroid.yjj_demo.tools;

/**
 * Created by apple on 17/3/1.
 */
public class Point3
{

    public float x, y, z;

    public Point3(float xx, float yy, float zz)
    {
        x=xx; y=yy; z=zz;
    }

    public Point3()
    {
        x=y=z=0;
    }

    void set(float dx,float dy,float dz)
    {
        x=dx; y=dy; z=dz;
    }
    void set(Point3 p)
    {
        x=p.x; y=p.y; z=p.z;
    }

    public float[] build4tuple()
    {
        float v[] = null;
        v[0]=x; v[1]=y; v[2]=z; v[3]=1.0f;
        return v;
    }

}
