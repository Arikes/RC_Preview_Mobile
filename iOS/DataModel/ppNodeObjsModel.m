//
//  ppNodeObjsModel.m
//  openGlText
//
//  Created by wagn on 22/03/2017.
//  Copyright © 2017 wagn. All rights reserved.
//

#import "ppNodeObjsModel.h"
#import "pointModel.h"

@implementation ppNodeObjsModel

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{@"Points" : [pointModel class]};
}
@end
