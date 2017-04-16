package com.example.testandroid.yjj_demo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.anandmuralidhar.assimpandroid.AssimpActivity;
import com.example.testandroid.yjj_demo.tools.DataProcessUtil;
import com.example.testandroid.yjj_demo.tools.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button mBtnTest1;

    private Button mBtnTest2,mBtnTest3,mBtnTest4,mBtnTest5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnTest1 = (Button)findViewById(R.id.btnTest);
        mBtnTest1.setOnClickListener(this);

        mBtnTest2 = (Button)findViewById(R.id.btnTest2);
        mBtnTest2.setOnClickListener(this);

        mBtnTest3 = (Button)findViewById(R.id.btnTest3);
        mBtnTest3.setOnClickListener(this);

        mBtnTest4 = (Button)findViewById(R.id.btnTest4);
        mBtnTest4.setOnClickListener(this);

        mBtnTest5 = (Button)findViewById(R.id.btnTest5);
        mBtnTest5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //String routeName = "paths/pathData.json";
        String routeName = "paths/leftDK.json";
//        switch(v.getId()){
//            case R.id.btnTest:
//                routeName = "paths/20170316104735.xml";
//                break;
//            case R.id.btnTest2:
//                routeName = "paths/ParallParking.xml";
//                break;
//            case R.id.btnTest3:
//                routeName = "paths/ReserveParking.xml";
//                break;
//            case R.id.btnTest4:
//                routeName = "paths/CurveDriving.xml";
//                break;
//            case R.id.btnTest5:
//                routeName = "paths/SlopeParking.xml";
//                break;
//        }

        InputStream mapStream = null,pathStream = null;
        try {
            mapStream = MainActivity.this.getAssets().open("mapData.json");

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            pathStream = MainActivity.this.getAssets().open(routeName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String mapStr = null;
        String pathStr = null;
        try {
            mapStr = StringUtil.inputStream2String(mapStream);
            pathStr = StringUtil.inputStream2String(pathStream);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        try {
//            PreviewActivity.mMapManager =new MapManager(MainActivity.this,mapStream,pathStream);
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }

        if(mapStr != null && pathStr != null){
            AssimpActivity.mapData = DataProcessUtil.getMapDataFromString(mapStr);
            AssimpActivity. pathData = DataProcessUtil.getPathDataFromString(pathStr);
        }

        Intent intent = new Intent(MainActivity.this,AssimpActivity.class);
        startActivity(intent);

    }
}
