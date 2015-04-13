package com.ankamagames.baseImpl.graphics.isometric.text;

import com.ankamagames.framework.graphics.engine.*;

public interface DrawedBackground
{
    int getLeftMargin();
    
    int getTopMargin();
    
    int getRightMargin();
    
    int getBottomMargin();
    
    float[][] getVerticesAdjustement();
    
    float[][] getVerticesWidthAndHeight();
    
    IndexBuffer getVerticesIndex();
    
    IndexBuffer getBorderVerticesIndex();
}
