package com.example.testandroid.yjj_demo.tools;

import android.text.TextUtils;

import com.example.testandroid.yjj_demo.DataModels.MapData;
import com.example.testandroid.yjj_demo.DataModels.PathData;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/4/11.
 */

public class DataProcessUtil {

    public static MapData getMapDataFromString(String response){
        return new Gson().fromJson(response, MapData.class);
    }

    public static PathData getPathDataFromString(String response){
        return new Gson().fromJson(response, PathData.class);
    }
}
