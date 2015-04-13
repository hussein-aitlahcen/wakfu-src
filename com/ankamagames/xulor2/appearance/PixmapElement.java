package com.ankamagames.xulor2.appearance;

import org.apache.log4j.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class PixmapElement extends AbstractSwitch implements Releasable, PixmapChangeClient
{
    private static Logger m_logger;
    public static final String TAG = "pixmap";
    private Pixmap m_pixmap;
    private String m_name;
    private Alignment17 m_position;
    private boolean m_pixmapChangedSize;
    private static final ObjectPool m_pool;
    public static final int HEIGHT_HASH;
    public static final int WIDTH_HASH;
    public static final int X_HASH;
    public static final int Y_HASH;
    public static final int NAME_HASH;
    public static final int TEXTURE_HASH;
    public static final int POSITION_HASH;
    public static final int ROTATION_HASH;
    public static final int FLIP_HORIZONTALY_HASH;
    public static final int FLIP_VERTICALY_HASH;
    
    public PixmapElement() {
        super();
        this.m_position = Alignment17.CENTER;
        this.m_pixmapChangedSize = false;
    }
    
    public static PixmapElement checkOut() {
        PixmapElement e;
        try {
            e = (PixmapElement)PixmapElement.m_pool.borrowObject();
            e.m_currentPool = PixmapElement.m_pool;
        }
        catch (Exception ex) {
            PixmapElement.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            e = new PixmapElement();
            e.onCheckOut();
        }
        return e;
    }
    
    @Override
    public String getTag() {
        return "pixmap";
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public void setX(final int x) {
        if (this.m_pixmap != null) {
            this.m_pixmap.setX(x);
            this.setNeedsToPreProcess();
        }
    }
    
    public int getX() {
        if (this.m_pixmap != null) {
            return this.m_pixmap.getX();
        }
        return 0;
    }
    
    public void setY(final int y) {
        if (this.m_pixmap != null) {
            this.m_pixmap.setY(y);
            this.setNeedsToPreProcess();
        }
    }
    
    public int getY() {
        if (this.m_pixmap != null) {
            return this.m_pixmap.getY();
        }
        return 0;
    }
    
    public void setWidth(final int width) {
        if (this.m_pixmap == null) {
            return;
        }
        this.m_pixmap.setWidth(width);
        this.m_pixmapChangedSize = true;
        this.setNeedsToPreProcess();
        this.m_pixmap.setToFullTexture(width == -1);
    }
    
    public int getWidth() {
        if (this.m_pixmap != null) {
            return this.m_pixmap.getWidth();
        }
        return 0;
    }
    
    public void setHeight(final int height) {
        if (this.m_pixmap != null) {
            this.m_pixmap.setHeight(height);
            this.m_pixmapChangedSize = true;
            this.setNeedsToPreProcess();
            this.m_pixmap.setToFullTexture(height == -1);
        }
    }
    
    public int getHeight() {
        if (this.m_pixmap != null) {
            return this.m_pixmap.getHeight();
        }
        return 0;
    }
    
    public int getOrientedHeight() {
        if (this.getRotation().isAffectWidthAndHeight()) {
            return this.getWidth();
        }
        return this.getHeight();
    }
    
    public int getOrientedWidth() {
        if (this.getRotation().isAffectWidthAndHeight()) {
            return this.getHeight();
        }
        return this.getWidth();
    }
    
    public void setFlipHorizontaly(final boolean flip) {
        if (this.m_pixmap != null && this.m_pixmap.isFlipHorizontaly() != flip) {
            this.m_pixmap.setFlipHorizontaly(flip);
            this.setNeedsToPreProcess();
        }
    }
    
    public void setFlipVerticaly(final boolean flip) {
        if (this.m_pixmap != null && this.m_pixmap.isFlipVerticaly() != flip) {
            this.m_pixmap.setFlipVerticaly(flip);
            this.setNeedsToPreProcess();
        }
    }
    
    public void setRotation(final GeometrySprite.SpriteRotation rotation) {
        if (this.m_pixmap != null && this.m_pixmap.getRotation() != rotation) {
            this.m_pixmap.setRotation(rotation);
            this.setNeedsToPreProcess();
        }
    }
    
    public GeometrySprite.SpriteRotation getRotation() {
        if (this.m_pixmap != null) {
            return this.m_pixmap.getRotation();
        }
        return null;
    }
    
    public void setTexture(final Texture texture) {
        if (this.m_pixmap == null) {
            return;
        }
        if (this.m_pixmap.usesFullTexture()) {
            final Texture currentTexture = this.m_pixmap.getTexture();
            if (texture != null || currentTexture != null) {
                if (texture == null || currentTexture == null) {
                    this.m_pixmapChangedSize = true;
                }
                else {
                    final Point2i size = texture.getSize();
                    if (size.getX() != this.m_pixmap.getRealWidth() || size.getY() != this.m_pixmap.getRealHeight()) {
                        this.m_pixmapChangedSize = true;
                    }
                }
            }
        }
        this.m_pixmap.setTexture(texture);
        this.setNeedsToPreProcess();
    }
    
    public Pixmap getPixmap() {
        return this.m_pixmap;
    }
    
    public void setPixmap(final Pixmap p) {
        this.m_pixmap = p;
    }
    
    public void setPosition(final Alignment17 pos) {
        this.m_position = pos;
    }
    
    public Alignment17 getPosition() {
        return this.m_position;
    }
    
    @Override
    public void setup(final SwitchClient e) {
        if (e instanceof PixmapClient) {
            ((PixmapClient)e).setPixmap(this);
        }
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_pixmap != null && this.m_pixmap.needsCompute()) {
            this.m_pixmap.computeCoordinates();
        }
        if (this.m_pixmapChangedSize) {
            final PixmapClient c = this.getParentOfType(PixmapClient.class);
            if (c instanceof Image) {
                c.setPixmap(this);
            }
            this.m_pixmapChangedSize = false;
        }
        return ret;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_position = null;
        if (this.m_pixmap != null) {
            this.m_pixmap.setTexture(null);
        }
        this.m_pixmap = null;
        this.m_name = null;
    }
    
    @Override
    public void onCheckOut() {
        assert this.m_pixmap == null;
        super.onCheckOut();
        this.m_pixmap = new Pixmap();
        this.m_position = Alignment17.CENTER;
        this.m_pixmapChangedSize = false;
    }
    
    @Override
    public void copyElement(final BasicElement p) {
        final PixmapElement e = (PixmapElement)p;
        super.copyElement(e);
        e.m_position = this.m_position;
        e.setTexture(this.m_pixmap.getTexture());
        if (!this.m_pixmap.usesFullTexture()) {
            e.setHeight(this.m_pixmap.getRealHeight());
            e.setWidth(this.m_pixmap.getRealWidth());
        }
        e.setX(this.m_pixmap.getX());
        e.setY(this.m_pixmap.getY());
        e.setFlipHorizontaly(this.m_pixmap.isFlipHorizontaly());
        e.setFlipVerticaly(this.m_pixmap.isFlipVerticaly());
        e.setRotation(this.m_pixmap.getRotation());
        e.setName(this.m_name);
    }
    
    @Override
    public void pixmapChanged(final Pixmap p) {
        this.m_pixmapChangedSize = true;
        this.setNeedsToPreProcess();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == PixmapElement.TEXTURE_HASH) {
            this.setTexture(cl.convertToTexture(value));
        }
        else if (hash == PixmapElement.HEIGHT_HASH) {
            this.setHeight(PrimitiveConverter.getInteger(value));
        }
        else if (hash == PixmapElement.WIDTH_HASH) {
            this.setWidth(PrimitiveConverter.getInteger(value));
        }
        else if (hash == PixmapElement.X_HASH) {
            this.setX(PrimitiveConverter.getInteger(value));
        }
        else if (hash == PixmapElement.Y_HASH) {
            this.setY(PrimitiveConverter.getInteger(value));
        }
        else if (hash == PixmapElement.POSITION_HASH) {
            this.setPosition(Alignment17.value(value));
        }
        else if (hash == PixmapElement.ROTATION_HASH) {
            this.setRotation(GeometrySprite.SpriteRotation.value(value));
        }
        else if (hash == PixmapElement.FLIP_HORIZONTALY_HASH) {
            this.setFlipHorizontaly(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == PixmapElement.FLIP_VERTICALY_HASH) {
            this.setFlipVerticaly(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != PixmapElement.NAME_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setName(value);
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == PixmapElement.TEXTURE_HASH) {
            this.setTexture((Texture)value);
        }
        else if (hash == PixmapElement.HEIGHT_HASH) {
            this.setHeight(PrimitiveConverter.getInteger(value));
        }
        else if (hash == PixmapElement.WIDTH_HASH) {
            this.setWidth(PrimitiveConverter.getInteger(value));
        }
        else if (hash == PixmapElement.X_HASH) {
            this.setX(PrimitiveConverter.getInteger(value));
        }
        else if (hash == PixmapElement.Y_HASH) {
            this.setY(PrimitiveConverter.getInteger(value));
        }
        else if (hash == PixmapElement.POSITION_HASH) {
            this.setPosition((Alignment17)value);
        }
        else if (hash == PixmapElement.ROTATION_HASH) {
            this.setRotation((GeometrySprite.SpriteRotation)value);
        }
        else if (hash == PixmapElement.FLIP_HORIZONTALY_HASH) {
            this.setFlipHorizontaly(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == PixmapElement.FLIP_VERTICALY_HASH) {
            this.setFlipVerticaly(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != PixmapElement.NAME_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setName((String)value);
        }
        return true;
    }
    
    static {
        PixmapElement.m_logger = Logger.getLogger((Class)PixmapElement.class);
        m_pool = new MonitoredPool(new ObjectFactory<PixmapElement>() {
            @Override
            public PixmapElement makeObject() {
                return new PixmapElement();
            }
        }, 10000);
        HEIGHT_HASH = "height".hashCode();
        WIDTH_HASH = "width".hashCode();
        X_HASH = "x".hashCode();
        Y_HASH = "y".hashCode();
        NAME_HASH = "name".hashCode();
        TEXTURE_HASH = "texture".hashCode();
        POSITION_HASH = "position".hashCode();
        ROTATION_HASH = "rotation".hashCode();
        FLIP_HORIZONTALY_HASH = "flipHorizontaly".hashCode();
        FLIP_VERTICALY_HASH = "flipVerticaly".hashCode();
    }
}
