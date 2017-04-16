package com.example.testandroid.yjj_demo.SceneRenderable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.testandroid.yjj_demo.tools.MResource;

/**
 * Created by Administrator on 2017/4/12.
 */

public class DataPanelView  extends View {

    private int baseX=0;
    private float scaleRate=0.4f;  //美术给图缩放比例

    private Bitmap mFXPBitmap=null;
    private Bitmap mPanelBk = null;
    private Bitmap mYMbmp = null;
    private Bitmap mLHQbmp = null;
    private Bitmap mSCbmp = null;

    private int mScreenWidth = 0;
    private int mScreenHeight = 0;

    private int fontSize = 24;
    private int rectwidth = 15;
    private int rectHeight = 114;

    private int imageYuanShiWitdh = 681;
    private int imageYuanShiHeight = 233;

    private int rectValueWidth = 9;
    private int rectValueHeight = 84;

    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            DataPanelView.this.postInvalidate();
            super.handleMessage(msg);
        }
    };

    public DataPanelView(Context context) {
        super(context);
        //mPanelBk= BitmapFactory.decodeResource(getResources(), R.drawable.bejing3x);
        mPanelBk=BitmapFactory.decodeResource(getResources(), MResource.getIdByName(context, "drawable", "bejing3x"));
        mPanelBk = scaleBitmap(mPanelBk, scaleRate, scaleRate);

        //mFXPBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.fangxiangpan3x);
        mFXPBitmap=BitmapFactory.decodeResource(getResources(), MResource.getIdByName(context, "drawable", "fangxiangpan3x"));
        mFXPBitmap = scaleBitmap(mFXPBitmap,scaleRate,scaleRate);
    }

    public void setScreenWidth(int mScreenWidth) {
        this.mScreenWidth = mScreenWidth;
    }

    public void setScreenHeight(int mScreenHeight) {
        this.mScreenHeight = mScreenHeight;
    }

    private float valueYM = 0.5f;
    public void setYouMenValue(float value){
        this.valueYM = value;
    }

    private float valueLH = 0.5f;
    public void setLiHeValue(float value){
        this.valueLH = value;
    }

    private float valueSC = 0.1f;
    public void setShaCheValue(float value){
        this.valueSC = value;
    }

    private float angleFXP = 0;
    public void SetAngleFangXiangPan(float angle){
        this.angleFXP = angle;
        this.UpdateView();
    }

    public void UpdateView()
    {
        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Message message=new Message();
                message.what=1;
                mHandler.sendMessage(message);
            }
        });
        thread.start();
    }

    private Camera mCamera;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);

        float panelLeft = (this.mScreenWidth - mPanelBk.getWidth()) / 2;
        float panelTop = this.mScreenHeight - mPanelBk.getHeight();
        canvas.drawBitmap(mPanelBk, panelLeft, panelTop, p);

        canvas.save();

        float pointFXP_X = panelLeft + getScreenWidthValue(100);
        float pointFXP_Y = this.mScreenHeight - getScreenHeightValue(26) - mFXPBitmap.getHeight();
        canvas.rotate(angleFXP, pointFXP_X + mFXPBitmap.getWidth() / 2, pointFXP_Y + mFXPBitmap.getHeight() / 2);
        canvas.drawBitmap(mFXPBitmap, pointFXP_X, pointFXP_Y, p);
        canvas.restore();

        //数据背景框
        float pointxYouMen = this.mScreenWidth -  panelLeft -  getScreenWidthValue(100) - getScreenWidthValue(rectwidth);
        float pointyYouMen = this.mScreenHeight - getScreenHeightValue(26);
        drawRectSign(canvas, "油门", Color.argb(180, 180, 180, 180), pointxYouMen, pointyYouMen);
        float pointxLiHe = pointxYouMen - getScreenWidthValue(50 + rectwidth);
        float pointyLiHe = this.mScreenHeight - getScreenHeightValue(26);
        drawRectSign(canvas, "离合", Color.argb(180, 180, 180, 180), pointxLiHe, pointyLiHe);
        float pointxShaChe = pointxLiHe - getScreenWidthValue(50 + rectwidth);
        float pointyShaChe = this.mScreenHeight - getScreenHeightValue(26);
        drawRectSign(canvas, "刹车", Color.argb(180, 180, 180, 180), pointxShaChe, pointyShaChe);

        drawRectYouMenSign(canvas, pointxYouMen, pointyYouMen, valueYM);
        drawLiHeSign(canvas, pointxLiHe, pointyLiHe, valueLH);
        drawShaCheSign(canvas, pointxShaChe, pointyShaChe, valueSC);
    }

    void drawText(Canvas canvas ,String text , float x ,float y,Paint paint ,float angle){
        if(angle != 0){
            canvas.rotate(angle, x, y);
        }
        canvas.drawText(text, x, y, paint);
        if(angle != 0){
            canvas.rotate(-angle, x, y);
        }
    }

    private int fontWidth = 16;
    private int fontHeight = 4;
    private void drawRectSign(Canvas canvas, String text, int colorValue, float x, float y){
        Paint paint = new Paint();
        paint.setColor(colorValue);
        canvas.drawRect(x, y -  getScreenHeightValue(rectHeight + 15 + fontHeight),
                x + getScreenWidthValue(rectwidth), y - getScreenHeightValue(15 + fontHeight), paint);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(fontSize);
        canvas.drawText(text, x - getScreenWidthValue(fontWidth), y, paint);
    }

    private void drawRectYouMenSign(Canvas canvas, float x, float y, float value){
        Paint paint = new Paint();
        paint.setColor(Color.argb(255, 43, 227, 0));
        float pointXS = x + getScreenWidthValue((rectwidth - rectValueWidth) / 2.0f);
        float pointYS = y -  getScreenHeightValue(rectHeight + 15 + fontHeight);
        float pointXE = pointXS + getScreenWidthValue(rectValueWidth);
        float pointYE = y - getScreenHeightValue(15 + fontHeight);
        canvas.drawRect(pointXS, pointYE - (pointYE -pointYS) * value, pointXE, pointYE, paint);
    }

    private void drawLiHeSign(Canvas canvas, float x, float y, float value){
        Paint paint = new Paint();
        paint.setColor(Color.argb(255, 255, 0, 0));
        float pointXS = x + getScreenWidthValue((rectwidth - rectValueWidth) / 2.0f);
        float pointYS = y - getScreenHeightValue(rectHeight + 15 + fontHeight);
        float pointXE = pointXS + getScreenWidthValue(rectValueWidth);
        float pointYE = y - getScreenHeightValue(15 + fontHeight);

        canvas.drawRect(pointXS, pointYE - (pointYE -pointYS) * value, pointXE, pointYE, paint);
    }

    private void drawShaCheSign(Canvas canvas, float x, float y, float value){
        Paint paint = new Paint();
        paint.setColor(Color.argb(255, 255, 216,  0));
        float pointXS = x + getScreenWidthValue((rectwidth - rectValueWidth) / 2.0f);
        float pointYS = y -  getScreenHeightValue(rectHeight + 15 + fontHeight);
        float pointXE =  pointXS + getScreenWidthValue(rectValueWidth);
        float pointYE =  y - getScreenHeightValue(15 + fontHeight);
        canvas.drawRect(pointXS, pointYE - (pointYE -pointYS) * value, pointXE, pointYE, paint);
    }

    private float getScreenWidthValue(float valueIamge){
        float valueR = mPanelBk.getWidth() * (valueIamge / imageYuanShiWitdh);
        return valueR;
    }

    private float getScreenHeightValue(float valueIamge){
        float valueR = mPanelBk.getHeight() * (valueIamge / imageYuanShiHeight);
        return valueR;
    }

    private Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setTranslate(origin.getWidth() / 2, origin.getHeight() / 2);
        matrix.postRotate(alpha);
        //matrix.setRotate(alpha, width / 2.0f, height / 2.0f);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, true);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    private Bitmap scaleBitmap(Bitmap origin,float widRate,float heightRate)
    {
        if (origin == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.setScale(widRate, heightRate);

        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, origin.getWidth(), origin.getHeight(), matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

}
