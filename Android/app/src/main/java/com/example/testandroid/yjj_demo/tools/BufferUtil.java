package com.example.testandroid.yjj_demo.tools;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class BufferUtil {
    public static FloatBuffer mBuffer;
    public static FloatBuffer floatToBuffer(float[] a){
        //先初始化buffer，数组的长度*4，因为一个float占4个字节
        ByteBuffer mbb = ByteBuffer.allocateDirect(a.length*4);
        //数组排序用nativeOrder
        mbb.order(ByteOrder.nativeOrder());
        mBuffer = mbb.asFloatBuffer();
        mBuffer.put(a);
        mBuffer.position(0);
        return mBuffer;
    }

    public static IntBuffer intToBuffer(int[] a){

        IntBuffer intBuffer;
        //先初始化buffer，数组的长度*4，因为一个float占4个字节
        ByteBuffer mbb = ByteBuffer.allocateDirect(a.length*4);
        //数组排序用nativeOrder
        mbb.order(ByteOrder.nativeOrder());
        intBuffer = mbb.asIntBuffer();
        intBuffer.put(a);
        intBuffer.position(0);
        return intBuffer;
    }

    public static ShortBuffer shortToBuffer(short[] a){
        ShortBuffer shortBuffer;
        ByteBuffer ibb = ByteBuffer.allocateDirect(a.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        shortBuffer = ibb.asShortBuffer();
        shortBuffer.put(a);
        shortBuffer.position(0);
        return shortBuffer;
    }

    public static ByteBuffer shortToByteBuffer(short[] a){
        ByteBuffer byteBuffer;

        byte indices[] = new byte[a.length];
        for(int i=0; i<a.length; i++){
            indices[i] = (byte) (a[i]);
        }

        // 创建三角形构造索引数据缓冲
        byteBuffer = ByteBuffer.allocateDirect(indices.length);
        byteBuffer.put(indices);// 向缓冲区中放入三角形构造索引数据
        byteBuffer.position(0);// 设置缓冲区起始位置

        return byteBuffer;
    }
}
