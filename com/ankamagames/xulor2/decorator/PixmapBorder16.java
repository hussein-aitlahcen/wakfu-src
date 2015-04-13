package com.ankamagames.xulor2.decorator;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.decorator.mesh.*;

public class PixmapBorder16 extends InsetsBorder implements PixmapClient
{
    private static Logger m_logger;
    private PixmapBorder16Mesh m_mesh;
    public static final String TAG = "pixmapBorder16";
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
        return "pixmapBorder16";
    }
    
    @Override
    public void setModulationColor(final Color c) {
        this.m_mesh.setModulationColor(c);
    }
    
    @Override
    public Color getModulationColor() {
        return (this.m_mesh != null) ? this.m_mesh.getModulationColor() : null;
    }
    
    @Override
    public void setPixmap(final PixmapElement p) {
        this.m_mesh.setPixmap(p.getPixmap(), p.getPosition());
        this.m_dirty = true;
        if (this.m_mesh.isPixmapInitialized()) {
            this.m_mesh.updateInsets(this.m_borderInsets);
            final DecoratorAppearance app = this.getParentOfType(DecoratorAppearance.class);
            if (app != null) {
                app.setBorder(this.m_borderInsets);
            }
        }
    }
    
    public void setPixmaps(final Pixmap northWest, final Pixmap northNorthWest, final Pixmap north, final Pixmap northNorthEast, final Pixmap northEast, final Pixmap northWestWest, final Pixmap northEastEast, final Pixmap west, final Pixmap east, final Pixmap southWestWest, final Pixmap southEastEast, final Pixmap southWest, final Pixmap southSouthWest, final Pixmap south, final Pixmap southSouthEast, final Pixmap southEast) {
        this.m_mesh.setPixmaps(northWest, northNorthWest, north, northNorthEast, northEast, northWestWest, northEastEast, west, east, southWestWest, southEastEast, southWest, southSouthWest, south, southSouthEast, southEast);
        this.m_dirty = true;
        if (this.m_mesh.isPixmapInitialized()) {
            this.m_mesh.updateInsets(this.m_borderInsets);
            final DecoratorAppearance app = this.getParentOfType(DecoratorAppearance.class);
            app.setBorder(this.m_borderInsets);
        }
    }
    
    public void setPixmaps(final Pixmap[] pixmap) {
        if (pixmap.length == 16) {
            this.m_mesh.setPixmaps(pixmap);
        }
        else {
            PixmapBorder16.m_logger.error((Object)"La taille du tableau pass\u00e9 en parametre ne correspond pas au nombre de pixmap donc on a besoin!");
        }
        this.m_dirty = true;
        if (this.m_mesh.isPixmapInitialized()) {
            this.m_mesh.updateInsets(this.m_borderInsets);
            final DecoratorAppearance app = this.getParentOfType(DecoratorAppearance.class);
            app.setBorder(this.m_borderInsets);
        }
    }
    
    @Override
    public PixmapBorder16Mesh getMesh() {
        return this.m_mesh;
    }
    
    @Override
    public Entity getEntity() {
        return this.m_mesh.getEntity();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        (this.m_mesh = new PixmapBorder16Mesh()).onCheckOut();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_mesh.onCheckIn();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == PixmapBorder16.MODULATION_COLOR_HASH) {
            this.setModulationColor(cl.convertToColor(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == PixmapBorder16.MODULATION_COLOR_HASH) {
            this.setModulationColor((Color)value);
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        PixmapBorder16.m_logger = Logger.getLogger((Class)PixmapBorder16.class);
        MODULATION_COLOR_HASH = "modulationColor".hashCode();
    }
}
