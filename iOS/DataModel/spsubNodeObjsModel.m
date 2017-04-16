//
//  spsubNodeObjsModel.m
//  openGlText
//
//  Created by wagn on 22/03/2017.
//  Copyright © 2017 wagn. All rights reserved.
//

#import "spsubNodeObjsModel.h"
#import "pointModel.h"

@implementation spsubNodeObjsModel

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{@"Points" : [pointModel class]};
}
@end
