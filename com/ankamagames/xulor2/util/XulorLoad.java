package com.ankamagames.xulor2.util;

import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import java.net.*;
import java.util.*;

public class XulorLoad implements XulorLoadUnload
{
    private final long m_options;
    private final EventDispatcher m_newElement;
    private final String m_id;
    private final Xulor.DialogPathDefinition m_pathDefinition;
    private final short m_level;
    private final ElementMap m_elementMap;
    private final EventDispatcher m_parent;
    private final URL m_currentDirectory;
    private final int m_duration;
    private final String m_relativeElementId;
    private final String m_relativeCascadeElementId;
    private final String m_relativePositionElementId;
    private final String m_controlGroupId;
    private boolean m_overRelative;
    private final boolean m_asTooltip;
    private final int m_tooltipX;
    private final int m_tooltipY;
    private HashMap<String, String> m_environmentProperties;
    
    public Xulor.DialogPathDefinition getPathDefinition() {
        return this.m_pathDefinition;
    }
    
    public HashMap<String, String> getEnvironmentProperties() {
        return this.m_environmentProperties;
    }
    
    public ElementMap getElementMap() {
        return this.m_elementMap;
    }
    
    public String getId() {
        return this.m_id;
    }
    
    public short getLevel() {
        return this.m_level;
    }
    
    public long getOptions() {
        return this.m_options;
    }
    
    public EventDispatcher getParent() {
        return this.m_parent;
    }
    
    public int getDuration() {
        return this.m_duration;
    }
    
    public URL getCurrentDirectory() {
        return this.m_currentDirectory;
    }
    
    public boolean isOverRelative() {
        return this.m_overRelative;
    }
    
    public String getRelativeCascadeElementId() {
        return this.m_relativeCascadeElementId;
    }
    
    public String getRelativePositionElementId() {
        return this.m_relativePositionElementId;
    }
    
    public String getRelativeElementId() {
        return this.m_relativeElementId;
    }
    
    public String getControlGroupId() {
        return this.m_controlGroupId;
    }
    
    public boolean isAsTooltip() {
        return this.m_asTooltip;
    }
    
    public int getTooltipX() {
        return this.m_tooltipX;
    }
    
    public int getTooltipY() {
        return this.m_tooltipY;
    }
    
    public EventDispatcher getNewElement() {
        return this.m_newElement;
    }
    
    public XulorLoad(final Xulor.DialogPathDefinition pathDefinition, final EventDispatcher newElement, final String id, final ElementMap elementMap, final EventDispatcher parent, final String relativeCascadeElementId, final String relativePositionElementId, final String relativeElementId, final String controlGroupId, final boolean overRelative, final boolean asTooltip, final int tooltipX, final int tooltipY, final URL currentDirectory, final int duration, final long options, final short level, final HashMap<String, String> environmentProperties) {
        super();
        this.m_overRelative = true;
        this.m_environmentProperties = null;
        this.m_pathDefinition = pathDefinition;
        this.m_elementMap = elementMap;
        this.m_id = id;
        this.m_level = level;
        this.m_options = options;
        this.m_parent = parent;
        this.m_duration = duration;
        this.m_currentDirectory = currentDirectory;
        this.m_relativeElementId = relativeElementId;
        this.m_relativeCascadeElementId = relativeCascadeElementId;
        this.m_relativePositionElementId = relativePositionElementId;
        this.m_controlGroupId = controlGroupId;
        this.m_overRelative = overRelative;
        this.m_asTooltip = asTooltip;
        this.m_tooltipX = tooltipX;
        this.m_tooltipY = tooltipY;
        this.m_newElement = newElement;
        this.m_environmentProperties = environmentProperties;
    }
}
