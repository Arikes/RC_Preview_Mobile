//
//  qtNodeObjsModel.h
//  openGlText
//
//  Created by wagn on 22/03/2017.
//  Copyright © 2017 wagn. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface qtNodeObjsModel : NSObject
@property (nonatomic, copy)    NSString*  code;
@property (nonatomic, strong)  NSArray*   Points;

-(void) FrameMove;

@end
