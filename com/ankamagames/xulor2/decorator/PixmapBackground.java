package com.ankamagames.xulor2.decorator;

import org.apache.log4j.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.decorator.mesh.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.xulor2.util.alignment.*;

public class PixmapBackground extends Background implements PixmapClient
{
    private static Logger m_logger;
    public static final String TAG = "PixmapBackground";
    private PixmapBackgroundMesh m_mesh;
    private static final ObjectPool m_pool;
    public static final int MODULATION_COLOR_HASH;
    
    public PixmapBackground() {
        super();
        this.m_mesh = new PixmapBackgroundMesh();
    }
    
    public static PixmapBackground checkOut() {
        PixmapBackground c;
        try {
            c = (PixmapBackground)PixmapBackground.m_pool.borrowObject();
            c.m_currentPool = PixmapBackground.m_pool;
        }
        catch (Exception e) {
            PixmapBackground.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            c = new PixmapBackground();
            c.onCheckOut();
        }
        return c;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof PixmapElement) {
            this.setPixmap((PixmapElement)e);
        }
        super.add(e);
    }
    
    @Override
    public String getTag() {
        return "PixmapBackground";
    }
    
    @Override
    public void setModulationColor(final Color c) {
        this.m_mesh.setModulationColor(c);
    }
    
    @Override
    public Color getModulationColor() {
        return this.m_mesh.getModulationColor();
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
            case CENTER: {
                this.m_mesh.setCenter(p.getPixmap());
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
    }
    
    public void setPixmaps(final Pixmap northWest, final Pixmap north, final Pixmap northEast, final Pixmap west, final Pixmap center, final Pixmap east, final Pixmap southWest, final Pixmap south, final Pixmap southEast) {
        this.m_mesh.setPixmaps(northWest, north, northEast, west, center, east, southWest, south, southEast);
        if (northWest == null || north == null || northEast == null || west == null || center == null || east == null || southWest == null || south == null || southEast == null) {
            PixmapBackground.m_logger.error((Object)"Une des pixmaps pass\u00e9e est nulle !");
        }
    }
    
    public void setPixmaps(final Pixmap[] pixmap) {
        if (pixmap.length == 9) {
            this.m_mesh.setPixmaps(pixmap);
        }
        else {
            PixmapBackground.m_logger.error((Object)"La taille du tableau pass\u00e9 en parametre ne correspond pas au nombre de pixmap donc on a besoin!");
        }
        if (pixmap[0] == null || pixmap[1] == null || pixmap[2] == null || pixmap[3] == null || pixmap[4] == null || pixmap[5] == null || pixmap[6] == null || pixmap[7] == null || pixmap[8] == null) {
            PixmapBackground.m_logger.error((Object)"Une des pixmaps pass\u00e9e est nulle !");
        }
    }
    
    public void setPixmaps(final Pixmap pixmap) {
        this.m_mesh.setPixmaps(pixmap);
        if (pixmap == null) {
            PixmapBackground.m_logger.error((Object)"Une des pixmaps pass\u00e9e est nulle !");
        }
    }
    
    @Override
    public PixmapBackgroundMesh getMesh() {
        return this.m_mesh;
    }
    
    @Override
    public Entity getEntity() {
        return this.m_mesh.getEntity();
    }
    
    @Override
    public void setScaled(final boolean scaled) {
        super.setScaled(scaled);
        this.m_mesh.setScaled(scaled);
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
        (this.m_mesh = new PixmapBackgroundMesh()).onCheckOut();
    }
    
    @Override
    public void copyElement(final BasicElement b) {
        super.copyElement(b);
        final PixmapBackground bg = (PixmapBackground)b;
        bg.setScaled(this.isScaled());
        bg.setModulationColor(this.getModulationColor());
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == PixmapBackground.MODULATION_COLOR_HASH) {
            this.setModulationColor(cl.convertToColor(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == PixmapBackground.MODULATION_COLOR_HASH) {
            this.setModulationColor((Color)value);
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        PixmapBackground.m_logger = Logger.getLogger((Class)PixmapBackground.class);
        m_pool = new MonitoredPool(new ObjectFactory<PixmapBackground>() {
            @Override
            public PixmapBackground makeObject() {
                return new PixmapBackground();
            }
        });
        MODULATION_COLOR_HASH = "modulationColor".hashCode();
    }
}
