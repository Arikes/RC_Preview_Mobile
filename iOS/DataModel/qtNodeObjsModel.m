//
//  qtNodeObjsModel.m
//  openGlText
//
//  Created by wagn on 22/03/2017.
//  Copyright © 2017 wagn. All rights reserved.
//

#import "qtNodeObjsModel.h"
#import "pointModel.h"

@implementation qtNodeObjsModel
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{@"Points" : [pointModel class]};
}

-(void) FrameMove
{
    NSLog(@"hello,QT FrameMove");
}
@end
