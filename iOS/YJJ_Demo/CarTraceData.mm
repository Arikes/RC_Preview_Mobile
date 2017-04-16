//
//  CarTraceData.m
//  YJJ_Demo
//
//  Created by YJJ_lxh on 17/4/14.
//  Copyright © 2017年 YJJ_lxh. All rights reserved.
//

#import "CarTraceData.h"
#include <stack>
#include <vector>
#import <map>
#import <string>
using namespace std;

//三角形片顶点，索引
vector<SCNVector3> g_TraceVertices;
vector<int> g_TraceIndices;

@implementation CarTraceData
{
    vector<SCNVector3> tempVector;
    
    vector<SCNVector3> tempLeftVector;
    vector<SCNVector3> tempRightVector;
}

-(void)addTracePoint:(SCNVector3)curPoint
{
    if (tempVector.empty()) {
        tempVector.push_back(curPoint);
    }
    SCNVector3 endPoint = tempVector[tempVector.size()-1];
    if (endPoint.x != curPoint.x &&endPoint.y != curPoint.y) {
        tempVector.push_back(curPoint);
        float rate = -1.0/((endPoint.y-curPoint.y)/(endPoint.x-curPoint.x));
        
        SCNVector3 vv ;
        vv.x = 1.0;
        vv.z = 0.0;
        vv.y = rate;
        
        SCNVector3 left = {curPoint.x+vv.x,curPoint.y+vv.y,0.0};
        SCNVector3 right = {curPoint.x+vv.x,curPoint.y+vv.y,0.0};
        
        tempLeftVector.push_back(left);
        tempRightVector.push_back(right);
    }
    
    g_TraceVertices.push_back({0,0,0.01});
    g_TraceVertices.push_back({0,3,0.01});
    g_TraceVertices.push_back({3,3,0.01});
    
    g_TraceIndices.push_back(0);
    g_TraceIndices.push_back(1);
    g_TraceIndices.push_back(2);
    
    
}

-(void)resetTraceData
{
    tempVector.clear();
//    g_TraceVertices.push_back({0,0,2});
//    g_TraceVertices.push_back({0,3,2});
//    g_TraceVertices.push_back({3,3,2});
//    
//    g_TraceIndices.push_back(0);
//    g_TraceIndices.push_back(1);
//    g_TraceIndices.push_back(2);

}

@end
