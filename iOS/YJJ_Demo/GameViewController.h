//
//  GameViewController.h
//  YJJ_Demo
//

//  Copyright (c) 2017年 YJJ_lxh. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <SceneKit/SceneKit.h>
#import "pointModel.h"
#import "mapDataModel.h"
#import "DataPool.h"

@interface GameViewController : UIViewController
{
    DataPool *dataPool;
    SCNNode *carnode;
    SCNNode *nodeCarema;
    SCNNode *traceNode;
    
    int mCurPathIndex;
    float mRotateAngle;
    
    SCNVector3 mCameraDes;
    SCNVector3 mCameraPos;
    
    SCNNode* mEyeBaseNode;
}
@property (nonatomic,strong) SCNScene *scene;//存放元素节点
@property (nonatomic,strong) SCNView *sceneView;//用来展示3D图形的控件

/*****************
 *获取场地信息
 ****************/
-(void) initData;
@end
