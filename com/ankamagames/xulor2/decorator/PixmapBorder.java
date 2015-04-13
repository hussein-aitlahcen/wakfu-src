package com.ankamagames.xulor2.decorator;

import org.apache.log4j.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.decorator.mesh.*;
import com.ankamagames.xulor2.util.alignment.*;

public class PixmapBorder extends InsetsBorder implements PixmapClient
{
    private static Logger m_logger;
    public static final String TAG = "PixmapBorder";
    private PixmapBorderMesh m_mesh;
    public static final int MODULATION_COLOR_HASH;
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof PixmapElement) {
            this.setPixmap((PixmapElement)e);
        }
        super.add(e);
    }
    
    @Override
    public String getTag() {
        return "PixmapBorder";
    }
    
    @Override
    public void setPixmap(final PixmapElement p) {
        switch (p.getPosition()) {
            case NORTH_WEST: {
                this.m_mesh.setNorthWest(p.getPixmap());
                break;
            }
            case NORTH: {
                this.m_mesh.setNorth(p.getPixmap());
                break;
            }
            case NORTH_EAST: {
                this.m_mesh.setNorthEast(p.getPixmap());
                break;
            }
            case WEST: {
                this.m_mesh.setWest(p.getPixmap());
                break;
            }
            case EAST: {
                this.m_mesh.setEast(p.getPixmap());
                break;
            }
            case SOUTH_WEST: {
                this.m_mesh.setSouthWest(p.getPixmap());
                break;
            }
            case SOUTH: {
                this.m_mesh.setSouth(p.getPixmap());
                break;
            }
            case SOUTH_EAST: {
                this.m_mesh.setSouthEast(p.getPixmap());
                break;
            }
        }
        if (this.m_mesh.isPixmapInitialized()) {
            final DecoratorAppearance app = this.getParentOfType(DecoratorAppearance.class);
            if (app != null) {
                this.m_mesh.updateInsets(this.m_borderInsets);
                app.setBorder(this.m_borderInsets);
            }
        }
    }
    
    public void setPixmaps(final Pixmap northWest, final Pixmap north, final Pixmap northEast, final Pixmap west, final Pixmap east, final Pixmap southWest, final Pixmap south, final Pixmap southEast) {
        this.m_mesh.setPixmaps(northWest, north, northEast, west, east, southWest, south, southEast);
        if (this.m_mesh.isPixmapInitialized()) {
            this.m_mesh.updateInsets(this.m_borderInsets);
            final DecoratorAppearance app = this.getParentOfType(DecoratorAppearance.class);
            app.setBorder(this.m_borderInsets);
        }
    }
    
    @Override
    public PixmapBorderMesh getMesh() {
        return this.m_mesh;
    }
    
    @Override
    public Entity getEntity() {
        return this.m_mesh.getEntity();
    }
    
    @Override
    public void setModulationColor(final Color color) {
        this.m_mesh.setModulationColor(color);
    }
    
    @Override
    public Color getModulationColor() {
        return this.m_mesh.getModulationColor();
    }
    
    @Override
    public boolean isValidAdd(final BasicElement e) {
        if (e instanceof PixmapElement && ((PixmapElement)e).getPosition() == null) {
            PixmapBorder.m_logger.error((Object)"Tentative d'ajout d'une Pixmap sans position");
            return false;
        }
        return super.isValidAdd(e);
    }
    
    @Override
    public void copyElement(final BasicElement b) {
        super.copyElement(b);
        final PixmapBorder pb = (PixmapBorder)b;
        pb.setModulationColor(this.getModulationColor());
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_mesh.onCheckIn();
        this.m_mesh = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        (this.m_mesh = new PixmapBorderMesh()).onCheckOut();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == PixmapBorder.MODULATION_COLOR_HASH) {
            this.setModulationColor(cl.convertToColor(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == PixmapBorder.MODULATION_COLOR_HASH) {
            this.setModulationColor((Color)value);
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        PixmapBorder.m_logger = Logger.getLogger((Class)PixmapBorder.class);
        MODULATION_COLOR_HASH = "modulationColor".hashCode();
    }
}
