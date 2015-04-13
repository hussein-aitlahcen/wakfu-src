package com.ankamagames.wakfu.client.core.game.collector.ui;

import com.ankamagames.framework.reflect.*;

public interface CollectorContentView extends FieldProvider
{
    int getMaxPlayerQuantity();
    
    int getTotalPlayerQuantity();
    
    int getNeededQuantity();
    
    int getMaxQuantity();
    
    int getCurrentQuantity();
    
    int getCurrentPlayerQuantity();
    
    void setCurrentPlayerQuantity(int p0);
    
    boolean canMax();
}
