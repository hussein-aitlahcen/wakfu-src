package com.ankamagames.xulor2.appearance;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.decorator.mesh.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class ColorElement extends AbstractSwitch implements Releasable
{
    private static Logger m_logger;
    public static final String TAG = "Color";
    public static final String ALTERNATIVE_TAG = "NamedColor";
    private Color m_color;
    private GradientBackgroundMesh.GradientBackgroundColorAlign m_position;
    private String m_name;
    private static final ObjectPool m_pool;
    public static final int COLOR_HASH;
    public static final int NAME_HASH;
    public static final int POSITION_HASH;
    
    public ColorElement() {
        super();
        this.m_color = null;
        this.m_position = null;
        this.m_name = null;
    }
    
    public static ColorElement checkOut() {
        ColorElement c;
        try {
            c = (ColorElement)ColorElement.m_pool.borrowObject();
            c.m_currentPool = ColorElement.m_pool;
        }
        catch (Exception e) {
            ColorElement.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            c = new ColorElement();
            c.onCheckOut();
        }
        return c;
    }
    
    @Override
    public String getTag() {
        return "Color";
    }
    
    public void setColor(final Color c) {
        if (this.m_color == c) {
            return;
        }
        this.m_color = c;
        this.dispatchEvent(new ColorChangedEvent(this));
    }
    
    public Color getColor() {
        return this.m_color;
    }
    
    public GradientBackgroundMesh.GradientBackgroundColorAlign getPosition() {
        return this.m_position;
    }
    
    public void setPosition(final GradientBackgroundMesh.GradientBackgroundColorAlign align) {
        this.m_position = align;
        this.dispatchEvent(new ColorChangedEvent(this));
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public void setName(final String name) {
        this.m_name = name;
        this.dispatchEvent(new ColorChangedEvent(this));
    }
    
    @Override
    public void setup(final SwitchClient e) {
        if (e instanceof ColorClient) {
            ((ColorClient)e).setColor(this.m_color, this.m_name);
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_color = null;
        this.m_name = null;
        this.m_position = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
    }
    
    @Override
    public void copyElement(final BasicElement c) {
        final ColorElement e = (ColorElement)c;
        super.copyElement(e);
        e.setColor(this.getColor());
        e.m_name = this.m_name;
        e.m_position = this.m_position;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ColorElement.COLOR_HASH) {
            this.setColor(cl.convertToColor(value));
        }
        else if (hash == ColorElement.NAME_HASH) {
            this.setName(cl.convertToString(value, this.m_elementMap));
        }
        else {
            if (hash != ColorElement.POSITION_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setPosition(GradientBackgroundMesh.GradientBackgroundColorAlign.value(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == ColorElement.COLOR_HASH) {
            this.setColor((Color)value);
        }
        else if (hash == ColorElement.NAME_HASH) {
            this.setName(String.valueOf(value));
        }
        else {
            if (hash != ColorElement.POSITION_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setPosition((GradientBackgroundMesh.GradientBackgroundColorAlign)value);
        }
        return true;
    }
    
    static {
        ColorElement.m_logger = Logger.getLogger((Class)ColorElement.class);
        m_pool = new MonitoredPool(new ObjectFactory<ColorElement>() {
            @Override
            public ColorElement makeObject() {
                return new ColorElement();
            }
        }, 800);
        COLOR_HASH = "color".hashCode();
        NAME_HASH = "name".hashCode();
        POSITION_HASH = "position".hashCode();
    }
}
