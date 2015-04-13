package com.ankamagames.xulor2.util;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class XulorUtil
{
    public static String generatePreferenceKey(final String elementMapId, final String id, final String key) {
        final StringBuilder sb = new StringBuilder();
        sb.append(elementMapId).append('.').append(id).append('.').append(key);
        return sb.toString();
    }
    
    public static String convertToTextBuilderColor(final float[] color) {
        final int r = (int)(color[0] * 255.0f);
        final int g = (int)(color[1] * 255.0f);
        final int b = (int)(color[2] * 255.0f);
        final StringBuilder sb = new StringBuilder();
        if (r < 16) {
            sb.append('0');
        }
        sb.append(Integer.toHexString(r));
        if (g < 16) {
            sb.append('0');
        }
        sb.append(Integer.toHexString(g));
        if (b < 16) {
            sb.append('0');
        }
        sb.append(Integer.toHexString(b));
        return sb.toString();
    }
    
    public static void setWidgetInScreen(final Widget w) {
        final int maxX = (int)(Xulor.getInstance().getScene().getFrustumWidth() - w.getWidth());
        final int maxY = (int)(Xulor.getInstance().getScene().getFrustumHeight() - w.getHeight());
        final int x = MathHelper.clamp(w.getX(), 0, maxX);
        final int y = MathHelper.clamp(w.getY(), 0, maxY);
        w.setPosition(x, y);
    }
}
