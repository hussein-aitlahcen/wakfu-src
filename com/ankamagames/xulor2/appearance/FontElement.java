package com.ankamagames.xulor2.appearance;

import org.apache.log4j.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.framework.fileFormat.document.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class FontElement extends AbstractSwitch implements Releasable
{
    private static Logger m_logger;
    public static final String TAG = "Font";
    private TextRenderer m_renderer;
    private static final ObjectPool m_pool;
    public static final int RENDERER_HASH;
    
    public FontElement() {
        super();
        this.m_renderer = null;
    }
    
    public static FontElement checkOut() {
        FontElement c;
        try {
            c = (FontElement)FontElement.m_pool.borrowObject();
            c.m_currentPool = FontElement.m_pool;
        }
        catch (Exception e) {
            FontElement.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            c = new FontElement();
            c.onCheckOut();
        }
        return c;
    }
    
    @Override
    public String getTag() {
        return "Font";
    }
    
    public void setRenderer(final TextRenderer renderer) {
        this.m_renderer = renderer;
    }
    
    public TextRenderer getRenderer() {
        return this.m_renderer;
    }
    
    @Override
    public void setup(final SwitchClient e) {
        if (e instanceof FontClient) {
            ((FontClient)e).setFont(this.m_renderer);
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_renderer = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
    }
    
    @Override
    public void copyElement(final BasicElement f) {
        final FontElement e = (FontElement)f;
        super.copyElement(e);
        e.m_renderer = this.m_renderer;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == FontElement.RENDERER_HASH) {
            this.setRenderer(cl.convertToFont(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == FontElement.RENDERER_HASH) {
            this.setRenderer((TextRenderer)value);
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    @Override
    public void preApplyAttributes(final DocumentEntry entry, final EventDispatcher parent, final Stack<ElementMap> elementMaps, final Environment env) {
        super.preApplyAttributes(entry, parent, elementMaps, env);
        final DocumentEntry ref = entry.getParameterByName("ref");
        if (ref != null) {
            this.setRenderer(Xulor.getInstance().getDocumentParser().getFont(ref.getStringValue()));
        }
        entry.removeChild(ref);
    }
    
    static {
        FontElement.m_logger = Logger.getLogger((Class)FontElement.class);
        m_pool = new MonitoredPool(new ObjectFactory<FontElement>() {
            @Override
            public FontElement makeObject() {
                return new FontElement();
            }
        }, 500);
        RENDERER_HASH = "renderer".hashCode();
    }
}
