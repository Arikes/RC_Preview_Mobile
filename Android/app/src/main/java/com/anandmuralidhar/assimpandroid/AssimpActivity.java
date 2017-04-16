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

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.example.testandroid.yjj_demo.DataModels.MapData;
import com.example.testandroid.yjj_demo.DataModels.PathData;
import com.example.testandroid.yjj_demo.SceneRenderable.DataPanelView;
import com.example.testandroid.yjj_demo.tools.MResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssimpActivity extends Activity{
    private static final int FLAG = 0x01;

    static public MapData mapData = null;
    static public PathData pathData = null;

    private GLSurfaceView mGLView = null;
    public int mScreenWidth,mScreenHeight;

    private MyGLSurfaceView sceneView = null;

    static public DataPanelView mPanelView = null;

    static public ListView mErrTipListview = null;

    //滚动的文字
    private Handler mEventHandler;
//    private AutoTextView mAutoTextView;
    private int mLoopCount = 0;
    private ArrayList<String> mStringArray;

    private native void CreateObjectNative(AssetManager assetManager, String pathToInternalDir);
    private native void DeleteObjectNative();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.activity_preview);
        setContentView(MResource.getIdByName(getApplication(), "layout", "activity_preview"));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = wm.getDefaultDisplay().getWidth();
        mScreenHeight = wm.getDefaultDisplay().getHeight();

        AssetManager assetManager = getAssets();
        String pathToInternalDir = getFilesDir().getAbsolutePath();

        // call the native constructors to create an object
        CreateObjectNative(assetManager, pathToInternalDir);

        sceneView = new MyGLSurfaceView(this);
        sceneView.requestFocus();
        sceneView.setFocusableInTouchMode(true);
        //RelativeLayout layout = (RelativeLayout) findViewById(R.id.gl_surface_view);
        RelativeLayout layout = (RelativeLayout) findViewById(MResource.getIdByName(getApplication(), "id", "gl_surface_view"));
        layout.addView(sceneView);

        //LinearLayout ss = (LinearLayout) findViewById(R.id.myDataLayout);
        LinearLayout ss = (LinearLayout) findViewById(MResource.getIdByName(getApplication(), "id", "myDataLayout"));
        mPanelView = new DataPanelView(this);
        mPanelView.setScreenWidth(this.mScreenWidth);
        mPanelView.setScreenHeight(this.mScreenHeight);
        ss.addView(mPanelView);

        mErrTipListview = (ListView)findViewById(MResource.getIdByName(getApplication(), "id", "lvErrTip"));
        SimpleAdapter adapter = new SimpleAdapter(this,getData(),MResource.getIdByName(getApplication(), "layout", "listview_errtip"),
                new String[]{"title","info","img"},
                new int[]{MResource.getIdByName(getApplication(), "id", "title"),MResource.getIdByName(getApplication(), "id", "info")});
        mErrTipListview.setAdapter(adapter);

        mErrTipListview.setSelection(2);


       // LinearLayout viewPortPanel = (LinearLayout)findViewById(R.id.myLinearLayout);
        LinearLayout viewPortPanel = (LinearLayout)findViewById(MResource.getIdByName(getApplication(), "id", "myLinearLayout"));
       // Button bnLookDown = (Button)viewPortPanel.findViewById(R.id.btnLookDown);
        final Button bnLookDown = (Button)viewPortPanel.findViewById(MResource.getIdByName(getApplication(), "id", "btnLookDown"));
        //Button bnFollow = (Button)viewPortPanel.findViewById(R.id.btnFollow);
        final Button bnFollow = (Button)viewPortPanel.findViewById(MResource.getIdByName(getApplication(), "id", "btnFollow"));
        //Button bnFreendom = (Button)viewPortPanel.findViewById(R.id.btnFreedom);
        final Button bnFreendom = (Button)viewPortPanel.findViewById(MResource.getIdByName(getApplication(), "id", "btnFreedom"));

        bnLookDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bnLookDown.setBackgroundColor(Color.argb(255, 160, 160, 160));
                bnFollow.setBackgroundColor(Color.argb(160, 160, 160, 160));
                bnFreendom.setBackgroundColor(Color.argb(160, 160, 160, 160));
                sceneView.setOperaode(0);
            }
        });

        bnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bnFollow.setBackgroundColor(Color.argb(255, 160, 160, 160));
                bnLookDown.setBackgroundColor(Color.argb(160, 160, 160, 160));
                bnFreendom.setBackgroundColor(Color.argb(160, 160, 160, 160));
                sceneView.setOperaode(1);
            }
        });

        bnFreendom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bnFreendom.setBackgroundColor(Color.argb(255, 160, 160, 160));
                bnFollow.setBackgroundColor(Color.argb(160, 160, 160, 160));
                bnLookDown.setBackgroundColor(Color.argb(160, 160, 160, 160));
                sceneView.setOperaode(2);
            }
        });
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "G1");
        map.put("info", "google 1");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "G2");
        map.put("info", "google 2");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "G3");
        map.put("info", "google 3");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "G4");
        map.put("info", "google 4");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "G5");
        map.put("info", "google 5");
        list.add(map);

        return list;
    }

    @Override
    protected void onResume() {

        super.onResume();

        // Android suggests that we call onResume on GLSurfaceView
        if (mGLView != null) {
            mGLView.onResume();
        }

    }

    @Override
    protected void onPause() {

        super.onPause();

        // Android suggests that we call onPause on GLSurfaceView
        if(mGLView != null) {
            mGLView.onPause();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        // We are exiting the activity, let's delete the native objects
        DeleteObjectNative();

    }

    /**
     * load libModelAssimpNative.so since it has all the native functions
     */
    static {
        System.loadLibrary("ModelAssimpNative");
    }
}
