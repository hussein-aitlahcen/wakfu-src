package com.ankamagames.xulor2.decorator;

import org.apache.log4j.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.decorator.mesh.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class TiledPixmapBackground extends Background implements PixmapClient
{
    private static Logger m_logger;
    public static final String TAG = "TiledPixmapBackground";
    private TiledPixmapBackgroundMesh m_mesh;
    private boolean m_vertical;
    private boolean m_horizontal;
    private static final ObjectPool m_pool;
    public static final int MODULATION_COLOR_HASH;
    public static final int VERTICAL_HASH;
    public static final int HORIZONTAl_HASH;
    
    public TiledPixmapBackground() {
        super();
        this.m_mesh = new TiledPixmapBackgroundMesh();
        this.m_vertical = true;
        this.m_horizontal = true;
    }
    
    public static TiledPixmapBackground checkOut() {
        TiledPixmapBackground c;
        try {
            c = (TiledPixmapBackground)TiledPixmapBackground.m_pool.borrowObject();
            c.m_currentPool = TiledPixmapBackground.m_pool;
        }
        catch (Exception e) {
            TiledPixmapBackground.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            c = new TiledPixmapBackground();
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
    public void addedToTree() {
        super.addedToTree();
        if (this.m_appearance == null) {
            TiledPixmapBackground.m_logger.warn((Object)"Appearance null !?");
            return;
        }
        final Widget widget = this.m_appearance.getWidget();
        if (widget == null) {
            TiledPixmapBackground.m_logger.warn((Object)"Widget null !?");
            return;
        }
        final Container parent = widget.getContainer();
        if (parent != null) {
            parent.setNeedsScissor(true);
        }
    }
    
    @Override
    public String getTag() {
        return "TiledPixmapBackground";
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
        this.m_mesh.setPixmap(p.getPixmap());
    }
    
    @Override
    public TiledPixmapBackgroundMesh getMesh() {
        return this.m_mesh;
    }
    
    @Override
    public Entity getEntity() {
        return this.m_mesh.getEntity();
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
        (this.m_mesh = new TiledPixmapBackgroundMesh()).onCheckOut();
        final EventDispatcher eventDispatcher = this.getParent();
    }
    
    @Override
    public void copyElement(final BasicElement b) {
        super.copyElement(b);
        final TiledPixmapBackground bg = (TiledPixmapBackground)b;
        bg.setScaled(this.isScaled());
        bg.setModulationColor(this.getModulationColor());
        bg.setHorizontal(this.m_horizontal);
        bg.setVertical(this.m_vertical);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == TiledPixmapBackground.MODULATION_COLOR_HASH) {
            this.setModulationColor(cl.convertToColor(value));
        }
        else if (hash == TiledPixmapBackground.VERTICAL_HASH) {
            this.setVertical(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != TiledPixmapBackground.HORIZONTAl_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == TiledPixmapBackground.MODULATION_COLOR_HASH) {
            this.setModulationColor((Color)value);
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    public void setVertical(final boolean vertical) {
        if (this.m_mesh != null) {
            this.m_mesh.setVertical(vertical);
        }
        this.m_vertical = vertical;
    }
    
    public void setHorizontal(final boolean horizontal) {
        if (this.m_mesh != null) {
            this.m_mesh.setHorizontal(horizontal);
        }
        this.m_horizontal = horizontal;
    }
    
    static {
        TiledPixmapBackground.m_logger = Logger.getLogger((Class)TiledPixmapBackground.class);
        m_pool = new MonitoredPool(new ObjectFactory<TiledPixmapBackground>() {
            @Override
            public TiledPixmapBackground makeObject() {
                return new TiledPixmapBackground();
            }
        });
        MODULATION_COLOR_HASH = "modulationColor".hashCode();
        VERTICAL_HASH = "vertical".hashCode();
        HORIZONTAl_HASH = "horizontal".hashCode();
    }
}
