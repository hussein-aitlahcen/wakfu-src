package com.ankamagames.xulor2.property;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.xulor2.core.taglibrary.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.form.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class PropertyElement extends NonGraphicalElement implements Releasable
{
    private static Logger m_logger;
    public static final String TAG = "Property";
    private String m_name;
    private String m_field;
    private String m_attribute;
    private boolean m_local;
    private Property m_property;
    private ResultProvider m_resultProvider;
    private static final ObjectPool m_pool;
    public static final int LOCAL_HASH;
    public static final int NAME_HASH;
    public static final int ATTRIBUTE_HASH;
    public static final int FIELD_HASH;
    
    public PropertyElement() {
        super();
        this.m_name = null;
        this.m_field = null;
        this.m_attribute = null;
        this.m_local = false;
        this.m_property = null;
        this.m_resultProvider = null;
    }
    
    public static PropertyElement checkOut() {
        PropertyElement c;
        try {
            c = (PropertyElement)PropertyElement.m_pool.borrowObject();
            c.m_currentPool = PropertyElement.m_pool;
        }
        catch (Exception e) {
            PropertyElement.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            c = new PropertyElement();
            c.onCheckOut();
        }
        return c;
    }
    
    @Override
    public void add(final EventDispatcher element) {
        if (element instanceof ResultProvider) {
            this.m_resultProvider = (ResultProvider)element;
        }
        super.add(element);
    }
    
    @Override
    public String getTag() {
        return "Property";
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public void setField(final String field) {
        this.m_field = field;
    }
    
    public void setAttribute(final String attribute) {
        this.m_attribute = attribute;
    }
    
    public void setLocal(final boolean local) {
        this.m_local = local;
    }
    
    public Property getProperty() {
        return this.m_property;
    }
    
    public void addPropertyClient(final BasicElement element) {
        if (this.m_property != null && element != null) {
            this.m_property.addPropertyClient(new PropertyClientData<BasicElement>(element, XulorTagLibrary.getInstance().getFactory(element.getTag()), this.m_attribute, this.m_field, this.m_resultProvider), false);
        }
    }
    
    public void createProperty() {
        if (this.m_property == null) {
            this.m_property = PropertiesProvider.getInstance().getProperty(this.m_name, this.m_local ? this.m_elementMap : null);
            if (this.m_property == null) {
                this.m_property = new Property(this.m_name, this.m_local ? this.m_elementMap : null);
                PropertiesProvider.getInstance().addProperty(this.m_property);
            }
            Environment e = this.m_elementMap.getEnvironment();
            if (e == null) {
                e = Xulor.getInstance().getEnvironment();
            }
            final Form[] forms = e.getCurrentForms();
            if (forms != null) {
                for (final Form f : forms) {
                    f.addProperty(this.m_property);
                }
            }
            final PropertyClientData pc = new PropertyClientData((T)this.m_basicParent, XulorTagLibrary.getInstance().getFactory(this.m_basicParent.getTag()), this.m_attribute, this.m_field, this.m_resultProvider);
            this.m_property.addPropertyClient(pc, false);
        }
    }
    
    @Override
    public void onChildrenAdded() {
        super.onChildrenAdded();
        this.createProperty();
    }
    
    @Override
    public void addedToTree() {
        super.addedToTree();
        this.createProperty();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_name = null;
        this.m_field = null;
        this.m_attribute = null;
        this.m_resultProvider = null;
        this.m_property = null;
    }
    
    @Override
    public void onCheckOut() {
        this.m_local = false;
        super.onCheckOut();
    }
    
    @Override
    public void copyElement(final BasicElement p) {
        final PropertyElement e = (PropertyElement)p;
        super.copyElement(e);
        e.m_attribute = this.m_attribute;
        e.m_field = this.m_field;
        e.m_name = this.m_name;
        e.m_local = this.m_local;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == PropertyElement.LOCAL_HASH) {
            this.setLocal(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == PropertyElement.NAME_HASH) {
            this.setName(cl.convertToString(value, this.m_elementMap));
        }
        else if (hash == PropertyElement.ATTRIBUTE_HASH) {
            this.setAttribute(cl.convertToString(value, this.m_elementMap));
        }
        else {
            if (hash != PropertyElement.FIELD_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setField(cl.convertToString(value, this.m_elementMap));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == PropertyElement.LOCAL_HASH) {
            this.setLocal(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == PropertyElement.NAME_HASH) {
            this.setName(String.valueOf(value));
        }
        else if (hash == PropertyElement.ATTRIBUTE_HASH) {
            this.setAttribute(String.valueOf(value));
        }
        else {
            if (hash != PropertyElement.FIELD_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setField(String.valueOf(value));
        }
        return true;
    }
    
    static {
        PropertyElement.m_logger = Logger.getLogger((Class)PropertyElement.class);
        m_pool = new MonitoredPool(new ObjectFactory<PropertyElement>() {
            @Override
            public PropertyElement makeObject() {
                return new PropertyElement();
            }
        }, 500);
        LOCAL_HASH = "local".hashCode();
        NAME_HASH = "name".hashCode();
        ATTRIBUTE_HASH = "attribute".hashCode();
        FIELD_HASH = "field".hashCode();
    }
}
