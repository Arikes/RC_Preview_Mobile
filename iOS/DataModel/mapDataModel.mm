//
//  mapDataModel.m
//  openGlText
//
//  Created by wagn on 22/03/2017.
//  Copyright © 2017 wagn. All rights reserved.
//
#import "mapDataModel.h"
#import "qtNodeObjsModel.h"
#import "ppNodeObjsModel.h"
#import "rpNodeObjsModel.h"
#import "cdNodeObjsModel.h"
#import "spNodeObjsModel.h"
#import "spsubNodeObjsModel.h"
#import "pointModel.h"
#import <vector>
#import <map>
#import <string>
using namespace std;
@implementation mapDataModel
{
}

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{@"qtNodeObjs" : [qtNodeObjsModel class],
             @"ppNodeObjs" : [ppNodeObjsModel class],
             @"rpNodeObjs" : [rpNodeObjsModel class],
             @"cdNodeObjs" : [cdNodeObjsModel class],
             @"spNodeObjs" : [spNodeObjsModel class],
             @"spsubNodeObjs" : [spsubNodeObjsModel class]
             };
}

@end
