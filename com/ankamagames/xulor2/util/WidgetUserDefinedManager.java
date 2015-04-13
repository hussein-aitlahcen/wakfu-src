package com.ankamagames.xulor2.util;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.baseImpl.graphics.ui.*;
import com.ankamagames.framework.preferences.*;
import com.ankamagames.xulor2.core.*;

public class WidgetUserDefinedManager extends UserDefinedManager<Widget>
{
    private boolean m_usePosition;
    private boolean m_useSize;
    private boolean m_positionChanged;
    private boolean m_sizeChanged;
    
    public WidgetUserDefinedManager(final Widget element) {
        super(element);
        this.m_usePosition = false;
        this.m_useSize = false;
        this.m_positionChanged = false;
        this.m_sizeChanged = false;
    }
    
    @Override
    protected void doLoadPreferences(final String mapId, final String elementId) {
        final UserDefinedAdapter adapter = Xulor.getInstance().getUserDefinedAdapter();
        if (adapter == null) {
            return;
        }
        final PreferenceStore pref = adapter.getPreferenceStoreForDialog(mapId);
        if (pref == null) {
            return;
        }
        if (this.m_usePosition) {
            final String xKey = XulorUtil.generatePreferenceKey(mapId, elementId, "x");
            final String yKey = XulorUtil.generatePreferenceKey(mapId, elementId, "y");
            int x = ((Widget)this.m_element).getX();
            int y = ((Widget)this.m_element).getY();
            if (pref.contains(xKey)) {
                x = pref.getInt(xKey);
            }
            if (pref.contains(yKey)) {
                y = pref.getInt(yKey);
            }
            ((Widget)this.m_element).setPosition(x, y);
        }
        if (this.m_useSize) {
            final String widthKey = XulorUtil.generatePreferenceKey(mapId, elementId, "width");
            final String heightKey = XulorUtil.generatePreferenceKey(mapId, elementId, "height");
            int width = ((Widget)this.m_element).getWidth();
            int height = ((Widget)this.m_element).getHeight();
            if (pref.contains(widthKey)) {
                width = pref.getInt(widthKey);
            }
            if (pref.contains(heightKey)) {
                height = pref.getInt(heightKey);
            }
            ((Widget)this.m_element).setSize(width, height);
        }
    }
    
    @Override
    protected void doStorePreferences(final String mapId, final String elementId) {
        final PreferenceStore pref = Xulor.getInstance().getUserDefinedAdapter().getPreferenceStoreForDialog(mapId);
        if (pref == null) {
            return;
        }
        if (((Widget)this.m_element).getWidth() == 0 && ((Widget)this.m_element).getHeight() == 0) {
            return;
        }
        if (this.m_usePosition && this.m_positionChanged) {
            final String x = XulorUtil.generatePreferenceKey(mapId, elementId, "x");
            final String y = XulorUtil.generatePreferenceKey(mapId, elementId, "y");
            pref.setValue(x, ((Widget)this.m_element).getX());
            pref.setValue(y, ((Widget)this.m_element).getY());
        }
        if (this.m_useSize && this.m_sizeChanged) {
            final String width = XulorUtil.generatePreferenceKey(mapId, elementId, "width");
            final String height = XulorUtil.generatePreferenceKey(mapId, elementId, "height");
            pref.setValue(width, ((Widget)this.m_element).getWidth());
            pref.setValue(height, ((Widget)this.m_element).getHeight());
        }
    }
    
    public void removeKeys(final String mapId, final String elementId) {
        final PreferenceStore pref = Xulor.getInstance().getUserDefinedAdapter().getPreferenceStoreForDialog(mapId);
        if (pref == null) {
            return;
        }
        final String x = XulorUtil.generatePreferenceKey(mapId, elementId, "x");
        final String y = XulorUtil.generatePreferenceKey(mapId, elementId, "y");
        final String width = XulorUtil.generatePreferenceKey(mapId, elementId, "width");
        final String height = XulorUtil.generatePreferenceKey(mapId, elementId, "height");
        pref.removeKey(x);
        pref.removeKey(y);
        pref.removeKey(width);
        pref.removeKey(height);
    }
    
    @Override
    public void removeFromGlobalManager() {
        GlobalUserDefinedManager.getInstance().removeManager(this);
    }
    
    @Override
    public void addToGlobalManager() {
        GlobalUserDefinedManager.getInstance().addManager(this);
    }
    
    @Override
    public boolean hasRecord() {
        final ElementMap map = ((Widget)this.m_element).getElementMap();
        if (map == null) {
            return false;
        }
        final String mapId = map.getId();
        final String elementId = ((Widget)this.m_element).getId();
        if (mapId == null) {
            return false;
        }
        final PreferenceStore pref = Xulor.getInstance().getUserDefinedAdapter().getPreferenceStoreForDialog(mapId);
        if (pref == null) {
            return false;
        }
        final String x = XulorUtil.generatePreferenceKey(mapId, elementId, "x");
        final String y = XulorUtil.generatePreferenceKey(mapId, elementId, "y");
        final String width = XulorUtil.generatePreferenceKey(mapId, elementId, "width");
        final String height = XulorUtil.generatePreferenceKey(mapId, elementId, "height");
        return pref.contains(x) || pref.contains(y) || pref.contains(width) || pref.contains(height);
    }
    
    public boolean isUsePosition() {
        return this.m_usePosition;
    }
    
    public void setUsePosition(final boolean usePosition) {
        this.m_usePosition = usePosition;
    }
    
    public boolean isUseSize() {
        return this.m_useSize;
    }
    
    public void setUseSize(final boolean useSize) {
        this.m_useSize = useSize;
    }
    
    public void positionChanged() {
        this.m_positionChanged = true;
    }
    
    public void sizeChanged() {
        this.m_sizeChanged = true;
    }
}
