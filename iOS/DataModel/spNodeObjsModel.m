//
//  spNodeObjsModel.m
//  openGlText
//
//  Created by wagn on 22/03/2017.
//  Copyright © 2017 wagn. All rights reserved.
//

#import "spNodeObjsModel.h"
#import "pointModel.h"

@implementation spNodeObjsModel

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{@"rightWall" : [pointModel class],
             @"leftWall"  : [pointModel class]};
}
@end
