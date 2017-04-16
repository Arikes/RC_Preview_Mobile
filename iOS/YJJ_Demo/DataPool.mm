//
//  DataPool.m
//  YJJ_Demo
//
//  Created by YJJ_lxh on 17/4/8.
//  Copyright © 2017年 YJJ_lxh. All rights reserved.
//

#import "DataPool.h"
#import "YYModel.h"
#import <SceneKit/SceneKit.h>
#import <vector>
#import <map>
#import <string>
using namespace std;

#import "qtNodeObjsModel.h"
#import "ppNodeObjsModel.h"
#import "rpNodeObjsModel.h"
#import "cdNodeObjsModel.h"
#import "spNodeObjsModel.h"
#import "spsubNodeObjsModel.h"

static const float Default_Z_Value =0.0003;
//三角形片顶点，索引
vector<SCNVector3> g_Vertices;
vector<int> g_Indices;

//白色边线顶点，索引
vector<SCNVector3> m_whiteLineVertices;
vector<int> m_whiteLineIndices;

//黄色边线顶点，索引
vector<SCNVector3> m_yellowLineVertices;
vector<int> m_yellowLineIndices;

pathDataModel *routeData;

@implementation DataPool
{
}
vector<int> rpIndices={
    1,2,3,
    1,3,0,
    3,6,0,
    6,7,0,
    3,4,6,
    4,5,6
};

vector<int> qtIndices =
{
    2,3,4,
    2,4,1,
    1,4,0,
    0,4,5
};

vector<int> spIndices =
{
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

-(void) getResData
{
    //获取场地数据信息以及车辆轨迹数据
    NSString *mapFilePath = [[NSBundle mainBundle]pathForResource:@"mapData" ofType:@"json"];
    NSString* content2 = [NSString stringWithContentsOfFile:mapFilePath encoding:NSUTF8StringEncoding error:nil];
    NSData *data = [content2 dataUsingEncoding:NSUTF8StringEncoding];
    mapModle = [[mapDataModel alloc]init];
    [mapModle yy_modelSetWithJSON:data];
    
//    /Users/yjj_chy/Desktop/YJJ_Demo/paths
    NSString* resPath = [[NSBundle mainBundle] resourcePath];
    
    NSString *routeDataPath = [[NSBundle mainBundle]pathForResource:@"rightDk" ofType:@"json"];
    NSString* routeContent = [NSString stringWithContentsOfFile:routeDataPath encoding:NSUTF8StringEncoding error:nil];
    NSData *routeNSData = [routeContent dataUsingEncoding:NSUTF8StringEncoding];
    routeData = [[pathDataModel alloc]init];
    [routeData yy_modelSetWithJSON:routeNSData];

    //获取数据结束
    NSLog(@"获取场地数据信息以及车辆轨迹数据成功");
    
    self.calDrawLine;
    self.calDrawData;
}

-(pathDataModel*) getPathData
{
    return routeData;
}

-(void) calDrawLine
{
    m_whiteLineVertices.clear();m_whiteLineIndices.clear();
    m_yellowLineVertices.clear();m_yellowLineIndices.clear();
    //倒车入库场地
    for(int i=0;i<mapModle.rpNodeObjs.count;i++)
    {
        rpNodeObjsModel* curNode = mapModle.rpNodeObjs[i];
        
        pointModel* curPoint =curNode.Points[curNode.Points.count-1];
        pointModel* nextPoint = curNode.Points[0];
        
        m_yellowLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_yellowLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,curPoint.z/100+Default_Z_Value});
        
        curPoint =curNode.Points[0];
        nextPoint = curNode.Points[1];
        m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,curPoint.z/100+Default_Z_Value});

        
        curPoint =curNode.Points[1];
        nextPoint = curNode.Points[2];
        m_yellowLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_yellowLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,curPoint.z/100+Default_Z_Value});


        curPoint =curNode.Points[2];
        nextPoint = curNode.Points[3];
        m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,curPoint.z/100+Default_Z_Value});
        
        curPoint =curNode.Points[3];
        nextPoint = curNode.Points[4];
        m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,curPoint.z/100+Default_Z_Value});
        
        curPoint =curNode.Points[4];
        nextPoint = curNode.Points[5];
        m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,curPoint.z/100+Default_Z_Value});
        
        curPoint =curNode.Points[5];
        nextPoint = curNode.Points[6];
        m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,curPoint.z/100+Default_Z_Value});
        
        curPoint =curNode.Points[6];
        nextPoint = curNode.Points[7];
        m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,curPoint.z/100+Default_Z_Value});
    }
    
    
    //侧方停车
    for(int i=0;i<mapModle.ppNodeObjs.count;i++)
    {
        ppNodeObjsModel* curNode = mapModle.ppNodeObjs[i];
        
        for (int ii=0; ii<curNode.Points.count-1; ii++) {
            pointModel* curPoint =curNode.Points[ii];
            pointModel* nextPoint = curNode.Points[ii+1];
            
            m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
            m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,curPoint.z/100+Default_Z_Value});
        }
        pointModel* curPoint =curNode.Points[3];
        pointModel* nextPoint = curNode.Points[6];
        
        m_yellowLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_yellowLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,curPoint.z/100+Default_Z_Value});
        
        curPoint =curNode.Points[0];
        nextPoint = curNode.Points[7];
        
        m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,curPoint.z/100+Default_Z_Value});
    }
    
    //直角转弯
    for(int i=0;i<mapModle.qtNodeObjs.count;i++)
    {
        qtNodeObjsModel* curNode = mapModle.qtNodeObjs[i];
        
        pointModel* curPoint =curNode.Points[0];
        pointModel* nextPoint = curNode.Points[1];
        
        m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,curPoint.z/100+Default_Z_Value});
        
        curPoint =curNode.Points[1];
        nextPoint = curNode.Points[2];
        
        m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,curPoint.z/100+Default_Z_Value});
        
        curPoint =curNode.Points[3];
        nextPoint = curNode.Points[4];
        
        m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,curPoint.z/100+Default_Z_Value});

        
        curPoint =curNode.Points[4];
        nextPoint = curNode.Points[5];
        
        m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,curPoint.z/100+Default_Z_Value});


    }
    
    //坡道边线
    for(int i=0;i<mapModle.spNodeObjs.count;i++)
    {
        spNodeObjsModel* curNode = mapModle.spNodeObjs[i];
        for (int i1=0; i1<curNode.leftWall.count-1; i1++) {
            pointModel* curPoint =curNode.leftWall[i1];
            pointModel* nextPoint = curNode.leftWall[i1+1];
            
            m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
            m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,nextPoint.z/100+Default_Z_Value});
        }
        
        for (int i1=0; i1<curNode.rightWall.count-1; i1++) {
            pointModel* curPoint =curNode.rightWall[i1];
            pointModel* nextPoint = curNode.rightWall[i1+1];
            
            m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
            m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,nextPoint.z/100+Default_Z_Value});
        }
    }
    

    //坡道控制线
    for(int i=0;i<mapModle.spsubNodeObjs.count;i++)
    {
        spsubNodeObjsModel* curNode = mapModle.spsubNodeObjs[i];
        //    spsubNodeObjsModel* curNode = mapModle.spsubNodeObjs[0];
        pointModel* curPoint =curNode.Points[4];
        pointModel* nextPoint = curNode.Points[5];
        m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,nextPoint.z/100+Default_Z_Value});
        
        curPoint =curNode.Points[6];
        nextPoint = curNode.Points[7];
        m_yellowLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_yellowLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,nextPoint.z/100+Default_Z_Value});
        curPoint =curNode.Points[8];
        nextPoint = curNode.Points[9];
        m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,nextPoint.z/100+Default_Z_Value});
        
        curPoint =curNode.Points[0];
        nextPoint = curNode.Points[1];
        m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,nextPoint.z/100+Default_Z_Value});
        
        curPoint =curNode.Points[2];
        nextPoint = curNode.Points[3];
        m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
        m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,nextPoint.z/100+Default_Z_Value});
    }
    
    
    //弯道边线
    for(int i=0;i<mapModle.cdNodeObjs.count;i++)
    {
        cdNodeObjsModel* curNode = mapModle.cdNodeObjs[i];

        for (int k=0; k<curNode.leftPoints.count-1; k++) {
            pointModel* curPoint =curNode.leftPoints[k];
            pointModel* nextPoint = curNode.leftPoints[k+1];
            
            m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
            m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,nextPoint.z/100+Default_Z_Value});
        }
        
        for (int k=0; k<curNode.rightPoints.count-1; k++) {
            pointModel* curPoint =curNode.rightPoints[k];
            pointModel* nextPoint = curNode.rightPoints[k+1];
            
            m_whiteLineVertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100+Default_Z_Value});
            m_whiteLineVertices.push_back({nextPoint.x/100,nextPoint.y/100,nextPoint.z/100+Default_Z_Value});
        }
    }
    
    //cal index for sideline
    for(int i =0;i<m_whiteLineVertices.size();i++)
    {
        m_whiteLineIndices.push_back(i);
    }
    
    for (int i=0; i<m_yellowLineVertices.size(); i++) {
        m_yellowLineIndices.push_back(i);
    }
    
}

-(void) calDrawData
{
    g_Vertices.clear();g_Indices.clear();
    //倒车入库场地
    for(int i=0;i<mapModle.rpNodeObjs.count;i++)
    {
        rpNodeObjsModel* curNode = mapModle.rpNodeObjs[i];
        for(int j = 0;j<curNode.Points.count;j++)
        {
            pointModel* curPoint =curNode.Points[j];
            g_Vertices.push_back({curPoint.x/100,curPoint.y/100,0.0});
        }
        
        for (int ii=0; ii<rpIndices.size(); ii++) {
            int index =rpIndices[ii]+i*8;
            g_Indices.push_back(index);
        }
    }
    
    int baseIndex;
    //    坡道场地
    baseIndex = g_Vertices.size();
    for (int i=0; i<mapModle.spNodeObjs.count; i++) {
        spNodeObjsModel* curNode = mapModle.spNodeObjs[i];
        for (int j=0; j<curNode.leftWall.count; j++) {
            pointModel* curPoint = curNode.leftWall[j];
            g_Vertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100});
        }
        for (int j=0; j<curNode.rightWall.count; j++) {
            pointModel* curPoint = curNode.rightWall[j];
            g_Vertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100});
        }
        
        for(int ii=0;ii<spIndices.size();ii++)
        {
            int index = spIndices[ii]+i*(curNode.leftWall.count+curNode.rightWall.count)+baseIndex;
            g_Indices.push_back(index);
        }
    }
    //直角转弯场地
    baseIndex = g_Vertices.size();
    for (int i=0; i<mapModle.qtNodeObjs.count; i++) {
        qtNodeObjsModel* curNode=mapModle.qtNodeObjs[i];
        for (int j=0; j<curNode.Points.count; j++) {
            pointModel* curPoint = curNode.Points[j];
            g_Vertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100});
        }
        for (int ii=0; ii<qtIndices.size(); ii++) {
            int index = qtIndices[ii]+i*curNode.Points.count+baseIndex;
            g_Indices.push_back(index);
        }
    }
    //侧方停车场地
    baseIndex = g_Vertices.size();
    for(int i=0;i<mapModle.ppNodeObjs.count;i++)
    {
        ppNodeObjsModel* curNode = mapModle.ppNodeObjs[i];
        for (int j=0; j<curNode.Points.count; j++) {
            pointModel* curPoint = curNode.Points[j];
            g_Vertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100});
        }
        
        for (int ii=0; ii<rpIndices.size(); ii++) {
            int index = rpIndices[ii]+i*8+baseIndex;
            g_Indices.push_back(index);
        }
    }
    
    //cal Indices for CurveDriving
    for(int i=0;i<mapModle.cdNodeObjs.count;i++)
    {
        baseIndex = g_Vertices.size();
        cdNodeObjsModel* curNode = mapModle.cdNodeObjs[i];
        for (int i1=0; i1<curNode.leftPoints.count; i1++) {
            pointModel* curPoint = curNode.leftPoints[i1];
            g_Vertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100});
        }
        for (int i2=curNode.rightPoints.count-1; i2>=0; i2--) {
            pointModel* curPoint = curNode.rightPoints[i2];
            g_Vertices.push_back({curPoint.x/100,curPoint.y/100,curPoint.z/100});
        }
        
        int rightBaseIndex = baseIndex+curNode.leftPoints.count;
        
        int left=0,right=0;
        int odd=0;
        while (left<curNode.leftPoints.count-1 && right<curNode.rightPoints.count-1) {
            if (odd%2==0) {
                g_Indices.push_back(baseIndex+left);
                g_Indices.push_back(baseIndex+left+1);
                g_Indices.push_back(rightBaseIndex+right);
                left++;

            }else
            {
                g_Indices.push_back(baseIndex+left);
                g_Indices.push_back(rightBaseIndex+right);
                g_Indices.push_back(rightBaseIndex+right+1);
                right++;
            }
            odd++;
        }
        
        while (left<curNode.leftPoints.count-1) {
            g_Indices.push_back(baseIndex+left);
            g_Indices.push_back(baseIndex+left+1);
            g_Indices.push_back(rightBaseIndex+right);
            left++;
        }
        
        while(right<curNode.rightPoints.count-1) {
            g_Indices.push_back(baseIndex+left);
            g_Indices.push_back(rightBaseIndex+right);
            g_Indices.push_back(rightBaseIndex+right+1);
            right++;
        }
        
    }
}

-(SCNVector3) getCenterOfArea
{
    SCNVector3 centerPt = SCNVector3Make(0, 0, 0);
    if ([routeData.AreaID hasPrefix:@"QT"]) {
        for(int i=0;i<mapModle.qtNodeObjs.count;i++)
        {
            qtNodeObjsModel* curNode = mapModle.qtNodeObjs[i];
            if ([routeData.AreaID isEqualToString:curNode.code]) {
                pointModel* curPoint3 =curNode.Points[1];
                pointModel* curPoint6 =curNode.Points[4];
                centerPt.x = (curPoint3.x+curPoint6.x)/200.0;
                centerPt.y = (curPoint3.y+curPoint6.y)/200.0;
                centerPt.z = (curPoint3.z+curPoint6.z)/200.0+5.5;
                break;
            }
        }
        //直角转弯
    }else if ([routeData.AreaID hasPrefix:@"RP"])
    {
        //倒车入库
        for(int i=0;i<mapModle.rpNodeObjs.count;i++)
        {
            rpNodeObjsModel* curNode = mapModle.rpNodeObjs[i];
            if ([routeData.AreaID isEqualToString:curNode.code]) {
                pointModel* curPoint3 =curNode.Points[3];
                pointModel* curPoint6 =curNode.Points[6];
                centerPt.x = (curPoint3.x+curPoint6.x)/200.0;
                centerPt.y = (curPoint3.y+curPoint6.y)/200.0;
                centerPt.z = (curPoint3.z+curPoint6.z)/200.0+5.5;
                break;
            }
        }
    }else if ([routeData.AreaID hasPrefix:@"PP"])
    {
        //侧方停车
        for(int i=0;i<mapModle.ppNodeObjs.count;i++)
        {
            ppNodeObjsModel* curNode = mapModle.ppNodeObjs[i];
            if ([routeData.AreaID isEqualToString:curNode.code]) {
                pointModel* curPoint3 =curNode.Points[3];
                pointModel* curPoint6 =curNode.Points[6];
                centerPt.x = (curPoint3.x+curPoint6.x)/200.0;
                centerPt.y = (curPoint3.y+curPoint6.y)/200.0;
                centerPt.z = (curPoint3.z+curPoint6.z)/200.0+5.5;
                break;
            }
        }
    }else if ([routeData.AreaID hasPrefix:@"CD"])
    {
        //弯道驾驶
        for(int i=0;i<mapModle.cdNodeObjs.count;i++)
        {
            cdNodeObjsModel* curNode = mapModle.cdNodeObjs[i];
            if ([routeData.AreaID isEqualToString:curNode.code]) {
                pointModel* curPoint3 =curNode.leftPoints[0];
                pointModel* curPoint6 =curNode.rightPoints[0];
                centerPt.x = (curPoint3.x+curPoint6.x)/200.0;
                centerPt.y = (curPoint3.y+curPoint6.y)/200.0;
                centerPt.z = (curPoint3.z+curPoint6.z)/200.0+5.5;
                break;
            }
        }
    }else if ([routeData.AreaID hasPrefix:@"SP"])
    {
        //坡道驾驶
    }
    
    return centerPt;
}


@end
