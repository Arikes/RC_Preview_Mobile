//
//  DataPool.h
//  YJJ_Demo
//
//  Created by YJJ_lxh on 17/4/8.
//  Copyright © 2017年 YJJ_lxh. All rights reserved.
//
#ifndef H_DATAPOOL_H
#define H_DATAPOOL_H
#import <Foundation/Foundation.h>
#import "pointModel.h"
#import "mapDataModel.h"
#import "pathDataModel.h"
#import <SceneKit/SceneKit.h>

@interface DataPool : NSObject
{
    mapDataModel *mapModle;
}

-(void) getResData;

-(pathDataModel*) getPathData;

-(SCNVector3) getCenterOfArea;
@end
#endif
