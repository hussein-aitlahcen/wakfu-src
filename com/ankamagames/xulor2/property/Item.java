package com.ankamagames.xulor2.property;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.reflect.*;

public class Item
{
    private static Logger m_logger;
    private Object m_value;
    private Property m_virtualProperty;
    
    private Item(final Object value) {
        super();
        this.m_value = value;
    }
    
    public static Item getItem(final Object value, final String name, final ElementMap map, final RenderableContainer rc) {
        if (value instanceof FieldProvider) {
            Property p = null;
            if (map == null) {
                p = PropertiesProvider.getInstance().getProperty(name);
            }
            if (p == null) {
                p = new Property(name, map, true);
                PropertiesProvider.getInstance().addProperty(p);
            }
            p.setValue(value, false);
            final Item item = new Item(value);
            item.setVirtualProperty(p);
            p.addAssociatedItem(item);
            return item;
        }
        return new Item(value);
    }
    
    public static void checkIn(final Item item) {
        if (item == null || item.getVirtualProperty() == null) {
            return;
        }
        item.getVirtualProperty().removeAssociatedItem(item);
    }
    
    public void setValue(final Object value) {
        this.m_value = value;
    }
    
    public Object getValue() {
        return this.m_value;
    }
    
    public void setVirtualProperty(final Property property) {
        this.m_virtualProperty = property;
    }
    
    public Property getVirtualProperty() {
        return this.m_virtualProperty;
    }
    
    public Object getFieldValue(final String fieldName) {
        if (this.m_value != null && this.m_value instanceof FieldProvider && fieldName != null) {
            return Property.getValue(this.m_value, fieldName);
        }
        return null;
    }
    
    static {
        Item.m_logger = Logger.getLogger((Class)Item.class);
    }
}
