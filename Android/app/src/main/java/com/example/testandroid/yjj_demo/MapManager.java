package com.example.testandroid.yjj_demo;

import android.content.Context;

import com.example.testandroid.yjj_demo.DataModels.MapData;
import com.example.testandroid.yjj_demo.DataModels.PathData;
import com.example.testandroid.yjj_demo.tools.MatrixState;
import com.example.testandroid.yjj_demo.tools.StringUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class MapManager {
    private Context mContext = null;
    public static String mPartName;

    public static PathData mPathData=null;
    public static MapData mMapData = null;
    public static Vector<Vector<Float>> mTriStripPointLists = new Vector<Vector<Float>>();

    public MapManager(Context context, InputStream mapStream, InputStream routeStream) throws IOException {

        mContext = context;
        String MapStr = StringUtil.inputStream2String(mapStream);
        String pathStr = StringUtil.inputStream2String(routeStream);

        mPathData = new Gson().fromJson(pathStr, PathData.class);
        mMapData = new Gson().fromJson(MapStr, MapData.class);


    }


}
