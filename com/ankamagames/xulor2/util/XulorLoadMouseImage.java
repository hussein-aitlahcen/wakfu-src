package com.ankamagames.xulor2.util;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.alignment.*;

public class XulorLoadMouseImage implements XulorLoadUnload
{
    public Widget WIDGET;
    public int XOFFSET;
    public int YOFFSET;
    public Alignment9 HOTPOINT;
    
    public XulorLoadMouseImage(final Widget widget, final int xOffset, final int yOffset, final Alignment9 hotpoint) {
        super();
        this.WIDGET = null;
        this.XOFFSET = 0;
        this.YOFFSET = 0;
        this.HOTPOINT = Alignment9.SOUTH_EAST;
        this.WIDGET = widget;
        this.XOFFSET = xOffset;
        this.YOFFSET = yOffset;
        this.HOTPOINT = hotpoint;
    }
}
