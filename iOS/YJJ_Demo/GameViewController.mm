//
//  GameViewController.m
//  YJJ_Demo
//
//  Created by YJJ_lxh on 17/4/7.
//  Copyright (c) 2017年 YJJ_lxh. All rights reserved.
//

#import "GameViewController.h"
#import <ModelIO/MDLAsset.h>
#import <ModelIO/ModelIO.h>
#import <SceneKit/ModelIO.h>
#import "YYModel.h"
#import "CarTraceData.h"
#include <vector>
#import "AAPLOverlayScene.h"
using namespace std;

//三角形片顶点，索引
extern vector<SCNVector3> g_Vertices;
extern vector<int> g_Indices;

//白色边线顶点，索引
extern vector<SCNVector3> m_whiteLineVertices;
extern vector<int> m_whiteLineIndices;

//黄色边线顶点，索引
extern vector<SCNVector3> m_yellowLineVertices;
extern vector<int> m_yellowLineIndices;

//轨迹面片定点，索引，轨迹数据管理器
extern vector<SCNVector3> g_TraceVertices;
extern vector<int> g_TraceIndices;
CarTraceData* mCarTraceData;


@implementation GameViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    mCarTraceData = [CarTraceData alloc];
    
    mCurPathIndex = 0;
    mRotateAngle = 0;
    
    //获取场地，轨迹数据到数据池
    self.initData;
    
    self.sceneView = [[SCNView alloc]initWithFrame:self.view.bounds];
    self.sceneView.observationInfo ;
    self.sceneView.center = self.view.center;
//    self.sceneView.backgroundColor = [UIColor blackColor];
    self.sceneView.autoenablesDefaultLighting = YES;
    self.sceneView.overlaySKScene = [[AAPLOverlayScene alloc] initWithSize:self.view.bounds.size];
//    统计面板
    self.sceneView.showsStatistics = NO;
    
    [self.view addSubview:self.sceneView];
    
//        scnView.overlaySKScene = [[AAPLOverlayScene alloc] initWithSize:scnView.bounds.size];
    
    //三维场景初始化
    self.scene = [SCNScene scene];
    
    //地面节点初始化
    SCNBox* box = [SCNBox boxWithWidth:10 height:10 length:0.003 chamferRadius:0];
    SCNNode* floor = [SCNNode node];
    floor.geometry = box;
    floor.position = SCNVector3Make(0, 0, -0.1);
    SCNMaterial* MaterialFloor = [SCNMaterial new];
    [MaterialFloor setDoubleSided:true];
    MaterialFloor.diffuse.contents =[UIColor colorWithRed:0 green:0.35 blue:0 alpha:1];
    box.firstMaterial = MaterialFloor;
    [self.scene.rootNode addChildNode:floor];
    
    
    //相机目标节点初始化
    mEyeBaseNode = [SCNNode node];
    SCNSphere* sphere = [SCNSphere sphereWithRadius:0.002];
    SCNMaterial* MaterialShere = [SCNMaterial new];
    [MaterialShere setDoubleSided:true];
    MaterialShere.diffuse.contents =[UIColor colorWithRed:1 green:0 blue:0 alpha:1];
    sphere.firstMaterial = MaterialShere;
    mEyeBaseNode.geometry = sphere;
    mEyeBaseNode.position = SCNVector3Make(0, 0, 0);
    [self.scene.rootNode addChildNode:mEyeBaseNode];
    
    //添加一个环境光
    SCNNode *nodeLight = [SCNNode node];
    nodeLight.light = [SCNLight light];
    nodeLight.light.type = SCNLightTypeAmbient;
    nodeLight.light.color = [UIColor colorWithWhite:0.4 alpha:1.0];
    [self.scene.rootNode addChildNode:nodeLight];
    
    //增加一个副光
    SCNNode *nodeLight2 = [SCNNode node];
    nodeLight2.light = [SCNLight light];
    nodeLight2.light.type = SCNLightTypeOmni;
    nodeLight2.light.color = [UIColor colorWithWhite:0.75 alpha:1];
    nodeLight2.position = SCNVector3Make(0, 50, 50);
    [self.scene.rootNode addChildNode:nodeLight2];
    
    //添加摄像机节点
    nodeCarema = [SCNNode node];
    SCNCamera* ccCamera = [SCNCamera camera];
    ccCamera.xFov = 3;
    ccCamera.yFov = 6;
    nodeCarema.camera = ccCamera;
    nodeCarema.position = SCNVector3Make(0, 0, 100);
    [mEyeBaseNode addChildNode:nodeCarema];
    
    
//    for test
//    nodeCarema.position = [dataPool getCenterOfArea];
//    mCameraPos = nodeCarema.position;
//
//    SCNMatrix4 projection = nodeCarema.camera.projectionTransform;
//    SCNMatrix4 mmm =nodeCarema.transform;
////    SCNMatrix4 view = SCNMatrix4Invert(nodeCarema.transform);
//    
////    SCNMatrix4 m2 = SCNMatrix4MakeTranslation(0, 0, -15);
////    
////    SCNMatrix4 m3 = SCNMatrix4MakeRotation(3.1415926/4.0, 0, 0, 1);
////    m2 = SCNMatrix4Mult(m3, m2);
////    SCNMatrix4 viewProjection = SCNMatrix4Mult(m2, projection);
//        SCNMatrix4 viewProjection = SCNMatrix4Mult(mmm, projection);
//    [nodeCarema.camera setProjectionTransform:viewProjection];
    
//    [self.scene.rootNode addChildNode:nodeCarema];
//    end test
    
    //汽车模型加载
    NSString* resPath = [[NSBundle mainBundle] resourcePath];
    NSString * str2 = @"/art.scnassets/yjjcar.obj";
    NSString *string = [resPath stringByAppendingString:str2];
    NSURL* nsurl = [NSURL fileURLWithPath:string];
    MDLAsset *asset = [[MDLAsset alloc] initWithURL:nsurl];
    carnode = [SCNNode nodeWithMDLObject:[asset objectAtIndex:0]];
    [self.scene.rootNode addChildNode:carnode];
    //绘制场地
    self.drawArea;
    
//    空间文字
//    UIFont* font = [UIFont systemFontOfSize:2];
//    SCNText* text = [SCNText textWithString: @"hello,狗带" extrusionDepth: 0.2];
//    text.font = font;
//    // text.firstMaterial.diffuse.contents = [UIColor yellowColor];
//    SCNNode* textNode = [SCNNode nodeWithGeometry: text];
//    textNode.position = SCNVector3Make(0, 0, 0);
//    [self.scene.rootNode addChildNode:textNode];
    
//    self.scene.overlaySKScene = nullptr;
    
    self.sceneView.scene = self.scene;
    
    //计时器帧刷新控制
    [NSTimer scheduledTimerWithTimeInterval:0.005 target:self selector:@selector(actionTimer:) userInfo:nil repeats:YES];
//    CGRect frame = CGRectMake(0, 30, 60, 0);
//    UIButton *btn = [UIButton buttonWithType:UIButtonTypeRoundedRect];
//    btn.frame = frame;
//    btn.backgroundColor  = [UIColor clearColor];
//    [btn setTitle:@"动态添加一个按钮!" forState:UIControlStateNormal];
//    [btn addTarget:self action:@selector(Edit:) forControlEvents:UIControlEventTouchUpInside];
//    [self.view addSubview:btn];
}

-(IBAction)Edit:(id)sender{
    NSString *msg = @"f*au*ck U";
    
    NSLog(@"ddddddd111111");
}

- (void)actionTimer:(NSTimer *)timer
{
    mCurPathIndex++;
    if (mCurPathIndex>=[dataPool getPathData].Points.count) {
        mCurPathIndex = 0;
//        [mCarTraceData resetTraceData];
    }

    pointModel* curPt = [dataPool getPathData].Points[mCurPathIndex];
    
    if(curPt.ErrContent.length>2)
        NSLog(curPt.ErrContent);
    
    [mCarTraceData addTracePoint:{curPt.x/100,curPt.y/100,curPt.z/100}];
    
    carnode.transform = SCNMatrix4MakeTranslation(0, 0, 0);
    
    carnode.scale = SCNVector3Make(0.01, 0.01, 0.01);

    carnode.transform = SCNMatrix4Rotate(carnode.transform, 3.1415/2, 1, 0, 0);
    carnode.transform = SCNMatrix4Rotate(carnode.transform, curPt.CarDir/360.0*3.141592653*2, 0, 0, 1);
    carnode.position = SCNVector3Make(curPt.x/100, curPt.y/100, curPt.z/100);
    
    SCNVector3 vvv = nodeCarema.position;
    if (vvv.z>10) {
        nodeCarema.position = SCNVector3Make(vvv.x, vvv.y, vvv.z-0.4);
    }else
    {
        self.sceneView.allowsCameraControl = YES;
    }
    mEyeBaseNode.position = carnode.position;
    
    CABasicAnimation* aa;
}

- (void) handleTap:(UIGestureRecognizer*)gestureRecognize
{
    // retrieve the SCNView
    SCNView *scnView = (SCNView *)self.view;
    
    // check what nodes are tapped
    CGPoint p = [gestureRecognize locationInView:scnView];
    NSArray *hitResults = [scnView hitTest:p options:nil];
    
    // check that we clicked on at least one object
    if([hitResults count] > 0){
        // retrieved the first clicked object
        SCNHitTestResult *result = [hitResults objectAtIndex:0];
        
        // get its material
        SCNMaterial *material = result.node.geometry.firstMaterial;
        
        // highlight it
        [SCNTransaction begin];
        [SCNTransaction setAnimationDuration:0.5];
        
        // on completion - unhighlight
        [SCNTransaction setCompletionBlock:^{
            [SCNTransaction begin];
            [SCNTransaction setAnimationDuration:0.5];
            
            material.emission.contents = [UIColor blackColor];
            
            [SCNTransaction commit];
        }];
        
        material.emission.contents = [UIColor redColor];
        
        [SCNTransaction commit];
    }
}

- (BOOL)shouldAutorotate
{
    return YES;
}

- (BOOL)prefersStatusBarHidden {
    return YES;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
        return UIInterfaceOrientationMaskAllButUpsideDown;
    } else {
        return UIInterfaceOrientationMaskAll;
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

-(void) initData
{
    dataPool = [[DataPool alloc] init];
    [dataPool getResData];
}

-(void) calTriangles
{
    
}

-(void) drawArea
{
    SCNGeometrySource *TraceVertexSource = [SCNGeometrySource geometrySourceWithVertices:&g_TraceVertices[0]
                                                                              count:g_TraceVertices.size()];
    NSData *TraceIndexData = [NSData dataWithBytes:&g_TraceIndices[0]
                                       length:g_TraceIndices.size()*4];
    SCNGeometryElement *TraceElement = [SCNGeometryElement geometryElementWithData:TraceIndexData
                                                                primitiveType:SCNGeometryPrimitiveTypeTriangles
                                                               primitiveCount:g_TraceIndices.size()/3
                                                                bytesPerIndex:sizeof(int)];
    SCNGeometry *TraceTriangles = [SCNGeometry geometryWithSources:@[TraceVertexSource]
                                                     elements:@[TraceElement]];
    SCNMaterial* myTraceMaterial = [SCNMaterial new];
    [myTraceMaterial setDoubleSided:true];
    myTraceMaterial.diffuse.contents =[UIColor redColor];
    TraceTriangles.firstMaterial = myTraceMaterial;
    
    traceNode = [SCNNode nodeWithGeometry:TraceTriangles];
    [self.scene.rootNode addChildNode:traceNode];
    
    
    SCNGeometrySource *vertexSource = [SCNGeometrySource geometrySourceWithVertices:&g_Vertices[0]
                                                                              count:g_Vertices.size()];
    NSData *indexData = [NSData dataWithBytes:&g_Indices[0]
                                       length:g_Indices.size()*4];
    SCNGeometryElement *element = [SCNGeometryElement geometryElementWithData:indexData
                                                                primitiveType:SCNGeometryPrimitiveTypeTriangles
                                                               primitiveCount:g_Indices.size()/3
                                                                bytesPerIndex:sizeof(int)];
    
    SCNGeometry *triangles = [SCNGeometry geometryWithSources:@[vertexSource]
                                                elements:@[element]];
    
    SCNMaterial* myMaterial = [SCNMaterial new];
    [myMaterial setDoubleSided:true];
    myMaterial.diffuse.contents =[UIColor darkGrayColor];
    
    triangles.firstMaterial = myMaterial;
    
    SCNNode *trianglesNode = [SCNNode nodeWithGeometry:triangles];
    [self.scene.rootNode addChildNode:trianglesNode];
    
    //white line
    SCNGeometrySource *vertexSource2 = [SCNGeometrySource geometrySourceWithVertices:&m_whiteLineVertices[0]
                                                                              count:m_whiteLineVertices.size()];
    NSData *indexData2 = [NSData dataWithBytes:&m_whiteLineIndices[0]
                                        length:m_whiteLineIndices.size()*4];
    SCNGeometryElement *element2 = [SCNGeometryElement geometryElementWithData:indexData2
                                                                 primitiveType:SCNGeometryPrimitiveTypeLine
                                                                primitiveCount:m_whiteLineIndices.size()/2
                                                                bytesPerIndex:sizeof(int)];

    SCNGeometry *line = [SCNGeometry geometryWithSources:@[vertexSource2]
                                                    elements:@[element2]];
    
    SCNNode *lineNode = [SCNNode nodeWithGeometry:line];
    
    [self.scene.rootNode addChildNode:lineNode];
    
    
    //yellow line
    SCNGeometrySource *vertexSourceYellow = [SCNGeometrySource geometrySourceWithVertices:&m_yellowLineVertices[0]
                                                                               count:m_yellowLineVertices.size()];
    NSData *indexDataYellow = [NSData dataWithBytes:&m_yellowLineIndices[0]
                                        length:m_yellowLineIndices.size()*4];
    SCNGeometryElement *elementYellow = [SCNGeometryElement geometryElementWithData:indexDataYellow
                                                                 primitiveType:SCNGeometryPrimitiveTypeLine
                                                                primitiveCount:m_yellowLineIndices.size()/2
                                                                 bytesPerIndex:sizeof(int)];
    
    SCNGeometry *lineYellow = [SCNGeometry geometryWithSources:@[vertexSourceYellow]
                                                elements:@[elementYellow]];
    SCNMaterial* MaterialYellow = [SCNMaterial new];
    [MaterialYellow setDoubleSided:true];
    MaterialYellow.diffuse.contents =[UIColor yellowColor];
    lineYellow.firstMaterial = MaterialYellow;
    
    SCNNode *lineYellowNode = [SCNNode nodeWithGeometry:lineYellow];
    
    [self.scene.rootNode addChildNode:lineYellowNode];
    
}

@end
