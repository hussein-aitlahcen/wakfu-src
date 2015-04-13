package com.ankamagames.xulor2.decorator;

import org.apache.log4j.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.decorator.mesh.*;

public class PlainBorder extends InsetsBorder implements ModulationColorClient
{
    private static Logger m_logger;
    public static final String TAG = "PlainBorder";
    private PlainBorderMesh m_mesh;
    public static final int COLOR_HASH;
    
    public PlainBorder() {
        super();
        this.m_mesh = new PlainBorderMesh();
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof ColorElement) {
            e.addEventListener(Events.COLOR_CHANGED, new EventListener() {
                @Override
                public boolean run(final Event event) {
                    PlainBorder.this.setColor(((ColorChangedEvent)event).getColorElement());
                    return false;
                }
            }, false);
            this.setColor((ColorElement)e);
            this.cleanColorsInChildren((ColorElement)e);
        }
        super.add(e);
    }
    
    @Override
    public String getTag() {
        return "PlainBorder";
    }
    
    @Override
    public boolean isValidAdd(final BasicElement e) {
        if (e instanceof ColorElement && ((ColorElement)e).getColor() == null) {
            PlainBorder.m_logger.error((Object)"Tentative d'ajout d'un ColorElement sans couleur");
            return false;
        }
        return super.isValidAdd(e);
    }
    
    @Override
    public PlainBorderMesh getMesh() {
        return this.m_mesh;
    }
    
    @Override
    public Entity getEntity() {
        return this.m_mesh.getEntity();
    }
    
    public void setColor(final ColorElement c) {
        this.setColor(c.getColor());
    }
    
    public void setColor(final Color color) {
        this.m_mesh.setColor(color);
    }
    
    public Color getColor() {
        return this.m_mesh.getColor();
    }
    
    @Override
    public void setModulationColor(final Color c) {
        this.m_mesh.setModulationColor(c);
    }
    
    @Override
    public Color getModulationColor() {
        return this.m_mesh.getModulationColor();
    }
    
    protected void cleanColorsInChildren(final ColorElement c) {
        if (this.m_children == null) {
            return;
        }
        for (int i = this.m_children.size() - 1; i >= 0; --i) {
            final EventDispatcher e = this.m_children.get(i);
            if (e instanceof ColorElement) {
                this.destroy(e);
            }
        }
    }
    
    @Override
    public void copyElement(final BasicElement b) {
        super.copyElement(b);
        final PlainBorder pb = (PlainBorder)b;
        pb.setColor(this.m_mesh.getColor());
        pb.setModulationColor(this.getModulationColor());
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_mesh.onCheckIn();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_mesh.onCheckOut();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == PlainBorder.COLOR_HASH) {
            this.setColor(cl.convertToColor(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == PlainBorder.COLOR_HASH) {
            if (value != null) {
                this.setColor((Color)value);
            }
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        PlainBorder.m_logger = Logger.getLogger((Class)PlainBorder.class);
        COLOR_HASH = "color".hashCode();
    }
}
