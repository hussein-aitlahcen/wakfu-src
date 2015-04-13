package com.ankamagames.wakfu.client.core.monitoring.statistics;

import com.ankamagames.framework.text.*;
import com.ankamagames.framework.kernel.gameStats.*;
import java.util.*;

public class SimpleElementView extends ElementView
{
    public static final String NAME_FIELD = "name";
    public static final String VALUE_FIELD = "value";
    public static final String TOOLTIPS_FIELDS = "tooltipText";
    public static final String[] SIMPLE_ELEMENT_FIELDS;
    public static final String[] ALL_FIELDS;
    private String m_name;
    protected String m_prefix;
    protected String m_suffix;
    protected Object m_value;
    private final String m_key;
    protected final List<SimpleElementView> m_linkedElements;
    protected ColorController m_colorController;
    
    public SimpleElementView(final String name, final String key, final String prefix, final String suffix, final ColorController colorController) {
        this(name, key, colorController);
        this.m_prefix = prefix;
        this.m_suffix = suffix;
    }
    
    public SimpleElementView(final String name, final String key, final String prefix, final String suffix) {
        this(name, key, prefix, suffix, null);
    }
    
    public SimpleElementView(final String name, final String key, final ColorController colorController) {
        super();
        this.m_name = "<undefined>";
        this.m_linkedElements = new ArrayList<SimpleElementView>();
        this.m_name = name;
        this.m_key = key;
        if (colorController != null) {
            colorController.setParent(this);
        }
        this.m_colorController = colorController;
    }
    
    public SimpleElementView(final String name, final String key) {
        this(name, key, null);
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public SimpleElementView setValue(final Object value) {
        this.m_value = value;
        return this;
    }
    
    public String getKey() {
        return this.m_key;
    }
    
    public String getStringValue(final boolean formatted) {
        if (this.m_value == null) {
            return formatted ? "<i>undefined</i>" : "undefined";
        }
        String value = this.m_value.toString();
        if (this.m_prefix != null) {
            value = this.m_prefix + " " + value;
        }
        if (this.m_suffix != null) {
            value = value + " " + this.m_suffix;
        }
        return formatted ? this.format(value) : value;
    }
    
    public String getStringValue() {
        return this.getStringValue(true);
    }
    
    public Object getValue() {
        return this.m_value;
    }
    
    protected String format(final String value) {
        if (this.m_colorController == null) {
            return value;
        }
        final String color = this.m_colorController.getColorFromValue(this.m_value);
        if (color == null) {
            return value;
        }
        return new TextWidgetFormater().addColor(color).append(value).finishAndToString();
    }
    
    public void addLinkedElement(final SimpleElementView elem) {
        this.m_linkedElements.add(elem);
    }
    
    @Override
    public ELEMENT_TYPE getType() {
        return ELEMENT_TYPE.SIMPLE_ELEMENT;
    }
    
    @Override
    public void update() {
        if (this.m_key == null) {
            super.update();
            return;
        }
        final SimplePropertyNode node = Statistics.getInstance().getRootPackage().getProperty(this.m_key);
        if (node != null) {
            this.m_value = node.getValue();
        }
        else {
            this.m_value = null;
        }
        for (final ElementView elem : this.m_linkedElements) {
            elem.update();
        }
        super.update();
    }
    
    protected String getTooltipText() {
        if (this.m_linkedElements == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getName()).append(" : ").append(this.getStringValue(false));
        for (final SimpleElementView view : this.m_linkedElements) {
            sb.append("\n");
            sb.append(view.getName()).append(" : ").append(view.getStringValue(false));
        }
        return sb.toString();
    }
    
    @Override
    public String[] getFields() {
        return SimpleElementView.ALL_FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_name;
        }
        if (fieldName.equals("value")) {
            return this.getStringValue();
        }
        if (fieldName.equals("tooltipText")) {
            return this.getTooltipText();
        }
        return super.getFieldValue(fieldName);
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public SimpleElementView getLinkedElement(final String key) {
        for (final SimpleElementView elem : this.m_linkedElements) {
            if (elem.getKey() != null && elem.getKey().equals(key)) {
                return elem;
            }
        }
        return null;
    }
    
    static {
        SIMPLE_ELEMENT_FIELDS = new String[] { "name", "value", "tooltipText" };
        ALL_FIELDS = new String[SimpleElementView.SIMPLE_ELEMENT_FIELDS.length + ElementView.FIELDS.length];
        System.arraycopy(SimpleElementView.SIMPLE_ELEMENT_FIELDS, 0, SimpleElementView.ALL_FIELDS, 0, SimpleElementView.SIMPLE_ELEMENT_FIELDS.length);
        System.arraycopy(ElementView.FIELDS, 0, SimpleElementView.ALL_FIELDS, SimpleElementView.SIMPLE_ELEMENT_FIELDS.length, ElementView.FIELDS.length);
    }
}
