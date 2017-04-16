package com.example.testandroid.yjj_demo.DataModels;

import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public class MapData {
    public List<QuarterTurnNode2> qtNodeObjs;
    public List <ParallelParkingNode2> ppNodeObjs;
    public List <ReverseParkingNode2> rpNodeObjs;
    public List <CurveDrivingNode2> cdNodeObjs;
    public List <SlopeSlopeParkingNode2> spNodeObjs;
    public List <SubSlopeParkingNode2> spsubNodeObjs;

    public MapData(){

    }

    public List<QuarterTurnNode2> getQtNodeObjs() {
        return qtNodeObjs;
    }

    public List<ParallelParkingNode2> getPpNodeObjs() {
        return ppNodeObjs;
    }

    public List<ReverseParkingNode2> getRpNodeObjs() {
        return rpNodeObjs;
    }

    public List<CurveDrivingNode2> getCdNodeObjs() {
        return cdNodeObjs;
    }

    public List<SlopeSlopeParkingNode2> getSpNodeObjs() {
        return spNodeObjs;
    }

    public List<SubSlopeParkingNode2> getSpsubNodeObjs() {
        return spsubNodeObjs;
    }
}
