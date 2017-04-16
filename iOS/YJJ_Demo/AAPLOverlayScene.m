//
//  AAPLOverlayScene.m
//  YJJ_Demo
//
//  Created by YJJ_lxh on 17/4/16.
//  Copyright © 2017年 YJJ_lxh. All rights reserved.
//

#import "AAPLOverlayScene.h"

@implementation AAPLOverlayScene
-(id)initWithSize:(CGSize)size {
    if (self = [super initWithSize:size]) {
        //setup the overlay scene
        self.anchorPoint = CGPointMake(0.5, 0.5);
        
        //automatically resize to fill the viewport
        self.scaleMode = SKSceneScaleModeResizeFill;
        
        //make UI larger on iPads
        BOOL iPad = ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPad);
        float scale = iPad ? 1.5 : 1;
        
        //add the speed gauge
        SKSpriteNode *myImage = [SKSpriteNode spriteNodeWithImageNamed:@"speedGauge.png"];
        myImage.anchorPoint = CGPointMake(0.5, 0);
        myImage.position = CGPointMake(size.width*0.33, -size.height*0.5);
        myImage.xScale = 0.8 * scale;
        myImage.yScale = 0.8 * scale;
        [self addChild:myImage];
        
        //add the needed
        SKNode *needleHandle = [SKNode node];
        SKSpriteNode *needle = [SKSpriteNode spriteNodeWithImageNamed:@"needle.png"];
        needleHandle.position = CGPointMake(0, 16);
        needle.anchorPoint = CGPointMake(0.5, 0);
        needle.xScale = 0.7;
        needle.yScale = 0.7;
        needle.zRotation = M_PI_2;
        [needleHandle addChild:needle];
        [myImage addChild:needleHandle];
        
        _speedNeedle = needleHandle;
        
    }
    return self;
}
@end
