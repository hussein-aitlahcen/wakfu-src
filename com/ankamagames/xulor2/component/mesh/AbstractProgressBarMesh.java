package com.ankamagames.xulor2.component.mesh;

import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.util.alignment.*;
import java.awt.*;

public abstract class AbstractProgressBarMesh implements ModulationColorClient
{
    protected Color m_modulationColor;
    
    public AbstractProgressBarMesh() {
        super();
        this.m_modulationColor = null;
    }
    
    @Override
    public void setModulationColor(final Color c) {
        if (this.m_modulationColor == c) {
            return;
        }
        this.m_modulationColor = c;
    }
    
    @Override
    public Color getModulationColor() {
        return this.m_modulationColor;
    }
    
    public abstract void setGeometry(final int p0, final int p1, final int p2, final int p3, final float p4);
    
    public abstract void setPixmaps(final Pixmap p0, final Pixmap p1, final Pixmap p2, final Pixmap p3, final Pixmap p4, final Pixmap p5, final Pixmap p6, final Pixmap p7, final Pixmap p8);
    
    public abstract Entity getEntity();
    
    public abstract void onCheckOut();
    
    public void onCheckIn() {
        this.m_modulationColor = null;
    }
    
    public abstract void setHorizontal(final boolean p0);
    
    public abstract void setColor(final Color p0);
    
    public abstract Color getColor();
    
    public abstract void setBorderColor(final Color p0);
    
    public abstract Color getBorderColor();
    
    public abstract void setFullCirclePercentage(final float p0);
    
    public abstract float getFullCirclePercentage();
    
    public abstract void setDeltaAngle(final float p0);
    
    public abstract float getDeltaAngle();
    
    public abstract void setPosition(final Alignment9 p0);
    
    public abstract Alignment9 getPosition();
    
    public abstract void setBorder(final Insets p0);
    
    public abstract Insets getBorder();
}
