//
//  cdNodeObjsModel.m
//  openGlText
//
//  Created by wagn on 22/03/2017.
//  Copyright © 2017 wagn. All rights reserved.
//

#import "cdNodeObjsModel.h"
#import "pointModel.h"

@implementation cdNodeObjsModel

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{@"leftPoints"  : [pointModel class],
             @"rightPoints" : [pointModel class],
             };
}
@end
