package com.ankamagames.baseImpl.graphics.isometric.maskableLayer;

import com.ankamagames.baseImpl.graphics.alea.display.*;

public final class MaskableHelper
{
    private static final int UNDEFINED_MASK = 0;
    private static final byte UNDEFINED_LAYER = -1;
    
    public static void setUndefined(final Maskable element) {
        element.setMaskKey(0, (short)(-1));
    }
    
    public static boolean isUndefined(final Maskable element) {
        return element.getLayerId() == -1;
    }
    
    public static void set(final Maskable maskable, final DisplayedScreenElement element) {
        if (element == null) {
            setUndefined(maskable);
        }
        else {
            maskable.setMaskKey(element.getMaskKey(), element.getLayerId());
        }
    }
}
