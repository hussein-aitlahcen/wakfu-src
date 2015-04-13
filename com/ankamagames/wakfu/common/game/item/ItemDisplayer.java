package com.ankamagames.wakfu.common.game.item;

public interface ItemDisplayer
{
    String[] getFields();
    
    Object getFieldValue(Item p0, String p1);
    
    String getIconUrl(Item p0);
    
    String getName(Item p0);
    
    String getDescription(Item p0);
    
    void resetCache(Item p0);
    
    void cleanUp(Item p0);
}
