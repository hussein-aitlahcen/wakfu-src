package com.ankamagames.xulor2.appearance;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class ImageAppearance extends DecoratorAppearance implements Releasable
{
    private static final Logger m_logger;
    public static final String TAG = "ImageAppearance";
    private boolean m_scaled;
    private boolean m_scaledInit;
    private PixmapElement m_pixmap;
    private Color m_modulationColor;
    private String m_shader;
    private static final ObjectPool m_pool;
    public static final int MODULATION_COLOR_HASH;
    public static final int SCALED_HASH;
    public static final int SHADER_HASH;
    
    public static ImageAppearance checkOut() {
        ImageAppearance c;
        try {
            c = (ImageAppearance)ImageAppearance.m_pool.borrowObject();
            c.m_currentPool = ImageAppearance.m_pool;
        }
        catch (Exception e) {
            ImageAppearance.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            c = new ImageAppearance();
            c.onCheckOut();
        }
        return c;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof PixmapElement) {
            this.m_pixmap = (PixmapElement)e;
            if (this.m_widget instanceof Image) {
                ((Image)this.m_widget).setPixmap((PixmapElement)e);
                if (this.m_scaledInit) {
                    ((Image)this.m_widget).setScaled(this.m_scaled);
                }
            }
            else if (this.m_widget != null) {
                ImageAppearance.m_logger.error((Object)("Un " + this.m_widget.getClass() + " poss\u00e8de une ImageAppearance et ne peut pas recevoir de Pixmap"));
            }
        }
        super.add(e);
    }
    
    @Override
    public String getTag() {
        return "ImageAppearance";
    }
    
    @Override
    public Color getModulationColor() {
        return this.m_modulationColor;
    }
    
    @Override
    public void setModulationColor(final Color modulationColor) {
        if (this.m_modulationColor == modulationColor) {
            return;
        }
        this.m_modulationColor = modulationColor;
        if (this.m_widget instanceof Image) {
            ((Image)this.m_widget).setModulationColor(modulationColor);
        }
    }
    
    public boolean isScaled() {
        return this.m_scaled;
    }
    
    public void setScaled(final boolean scaled) {
        this.m_scaled = scaled;
        this.m_scaledInit = true;
        if (this.m_widget instanceof Image) {
            ((Image)this.m_widget).setScaled(this.m_scaled);
        }
    }
    
    @Override
    public void setWidget(final Widget w) {
        super.setWidget(w);
        if (w instanceof Image) {
            final Image im = (Image)w;
            if (this.m_scaledInit) {
                im.setScaled(this.m_scaled);
            }
            if (this.m_modulationColor != null) {
                im.setModulationColor(this.m_modulationColor);
            }
            this.setShader(this.m_shader);
        }
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        if (this.m_widgetChanged && this.m_widget instanceof Image) {
            final Image image = (Image)this.m_widget;
            if (this.m_pixmap != null) {
                image.setPixmap(this.m_pixmap);
            }
            if (this.m_scaledInit) {
                image.setScaled(this.m_scaled);
            }
        }
        return super.preProcess(deltaTime);
    }
    
    @Override
    public void destroyAllRemovableDecorators() {
        for (int i = this.m_switches.size() - 1; i >= 0; --i) {
            final Switch s = this.m_switches.get(i);
            if (s instanceof PixmapElement) {
                this.m_widget.getEntity().removeAllChildren();
            }
        }
        super.destroyAllRemovableDecorators();
    }
    
    @Override
    public void copyElement(final BasicElement i) {
        final ImageAppearance e = (ImageAppearance)i;
        super.copyElement(e);
        if (this.m_scaledInit) {
            e.setScaled(this.m_scaled);
        }
        e.setShader(this.m_shader);
        e.setModulationColor(this.m_modulationColor);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_pixmap = null;
        this.m_modulationColor = null;
        this.m_shader = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_scaled = false;
        this.m_scaledInit = false;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ImageAppearance.MODULATION_COLOR_HASH) {
            this.setModulationColor(cl.convertToColor(value));
        }
        else if (hash == ImageAppearance.SCALED_HASH) {
            this.setScaled(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != ImageAppearance.SHADER_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setShader(value);
        }
        return true;
    }
    
    public void setShader(final String shaderName) {
        this.m_shader = shaderName;
        final Widget widget = this.getWidget();
        if (widget == null) {
            return;
        }
        ((Image)widget).setShader(shaderName);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == ImageAppearance.MODULATION_COLOR_HASH) {
            this.setModulationColor((Color)value);
        }
        else if (hash == ImageAppearance.SCALED_HASH) {
            this.setScaled(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != ImageAppearance.SHADER_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setShader(value.toString());
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ImageAppearance.class);
        m_pool = new MonitoredPool(new ObjectFactory<ImageAppearance>() {
            @Override
            public ImageAppearance makeObject() {
                return new ImageAppearance();
            }
        });
        MODULATION_COLOR_HASH = "modulationColor".hashCode();
        SCALED_HASH = "scaled".hashCode();
        SHADER_HASH = "shader".hashCode();
    }
}
