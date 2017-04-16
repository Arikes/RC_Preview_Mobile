//
//  Line2d.h
//  YJJ_Demo
//
//  Created by YJJ_lxh on 17/4/15.
//  Copyright © 2017年 YJJ_lxh. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <SceneKit/SceneKit.h>

@interface Line2d : NSObject
@property (nonatomic) CGPoint begin;
@property (nonatomic) CGPoint end;
@property (nonatomic,retain) UIColor* linecolor;
@end
