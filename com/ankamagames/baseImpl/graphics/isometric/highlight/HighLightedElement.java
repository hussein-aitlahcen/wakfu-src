package com.ankamagames.baseImpl.graphics.isometric.highlight;

public interface HighLightedElement extends HighLightMeshTransformer
{
    long getLayerReference();
    
    boolean isHollow();
}
