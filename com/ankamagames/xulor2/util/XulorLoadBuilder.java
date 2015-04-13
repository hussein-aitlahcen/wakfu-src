package com.ankamagames.xulor2.util;

import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import java.net.*;
import java.util.*;

public class XulorLoadBuilder
{
    private long m_options;
    private EventDispatcher m_newElement;
    private String m_id;
    private Xulor.DialogPathDefinition m_pathDefinition;
    private short m_level;
    private ElementMap m_elementMap;
    private EventDispatcher m_parent;
    private URL m_currentDirectory;
    private int m_duration;
    private String m_relativeElementId;
    private String m_relativeCascadeElementId;
    private String m_relativePositionElementId;
    private String m_controlGroupId;
    private boolean m_overRelative;
    private boolean m_asTooltip;
    private int m_tooltipX;
    private int m_tooltipY;
    private HashMap<String, String> m_environmentProperties;
    
    public XulorLoadBuilder() {
        super();
        this.m_options = 0L;
        this.m_newElement = null;
        this.m_id = null;
        this.m_pathDefinition = null;
        this.m_level = 0;
        this.m_elementMap = null;
        this.m_parent = null;
        this.m_currentDirectory = null;
        this.m_duration = Integer.MAX_VALUE;
        this.m_relativeElementId = null;
        this.m_relativeCascadeElementId = null;
        this.m_relativePositionElementId = null;
        this.m_controlGroupId = null;
        this.m_overRelative = false;
        this.m_asTooltip = false;
        this.m_tooltipX = 0;
        this.m_tooltipY = 0;
        this.m_environmentProperties = null;
    }
    
    public XulorLoadBuilder setPath(final String path) {
        this.m_pathDefinition = Xulor.DialogPathDefinition.createDialogPathDefinition(path);
        return this;
    }
    
    public XulorLoadBuilder setPathDefinition(final Xulor.DialogPathDefinition def) {
        this.m_pathDefinition = def;
        return this;
    }
    
    public XulorLoadBuilder setElementMap(final ElementMap map) {
        this.m_elementMap = map;
        return this;
    }
    
    public XulorLoadBuilder setId(final String id) {
        this.m_id = id;
        return this;
    }
    
    public XulorLoadBuilder setLevel(final short level) {
        this.m_level = level;
        return this;
    }
    
    public XulorLoadBuilder setOptions(final long options) {
        this.m_options = options;
        return this;
    }
    
    public XulorLoadBuilder setParent(final EventDispatcher parent) {
        this.m_parent = parent;
        return this;
    }
    
    public XulorLoadBuilder setDuration(final int duration) {
        this.m_duration = duration;
        return this;
    }
    
    public XulorLoadBuilder setCurrentDirectory(final URL currentDirectory) {
        this.m_currentDirectory = currentDirectory;
        return this;
    }
    
    public XulorLoadBuilder setOverRelative(final boolean overRelative) {
        this.m_overRelative = overRelative;
        return this;
    }
    
    public XulorLoadBuilder setRelativeCascadeElementId(final String id) {
        this.m_relativeCascadeElementId = id;
        return this;
    }
    
    public XulorLoadBuilder setRelativePositionElementId(final String id) {
        this.m_relativePositionElementId = id;
        return this;
    }
    
    public XulorLoadBuilder setRelativeElementId(final String id) {
        this.m_relativeElementId = id;
        return this;
    }
    
    public XulorLoadBuilder setControlGroupId(final String id) {
        this.m_controlGroupId = id;
        return this;
    }
    
    public XulorLoadBuilder setAsTooltip(final boolean asTooltip) {
        this.m_asTooltip = asTooltip;
        return this;
    }
    
    public XulorLoadBuilder setTooltipX(final int x) {
        this.m_tooltipX = x;
        return this;
    }
    
    public XulorLoadBuilder setTooltipY(final int y) {
        this.m_tooltipY = y;
        return this;
    }
    
    public XulorLoadBuilder setNewElement(final EventDispatcher newElement) {
        this.m_newElement = newElement;
        return this;
    }
    
    public void putEnvironmentProperty(final String name, final String value) {
        if (this.m_environmentProperties == null) {
            this.m_environmentProperties = new HashMap<String, String>();
        }
        this.m_environmentProperties.put(name, value);
    }
    
    public XulorLoad build() {
        return new XulorLoad(this.m_pathDefinition, this.m_newElement, this.m_id, this.m_elementMap, this.m_parent, this.m_relativeCascadeElementId, this.m_relativePositionElementId, this.m_relativeElementId, this.m_controlGroupId, this.m_overRelative, this.m_asTooltip, this.m_tooltipX, this.m_tooltipY, this.m_currentDirectory, this.m_duration, this.m_options, this.m_level, this.m_environmentProperties);
    }
}
