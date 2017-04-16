package com.example.testandroid.yjj_demo.SceneRenderable;

import android.graphics.Color;

import com.anandmuralidhar.assimpandroid.AssimpActivity;
import com.anandmuralidhar.assimpandroid.MyGLSurfaceView;
import com.example.testandroid.yjj_demo.DataModels.CurveDrivingNode2;
import com.example.testandroid.yjj_demo.DataModels.ParallelParkingNode2;
import com.example.testandroid.yjj_demo.DataModels.PointNode;
import com.example.testandroid.yjj_demo.DataModels.QuarterTurnNode2;
import com.example.testandroid.yjj_demo.DataModels.ReverseParkingNode2;
import com.example.testandroid.yjj_demo.DataModels.SlopeSlopeParkingNode2;
import com.example.testandroid.yjj_demo.Rendrable.LineRenderable;
import com.example.testandroid.yjj_demo.Rendrable.MeshRenderable;
import com.example.testandroid.yjj_demo.Rendrable.Renderable;
import com.example.testandroid.yjj_demo.tools.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2017/4/8.
 */

public class SceneRenderable implements Renderable {

    private MyGLSurfaceView mv = null;

    private Vector<Vector<Float>> mTriStripPointLists = new Vector<Vector<Float>>();

    private Vector<Float> mlinePointList = new Vector<Float>();
    private Vector<Float> mlineYellowPointList = new Vector<Float>();

    private LineRenderable lineWhite = null;
    private LineRenderable lineYellow = null;

    private List<LineRenderable> lineCDList = null;

    private Vector<Float> mVerts = new Vector<Float>();
    private Vector<Integer> mIndices = new Vector<Integer>();
    private MeshRenderable areaTriangle = null;

    private Vector<Float> cdVert = null;
    private Vector<Integer> cdIndices = null;
    private MeshRenderable cdTriangle = null;

    private short[] quarterIndices = {
            2,3,4,
            2,4,1,
            1,4,0,
            0,4,5
    };

    private short[] spIndices ={
            0,1,7,
            1,8,7,
            1,2,8,
            2,9,8,
            2,3,9,
            3,10,9,
            3,4,10,
            4,11,10,
            4,5,11,
            5,12,11,
            5,6,12,
            6,13,12
    };

    private short[] rpIndices ={
            1,2,3,
            1,3,0,
            3,6,0,
            6,7,0,
            3,4,6,
            4,5,6
    };

    public SceneRenderable(MyGLSurfaceView mv){
        this.mv = mv;

        initSceneData();

        initCDLineRenderableData();

        lineWhite = new LineRenderable(mv, mlinePointList, false);
        lineWhite.setLineColor(Color.WHITE);
        lineWhite.setLineWidth(6.0f);

        lineYellow = new LineRenderable(mv, mlineYellowPointList, false);
        lineYellow.setLineColor(Color.YELLOW);
        lineYellow.setLineWidth(3.0f);

        areaTriangle = new MeshRenderable(mv, mVerts, mIndices);
        areaTriangle.setMeshColor(Color.argb(255, (int)(0.336f * 255), (int)(0.336f * 255), (int)(0.336f * 255)));

        cdTriangle  = new MeshRenderable(mv, cdVert, cdIndices);
        cdTriangle.setMeshColor(Color.argb(255, (int)(0.336f * 255), (int)(0.336f * 255), (int)(0.336f * 255)));

    }

    @Override
    public void render() {
        if(lineCDList != null){
            for(LineRenderable line : lineCDList){
                line.render();
            }
        }

        if(lineWhite != null){
            lineWhite.render();
        }

        if(lineYellow != null)
            lineYellow.render();

        if(areaTriangle != null){
            areaTriangle.render();
        }

        if(cdTriangle != null){
            cdTriangle.render();
        }


    }

    public Vector3f getCenterByName(String name){
        Vector3f pt = new Vector3f(0.0f,0.0f,0.0f);
        if(name.contains("QT"))
        {
            for(QuarterTurnNode2 curNode : AssimpActivity.mapData.getQtNodeObjs() ){
                if(curNode.code.equals(name)){
                    pt.x = curNode.Points.get(1).x;
                    pt.z = curNode.Points.get(1).y;
                    break;
                }

            }
        }else if(name.contains("PP")){
            for(ParallelParkingNode2 curNode : AssimpActivity.mapData.getPpNodeObjs() ){
                if(curNode.code.equals(name)){
                    pt.x = (curNode.Points.get(3).x+curNode.Points.get(6).x)/2.0f;
                    pt.z = (curNode.Points.get(3).y+curNode.Points.get(6).y)/2.0f;
                    break;
                }

            }
        }else if(name.contains("RP")){
            for(ReverseParkingNode2 curNode : AssimpActivity.mapData.getRpNodeObjs() ){
                if(curNode.code.equals(name)){
                    pt.x = (curNode.Points.get(3).x+curNode.Points.get(6).x)/2.0f;
                    pt.z = (curNode.Points.get(3).y+curNode.Points.get(6).y)/2.0f;
                    break;
                }
            }
        }else if(name.contains("CD")){
            for(CurveDrivingNode2 curNode:AssimpActivity.mapData.getCdNodeObjs()){
                if(curNode.code.equals(name)){
                    pt.x = (curNode.leftPoints.get(0).x+curNode.rightPoints.get(0).x)/2.0f;
                    pt.z = (curNode.leftPoints.get(0).y+curNode.rightPoints.get(0).y)/2.0f;
                    break;
                }
            }
        }
        else{
            pt.x=0.0f;
            pt.z=0.0f;
        }

        pt.x/=100.0f;
        pt.z/=100.0f;
        pt.y=0.0f;
        return pt;
    }

    private void initSceneData(){
        //初始化将要绘制的所有场地的边线

        //弯道行驶边线
        for(CurveDrivingNode2 curNode : AssimpActivity.mapData.getCdNodeObjs()){

            Vector<Float> triStripPointList = new Vector<Float>();
            int leftIndex=0,rightIndex = curNode.rightPoints.size()-1;
            int index=0;
            while(leftIndex<curNode.leftPoints.size()&&rightIndex>0){
                if(index%2==0){
                    triStripPointList.add(curNode.leftPoints.get(leftIndex).x/100);
                    triStripPointList.add(curNode.leftPoints.get(leftIndex).z/100);
                    triStripPointList.add(curNode.leftPoints.get(leftIndex).y/100);
                    leftIndex++;
                }else{
                    triStripPointList.add(curNode.rightPoints.get(rightIndex).x/100);
                    triStripPointList.add(curNode.rightPoints.get(rightIndex).z/100);
                    triStripPointList.add(curNode.rightPoints.get(rightIndex).y/100);
                    rightIndex--;
                }
                index++;
            }
            mTriStripPointLists.add(triStripPointList);
            triStripPointList = null;
        }

        //直角转弯边线
        for(QuarterTurnNode2 curNode : AssimpActivity.mapData.getQtNodeObjs()){
            mlinePointList.add(curNode.Points.get(3).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(3).y/100);

            mlinePointList.add(curNode.Points.get(4).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(4).y/100);

            mlinePointList.add(curNode.Points.get(4).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(4).y/100);

            mlinePointList.add(curNode.Points.get(5).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(5).y/100);

            mlinePointList.add(curNode.Points.get(0).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(0).y/100);

            mlinePointList.add(curNode.Points.get(1).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(1).y/100);

            mlinePointList.add(curNode.Points.get(1).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(1).y/100);

            mlinePointList.add(curNode.Points.get(2).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(2).y/100);
        }


        //倒车入库边线
        for(ReverseParkingNode2 curNode : AssimpActivity.mapData.getRpNodeObjs()){
            mlinePointList.add(curNode.Points.get(0).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(0).y/100);

            mlinePointList.add(curNode.Points.get(1).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(1).y/100);

            mlinePointList.add(curNode.Points.get(2).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(2).y/100);

            mlinePointList.add(curNode.Points.get(3).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(3).y/100);

            mlinePointList.add(curNode.Points.get(3).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(3).y/100);

            mlinePointList.add(curNode.Points.get(4).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(4).y/100);


            mlinePointList.add(curNode.Points.get(4).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(4).y/100);

            mlinePointList.add(curNode.Points.get(5).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(5).y/100);

            mlinePointList.add(curNode.Points.get(5).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(5).y/100);

            mlinePointList.add(curNode.Points.get(6).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(6).y/100);

            mlinePointList.add(curNode.Points.get(6).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(6).y/100);

            mlinePointList.add(curNode.Points.get(7).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(7).y/100);

            mlineYellowPointList.add(curNode.Points.get(7).x/100);
            mlineYellowPointList.add(0.0f);
            mlineYellowPointList.add(curNode.Points.get(7).y/100);

            mlineYellowPointList.add(curNode.Points.get(0).x/100);
            mlineYellowPointList.add(0.0f);
            mlineYellowPointList.add(curNode.Points.get(0).y/100);

            mlineYellowPointList.add(curNode.Points.get(1).x/100);
            mlineYellowPointList.add(0.0f);
            mlineYellowPointList.add(curNode.Points.get(1).y/100);

            mlineYellowPointList.add(curNode.Points.get(2).x/100);
            mlineYellowPointList.add(0.0f);
            mlineYellowPointList.add(curNode.Points.get(2).y/100);
        }


        //侧方停车边线绘制
        for(ParallelParkingNode2 curNode : AssimpActivity.mapData.getPpNodeObjs()){
            mlinePointList.add(curNode.Points.get(0).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(0).y/100);

            mlinePointList.add(curNode.Points.get(1).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(1).y/100);

            mlinePointList.add(curNode.Points.get(1).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(1).y/100);

            mlinePointList.add(curNode.Points.get(2).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(2).y/100);

            mlinePointList.add(curNode.Points.get(2).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(2).y/100);

            mlinePointList.add(curNode.Points.get(3).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(3).y/100);

            mlinePointList.add(curNode.Points.get(3).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(3).y/100);

            mlinePointList.add(curNode.Points.get(4).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(4).y/100);


            mlinePointList.add(curNode.Points.get(4).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(4).y/100);

            mlinePointList.add(curNode.Points.get(5).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(5).y/100);

            mlinePointList.add(curNode.Points.get(5).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(5).y/100);

            mlinePointList.add(curNode.Points.get(6).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(6).y/100);

            mlinePointList.add(curNode.Points.get(6).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(6).y/100);

            mlinePointList.add(curNode.Points.get(7).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(7).y/100);

            mlinePointList.add(curNode.Points.get(7).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(7).y/100);

            mlinePointList.add(curNode.Points.get(0).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.Points.get(0).y/100);

            mlineYellowPointList.add(curNode.Points.get(6).x/100);
            mlineYellowPointList.add(0.0f);
            mlineYellowPointList.add(curNode.Points.get(6).y/100);

            mlineYellowPointList.add(curNode.Points.get(3).x/100);
            mlineYellowPointList.add(0.0f);
            mlineYellowPointList.add(curNode.Points.get(3).y/100);
        }

        //坡道停车边线
//        public List <SlopeSlopeParkingNode2> spNodeObjs;
        for(SlopeSlopeParkingNode2 curNode : AssimpActivity.mapData.getSpNodeObjs()){

            mlinePointList.add(curNode.leftWall.get(0).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.leftWall.get(0).y/100);
            for(int i=1;i<curNode.leftWall.size()-1;i++)
            {
                mlinePointList.add(curNode.leftWall.get(i).x/100);
                mlinePointList.add(0.0f);
                mlinePointList.add(curNode.leftWall.get(i).y/100);

                mlinePointList.add(curNode.leftWall.get(i).x/100);
                mlinePointList.add(0.0f);
                mlinePointList.add(curNode.leftWall.get(i).y/100);
            }
            mlinePointList.add(curNode.leftWall.get(curNode.leftWall.size()-1).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.leftWall.get(curNode.leftWall.size()-1).y/100);


            mlinePointList.add(curNode.rightWall.get(0).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.rightWall.get(0).y/100);
            for(int i=1;i<curNode.rightWall.size()-1;i++)
            {
                mlinePointList.add(curNode.rightWall.get(i).x/100);
                mlinePointList.add(0.0f);
                mlinePointList.add(curNode.rightWall.get(i).y/100);

                mlinePointList.add(curNode.rightWall.get(i).x/100);
                mlinePointList.add(0.0f);
                mlinePointList.add(curNode.rightWall.get(i).y/100);
            }
            mlinePointList.add(curNode.rightWall.get(curNode.rightWall.size()-1).x/100);
            mlinePointList.add(0.0f);
            mlinePointList.add(curNode.rightWall.get(curNode.rightWall.size()-1).y/100);
        }

        mVerts.clear();
        mIndices.clear();

        int baseIndex = 0;
        for(int i = 0; i<AssimpActivity.mapData.getRpNodeObjs().size(); i++)
        {
            ReverseParkingNode2 curNode = AssimpActivity.mapData.getRpNodeObjs().get(i);
            for(PointNode curPt:curNode.Points)
            {
                mVerts.add(curPt.x/100);mVerts.add(curPt.z);mVerts.add(curPt.y/100);
            }

            for(int ii=0;ii<rpIndices.length;ii++)
            {
                int index = rpIndices[ii]+i*8;
                mIndices.add(index);
            }
        }
//        //侧方停车
        baseIndex = mVerts.size()/3;
        for(int i = 0;i < AssimpActivity.mapData.getPpNodeObjs().size(); i++)
        {
            ParallelParkingNode2 curNode = AssimpActivity.mapData.getPpNodeObjs().get(i);
            for(PointNode curPt:curNode.Points)
            {
                mVerts.add(curPt.x/100);mVerts.add(curPt.z);mVerts.add(curPt.y/100);
            }

            for(int ii=0;ii<rpIndices.length;ii++)
            {
                int index = rpIndices[ii]+i*8+baseIndex;
                mIndices.add(index);
            }
        }
//
        //直角转弯
        baseIndex = mVerts.size()/3;
        for(int i = 0 ;i < AssimpActivity.mapData.getQtNodeObjs().size(); i++)
        {
            QuarterTurnNode2 curNode = AssimpActivity.mapData.getQtNodeObjs().get(i);
            for(PointNode curPt:curNode.Points)
            {
                mVerts.add(curPt.x/100);mVerts.add(curPt.z);mVerts.add(curPt.y/100);
            }

            for(int ii=0;ii<quarterIndices.length;ii++)
            {
                int index = quarterIndices[ii]+i*curNode.Points.size()+baseIndex;
                mIndices.add(index);
            }
        }
        //坡道行驶
        baseIndex = mVerts.size() / 3;
        for(int i = 0; i<AssimpActivity.mapData.getSpNodeObjs().size(); i++)
        {
            SlopeSlopeParkingNode2 curNode = AssimpActivity.mapData.getSpNodeObjs().get(i);
            for(PointNode curPt:curNode.leftWall)
            {
                mVerts.add(curPt.x/100);mVerts.add(0.0f);mVerts.add(curPt.y/100);
            }
            for(PointNode curPt:curNode.rightWall)
            {
                mVerts.add(curPt.x/100);mVerts.add(0.0f);mVerts.add(curPt.y/100);
            }

            for(int ii=0;ii<spIndices.length;ii++)
            {
                int index = spIndices[ii]+i*(curNode.leftWall.size()/3+curNode.rightWall.size()/3)+baseIndex;
                mIndices.add(index);
            }
        }

        //弯道
        cdVert = new Vector<Float>();
        cdIndices = new Vector<Integer>();
        for(int i = 0; i < 6; i++)
        {
            CurveDrivingNode2 curNode = AssimpActivity.mapData.getCdNodeObjs().get(i);
            baseIndex = cdVert.size() / 3;
            int rightBaseIndex = baseIndex + curNode.leftPoints.size();
            for(int ivL = 0; ivL < curNode.leftPoints.size(); ivL++){
                PointNode curPoint = curNode.leftPoints.get(ivL);
                cdVert.add(curPoint.x/100);cdVert.add(0f);cdVert.add(curPoint.y/100);
            }

            for(int ivR = curNode.rightPoints.size() - 1; ivR >= 0; ivR--){
                PointNode curPoint = curNode.rightPoints.get(ivR);
                cdVert.add(curPoint.x/100);cdVert.add(0f);cdVert.add(curPoint.y/100);
            }

            int left = 0,right = 0;
            int odd = 0;
            while (left < curNode.leftPoints.size() - 1 && right < curNode.rightPoints.size() - 1) {
                if (odd % 2 == 0) {
                    cdIndices.add((baseIndex + left));
                    cdIndices.add((baseIndex + left + 1));
                    cdIndices.add((rightBaseIndex + right));

                    left++;

                }else
                {
                    cdIndices.add((baseIndex + left));
                    cdIndices.add((rightBaseIndex + right));
                    cdIndices.add((rightBaseIndex + right + 1));

                    right++;
                }
                odd++;
            }
        }
    }

    private void initCDLineRenderableData(){
        if(lineCDList == null){
            lineCDList = new ArrayList<LineRenderable>(AssimpActivity.mapData.getCdNodeObjs().size() * 2);

            for(CurveDrivingNode2 curNode : AssimpActivity.mapData.getCdNodeObjs()){

                Vector<Float> leftPts = new Vector<Float>();
                Vector<Float> rightPts= new Vector<Float>();

                for(PointNode curPoint:curNode.leftPoints){
                    leftPts.add(curPoint.x/100);
                    leftPts.add(curPoint.z/100);
                    leftPts.add(curPoint.y/100);
                }

                for(PointNode curPoint:curNode.rightPoints){
                    rightPts.add(curPoint.x/100);
                    rightPts.add(curPoint.z/100);
                    rightPts.add(curPoint.y/100);
                }

                LineRenderable leftLineStrip = new LineRenderable(mv, leftPts, true);
                leftLineStrip.setLineColor(Color.WHITE);
                leftLineStrip.setLineWidth(6.0f);
                LineRenderable rightLineStrip = new LineRenderable(mv, rightPts, true);
                rightLineStrip.setLineColor(Color.WHITE);
                rightLineStrip.setLineWidth(6.0f);

                lineCDList.add(leftLineStrip);
                lineCDList.add(rightLineStrip);

            }
        }
    }
}
