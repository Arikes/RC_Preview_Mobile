package com.example.testandroid.yjj_demo.tools;

/**
 * Created by apple on 17/3/1.
 */
public class Vector3f
{

    public float x, y, z;

    public Vector3f(float xx, float yy, float zz)
    {
        x=xx; y=yy; z=zz;
    }

    public Vector3f(Vector3f v)
    {
        x=v.x; y=v.y; z=v.z;
    }
    Vector3f()
    {
        x=0; y=0; z=0;
    }

    void set(float dx,float dy,float dz)
    {
        x=dx; y=dy; z=dz;
    }
    void set(Vector3f v)
    {
        x=v.x; y=v.y; z=v.z;
    }
    void flip()
    {
        x=-x; y=-y; z=-z;
    }
    void setDiff(Point3 a,Point3 b)
    {
        x=a.x-b.x; y=a.y-b.y; z=a.z-b.z;
    }
    void normalize()
    {
        float base = (float)(Math.pow(x, 2)+ Math.pow(y, 2) + Math.pow(z, 2));
        x = (float)(x/Math.pow(base,0.5));
        y=(float)(y/Math.pow(base, 0.5));
        z=(float)(z/Math.pow(base, 0.5));
    }

    public Vector3f cross(Vector3f b)
    {
        float x1,y1,z1;
        x1 = y * b.z - z * b.y;
        y1 = z * b.x - x * b.z;
        z1 = x * b.y- y * b.x;

        Vector3f c = new Vector3f(x1, y1, z1);

        return c;
    }

    float dot(Vector3f b)
    {
        float d = x * b.x + y * b.y + z * b.z;
        return d;
    }
}
