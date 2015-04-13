package com.ankamagames.xulor2.util;

public class DisplayableMapPoint
{
    private float m_isoX;
    private float m_isoY;
    private float m_isoZ;
    private float m_desiredIsoX;
    private float m_desiredIsoY;
    private float m_desiredIsoZ;
    private short m_instanceId;
    private String m_name;
    private boolean m_editable;
    private boolean m_dndropable;
    private boolean m_beingEdited;
    private boolean m_visible;
    private boolean m_highlightOnOver;
    private boolean m_useGrayScale;
    private Object m_value;
    private DisplayableMapPointIcon m_icon;
    private DisplayableMapPointIcon m_alternateIcon;
    private DisplayableMapPointIcon m_overlayIcon;
    private String m_particlePath;
    private float[] m_color;
    
    public DisplayableMapPoint(final float isoX, final float isoY, final float isoZ, final short instanceId, final String name, final DisplayableMapPointIcon icon, final float[] color) {
        this(isoX, isoY, isoZ, instanceId, name, null, icon, color);
    }
    
    public DisplayableMapPoint(final float isoX, final float isoY, final float isoZ, final short instanceId, final String name, final Object value, final DisplayableMapPointIcon icon, final float[] color) {
        this(isoX, isoY, isoZ, instanceId, name, value, icon, color, false, false);
    }
    
    public DisplayableMapPoint(final float isoX, final float isoY, final float isoZ, final short instanceId, final String name, final Object value, final DisplayableMapPointIcon icon, final String particlePath, final float[] color) {
        this(isoX, isoY, isoZ, instanceId, name, value, icon, null, particlePath, color, false, false);
    }
    
    public DisplayableMapPoint(final float isoX, final float isoY, final float isoZ, final short instanceId, final String name, final Object value, final DisplayableMapPointIcon icon, final float[] color, final boolean editable, final boolean dragNDropable) {
        this(isoX, isoY, isoZ, instanceId, name, value, icon, null, color, editable, dragNDropable);
    }
    
    public DisplayableMapPoint(final float isoX, final float isoY, final float isoZ, final short instanceId, final String name, final Object value, final DisplayableMapPointIcon icon, final DisplayableMapPointIcon alternateIcon, final float[] color, final boolean editable, final boolean dragNDropable) {
        this(isoX, isoY, isoZ, instanceId, name, value, icon, alternateIcon, null, color, editable, dragNDropable);
    }
    
    public DisplayableMapPoint(final float isoX, final float isoY, final float isoZ, final short instanceId, final String name, final Object value, final DisplayableMapPointIcon icon, final DisplayableMapPointIcon alternateIcon, final String particlePath, final float[] color, final boolean editable, final boolean dragNDropable) {
        super();
        this.m_visible = true;
        this.m_isoX = isoX;
        this.m_isoY = isoY;
        this.m_isoZ = isoZ;
        this.m_desiredIsoX = isoX;
        this.m_desiredIsoY = isoY;
        this.m_desiredIsoZ = isoZ;
        this.m_instanceId = instanceId;
        this.m_name = name;
        this.m_value = value;
        this.m_icon = icon;
        this.m_alternateIcon = alternateIcon;
        this.m_particlePath = particlePath;
        this.m_color = color;
        this.m_editable = editable;
        this.m_dndropable = dragNDropable;
    }
    
    public float getIsoX() {
        return this.m_isoX;
    }
    
    public void setIsoX(final float isoX) {
        this.m_isoX = isoX;
    }
    
    public float getIsoY() {
        return this.m_isoY;
    }
    
    public void setIsoY(final float isoY) {
        this.m_isoY = isoY;
    }
    
    public float getIsoZ() {
        return this.m_isoZ;
    }
    
    public void setIsoZ(final float isoZ) {
        this.m_isoZ = isoZ;
    }
    
    public short getInstanceId() {
        return this.m_instanceId;
    }
    
    public void setInstanceId(final short instanceId) {
        this.m_instanceId = instanceId;
    }
    
    public String getName() {
        if (this.m_name != null) {
            return this.m_name;
        }
        return null;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public DisplayableMapPointIcon getIcon() {
        return this.m_icon;
    }
    
    public DisplayableMapPointIcon getAlternateIcon() {
        return this.m_alternateIcon;
    }
    
    public DisplayableMapPointIcon getOverlayIcon() {
        return this.m_overlayIcon;
    }
    
    public void setIcon(final DisplayableMapPointIcon icon) {
        this.m_icon = icon;
    }
    
    public void setAlternateIcon(final DisplayableMapPointIcon alternateIcon) {
        this.m_alternateIcon = alternateIcon;
    }
    
    public void setOverlayIcon(final DisplayableMapPointIcon overlayIcon) {
        this.m_overlayIcon = overlayIcon;
    }
    
    public float[] getColor() {
        return this.m_color;
    }
    
    public void setColor(final float[] color) {
        this.m_color = color;
    }
    
    public Object getValue() {
        return this.m_value;
    }
    
    public void setValue(final Object value) {
        this.m_value = value;
    }
    
    public float getDesiredIsoY() {
        return this.m_desiredIsoY;
    }
    
    public float getDesiredIsoX() {
        return this.m_desiredIsoX;
    }
    
    public float getDesiredIsoZ() {
        return this.m_desiredIsoZ;
    }
    
    public void setDesiredIso(final float worldX, final float worldY, final float altitude) {
        this.m_desiredIsoX = worldX;
        this.m_desiredIsoY = worldY;
        this.m_desiredIsoZ = altitude;
    }
    
    public boolean isEditable() {
        return this.m_editable;
    }
    
    public void setEditable(final boolean editable) {
        this.m_editable = editable;
    }
    
    public void setDndropable(final boolean dndropable) {
        this.m_dndropable = dndropable;
    }
    
    public boolean isDndropable() {
        return this.m_dndropable;
    }
    
    public boolean isBeingEdited() {
        return this.m_beingEdited;
    }
    
    public void setBeingEdited(final boolean beingEdited) {
        this.m_beingEdited = beingEdited;
    }
    
    public boolean isHighlightOnOver() {
        return this.m_highlightOnOver;
    }
    
    public void setHighlightOnOver(final boolean highlightOnOver) {
        this.m_highlightOnOver = highlightOnOver;
    }
    
    public boolean isUseGrayScale() {
        return this.m_useGrayScale;
    }
    
    public void setUseGrayScale(final boolean useGrayScale) {
        this.m_useGrayScale = useGrayScale;
    }
    
    public String getParticlePath() {
        return this.m_particlePath;
    }
    
    public void setParticlePath(final String particlePath) {
        this.m_particlePath = particlePath;
    }
    
    public boolean isVisible() {
        return this.m_visible;
    }
    
    public void setVisible(final boolean visible) {
        this.m_visible = visible;
    }
    
    public boolean needToBeRefreshed(final float epsilon) {
        return Math.abs(this.m_desiredIsoX - this.m_isoX) >= epsilon || Math.abs(this.m_desiredIsoY - this.m_isoY) >= epsilon;
    }
}
