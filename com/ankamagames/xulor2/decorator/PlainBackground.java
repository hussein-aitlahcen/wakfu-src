package com.ankamagames.xulor2.decorator;

import org.apache.log4j.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.decorator.mesh.*;

public class PlainBackground extends Background implements ModulationColorClient
{
    private static Logger m_logger;
    public static final String TAG = "PlainBackground";
    private PlainBackgroundMesh m_mesh;
    public static final int COLOR_HASH;
    
    @Override
    public void addFromXML(final EventDispatcher element) {
        super.addFromXML(element);
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof ColorElement) {
            e.addEventListener(Events.COLOR_CHANGED, new EventListener() {
                @Override
                public boolean run(final Event event) {
                    PlainBackground.this.setColor(((ColorChangedEvent)event).getColorElement().getColor());
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
        return "PlainBackground";
    }
    
    @Override
    public void copyElement(final BasicElement b) {
        final PlainBackground pb = (PlainBackground)b;
        pb.setColor(this.getColor());
        super.copyElement(b);
    }
    
    public void setColor(final ColorElement colorElement) {
        this.setColor(colorElement.getColor());
    }
    
    public void setColor(final Color color) {
        if (color != null) {
            this.getMesh().setColor(color);
        }
    }
    
    public Color getColor() {
        return this.getMesh().getColor();
    }
    
    @Override
    public PlainBackgroundMesh getMesh() {
        return this.m_mesh;
    }
    
    @Override
    public Entity getEntity() {
        return this.getMesh().getEntity();
    }
    
    @Override
    public boolean isValidAdd(final BasicElement e) {
        if (e instanceof ColorElement && ((ColorElement)e).getColor() == null) {
            PlainBackground.m_logger.error((Object)"Tentative d'ajout d'un ColorElement sans couleur");
            return false;
        }
        return super.isValidAdd(e);
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
    public void onCheckOut() {
        super.onCheckOut();
        (this.m_mesh = new PlainBackgroundMesh()).onCheckOut();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_mesh.onCheckIn();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == PlainBackground.COLOR_HASH) {
            this.setColor(cl.convertToColor(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == PlainBackground.COLOR_HASH) {
            this.setColor((Color)value);
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    @Override
    public void setModulationColor(final Color c) {
        this.m_mesh.setModulationColor(c);
    }
    
    @Override
    public Color getModulationColor() {
        return this.m_mesh.getModulationColor();
    }
    
    static {
        PlainBackground.m_logger = Logger.getLogger((Class)PlainBackground.class);
        COLOR_HASH = "color".hashCode();
    }
}
