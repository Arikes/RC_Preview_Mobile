//
//  pathDataModel.m
//  Preview3D
//
//  Created by 北京易驾佳 on 2017/3/23.
//  Copyright © 2017年 北京易驾佳. All rights reserved.
//
#import "pointModel.h"
#import "pathDataModel.h"

@implementation pathDataModel
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{@"Points"  : [pointModel class]
             };
}
@end
