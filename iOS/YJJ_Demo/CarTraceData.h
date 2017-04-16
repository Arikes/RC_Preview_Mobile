//
//  CarTraceData.h
//  YJJ_Demo
//
//  Created by YJJ_lxh on 17/4/14.
//  Copyright © 2017年 YJJ_lxh. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "pointModel.h"
#import "mapDataModel.h"
#import "pathDataModel.h"
#import <SceneKit/SceneKit.h>
@interface CarTraceData : NSObject
-(void)addTracePoint:(SCNVector3)curPoint;
-(void)resetTraceData;
@end
