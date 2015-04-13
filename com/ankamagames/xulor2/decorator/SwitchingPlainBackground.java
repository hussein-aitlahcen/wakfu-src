package com.ankamagames.xulor2.decorator;

import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.decorator.mesh.*;

public class SwitchingPlainBackground extends Background
{
    public static final String TAG = "SwitchingPlainBackground";
    private SwitchingPlainBackgroundMesh m_mesh;
    public static final int DURATION_HASH;
    
    public SwitchingPlainBackground() {
        super();
        this.m_mesh = new SwitchingPlainBackgroundMesh();
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof ColorElement) {
            this.m_mesh.addColor(((ColorElement)e).getColor());
        }
        super.add(e);
    }
    
    @Override
    public String getTag() {
        return "SwitchingPlainBackground";
    }
    
    @Override
    public AbstractBackgroundMesh getMesh() {
        return this.m_mesh;
    }
    
    @Override
    public Entity getEntity() {
        return null;
    }
    
    public int getDuration() {
        return this.m_mesh.getDuration();
    }
    
    public void setDuration(final int duration) {
        this.m_mesh.setDuration(duration);
    }
    
    @Override
    public void copyElement(final BasicElement b) {
        super.copyElement(b);
        final SwitchingPlainBackground s = (SwitchingPlainBackground)b;
        s.setDuration(this.getDuration());
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_mesh.onCheckIn();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_mesh.onCheckOut();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == SwitchingPlainBackground.DURATION_HASH) {
            this.setDuration(PrimitiveConverter.getInteger(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == SwitchingPlainBackground.DURATION_HASH) {
            this.setDuration(PrimitiveConverter.getInteger(value));
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        DURATION_HASH = "duration".hashCode();
    }
}
