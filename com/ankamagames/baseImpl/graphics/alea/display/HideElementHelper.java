package com.ankamagames.baseImpl.graphics.alea.display;

public class HideElementHelper
{
    public static final byte STATE_HIDING = 0;
    public static final byte STATE_HIDDEN = 1;
    public static final byte STATE_UNHIDING = 2;
    public static final byte STATE_VISIBLE = 3;
    
    public static byte computeColor(final byte state, final float[] m_tempColor) {
        switch (state) {
            case 0: {
                HiddenElementManager.getInstance().computeColor(m_tempColor);
                if (!HiddenElementManager.getInstance().isInTransition()) {
                    return 1;
                }
                break;
            }
            case 2: {
                HiddenElementManager.getInstance().computeColor(m_tempColor);
                if (!HiddenElementManager.getInstance().isInTransition()) {
                    return 3;
                }
                break;
            }
            case 1: {
                HiddenElementManager.getInstance().computeColor(m_tempColor);
                return state;
            }
        }
        return state;
    }
}
