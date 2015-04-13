package com.ankamagames.wakfu.client.core.world.dynamicElement;

import com.ankamagames.baseImpl.graphics.game.DynamicElement.*;

public class SimpleDynamicElementTypeProvider extends WakfuDynamicElementTypeProvider
{
    @Override
    public void initialize(final DynamicElement elt) {
        elt.setAnimation(elt.getBaseAnimation());
    }
    
    @Override
    public void clear(final DynamicElement elt) {
    }
}
