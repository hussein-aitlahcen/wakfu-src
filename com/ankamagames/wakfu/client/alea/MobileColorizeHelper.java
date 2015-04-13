package com.ankamagames.wakfu.client.alea;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;

public class MobileColorizeHelper
{
    private static final Logger m_logger;
    public static final Material m_tempColor;
    private static final float HOVER_BOOST = 0.0f;
    private static final float SELECTED_BOOST = 0.1f;
    
    public static void onHover(final AnimatedInteractiveElement element) {
        colorize(element, 0.0f);
    }
    
    public static void onSelected(final AnimatedInteractiveElement element) {
        colorize(element, 0.1f);
    }
    
    public static void onLeave(final AnimatedInteractiveElement element) {
        element.resetColor();
    }
    
    private static void colorize(final AnimatedInteractiveElement element, final float boost) {
        final float[] c = element.getAnm().getHighlightColor();
        MobileColorizeHelper.m_tempColor.setSpecularColor(c[0] + boost, c[1] + boost, c[2] + boost);
        element.colorize(MobileColorizeHelper.m_tempColor);
    }
    
    static {
        m_logger = Logger.getLogger((Class)MobileColorizeHelper.class);
        m_tempColor = Material.Factory.newInstance();
    }
}
