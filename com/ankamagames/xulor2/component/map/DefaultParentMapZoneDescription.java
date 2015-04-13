package com.ankamagames.xulor2.component.map;

import com.ankamagames.framework.graphics.image.*;
import org.jetbrains.annotations.*;
import java.util.*;

public class DefaultParentMapZoneDescription<T extends ParentMapZoneDescription> implements ParentMapZoneDescription
{
    private int m_id;
    private Color m_color;
    private String m_textDescription;
    private String m_iconUrl;
    private int m_borderWidth;
    private final ArrayList<PartitionListMapZoneDescription> m_children;
    private short m_altitude;
    private T m_parent;
    
    public DefaultParentMapZoneDescription(final int id, final Color color, final String textDescription, final String iconUrl) {
        super();
        this.m_children = new ArrayList<PartitionListMapZoneDescription>();
        this.m_id = id;
        this.m_color = color;
        this.m_textDescription = textDescription;
        this.m_iconUrl = iconUrl;
    }
    
    public T getParent() {
        return this.m_parent;
    }
    
    public void setParent(final T parent) {
        this.m_parent = parent;
    }
    
    @Override
    public short getAltitudeAt00() {
        return this.m_altitude;
    }
    
    public void setAltitudeAt00(final short altitude) {
        this.m_altitude = altitude;
    }
    
    public void addChild(final PartitionListMapZoneDescription mzd) {
        this.m_children.add(mzd);
    }
    
    public void removeChild(final PartitionListMapZoneDescription mzd) {
        this.m_children.remove(mzd);
    }
    
    public void removeAllChildren() {
        this.m_children.clear();
    }
    
    @Override
    public List<PartitionListMapZoneDescription> getChildren() {
        return this.m_children;
    }
    
    @Override
    public Color getZoneColor() {
        return this.m_color;
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public byte getMaskIndex() {
        return 0;
    }
    
    @Override
    public String getTextDescription() {
        return this.m_textDescription;
    }
    
    public void setBorderWidth(final int borderWidth) {
        this.m_borderWidth = borderWidth;
    }
    
    @Override
    public int getBorderWidth() {
        return this.m_borderWidth;
    }
    
    @Override
    public String getIconUrl() {
        return this.m_iconUrl;
    }
    
    @Override
    public boolean isInteractive() {
        return false;
    }
    
    @Nullable
    @Override
    public String getAnim1() {
        return null;
    }
    
    @Nullable
    @Override
    public String getAnim2() {
        return null;
    }
    
    @Nullable
    @Override
    public String getMapUrl() {
        return null;
    }
    
    @Override
    public long getHighlightSoundId() {
        return -1L;
    }
    
    @Override
    public Collection<? extends ParentMapZoneDescription> getDisplayedZones() {
        return Collections.singleton(this);
    }
}
