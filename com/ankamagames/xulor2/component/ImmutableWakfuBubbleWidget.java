package com.ankamagames.xulor2.component;

import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.xulor2.component.bubble.bubbleStyleTransformers.*;

public class ImmutableWakfuBubbleWidget extends WakfuBubbleWidget
{
    public static final String TAG = "immutableWakfuBubble";
    
    @Override
    protected void applyDirection() {
    }
    
    protected ObjectPair<WakfuBubbleStyleTransformer, String> getWakfuBubbleStyleTransformer(final String message) {
        final ObjectPair returnValue = new ObjectPair();
        returnValue.setFirst(new WakfuBubbleStyleTransformersLibrary.StandartBubbleStyleTransformer());
        returnValue.setSecond(message);
        return (ObjectPair<WakfuBubbleStyleTransformer, String>)returnValue;
    }
    
    @Override
    protected void transform() {
    }
}
