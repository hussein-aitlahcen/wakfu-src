package com.ankamagames.xulor2.decorator;

import org.apache.log4j.*;
import java.awt.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.decorator.mesh.*;

public class BubbleBorder extends InsetsBorder
{
    private static Logger m_logger;
    public static final String TAG = "BubbleBorder";
    private BubbleBorderMesh m_mesh;
    private boolean m_displaySpark;
    private boolean m_displaySparkInit;
    private float m_sparkAngle;
    private boolean m_sparkAngleInit;
    public static final int DISPLAY_SPARK_HASH;
    public static final int SPARK_ANGLE_HASH;
    
    public BubbleBorder() {
        super();
        this.m_displaySpark = true;
        this.m_displaySparkInit = false;
        this.m_sparkAngle = -2.0943952f;
        this.m_sparkAngleInit = false;
    }
    
    @Override
    public String getTag() {
        return "BubbleBorder";
    }
    
    @Override
    public BubbleBorderMesh getMesh() {
        return this.m_mesh;
    }
    
    @Override
    public void setInsets(final Insets insets) {
    }
    
    @Override
    public Entity getEntity() {
        return this.m_mesh.getEntity();
    }
    
    public void setDisplaySpark(final boolean displaySpark) {
        this.m_displaySpark = displaySpark;
        this.m_displaySparkInit = true;
        this.m_mesh.setDisplaySpark(displaySpark);
    }
    
    public void setSparkAngle(final float angle) {
        this.m_sparkAngle = angle;
        this.m_sparkAngleInit = true;
        if (this.m_displaySpark) {
            this.m_mesh.setSparkAngle(angle);
        }
    }
    
    @Override
    public void copyElement(final BasicElement b) {
        super.copyElement(b);
        final BubbleBorder bb = (BubbleBorder)b;
        if (this.m_displaySparkInit) {
            bb.setDisplaySpark(this.m_displaySpark);
        }
        if (this.m_sparkAngleInit) {
            bb.setSparkAngle(this.m_sparkAngle);
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_mesh.onCheckIn();
        this.m_mesh = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_sparkAngle = -2.0943952f;
        this.m_displaySpark = true;
        (this.m_mesh = new BubbleBorderMesh()).onCheckOut();
        super.setInsets(this.m_mesh.getInsets());
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == BubbleBorder.DISPLAY_SPARK_HASH) {
            this.setDisplaySpark(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != BubbleBorder.SPARK_ANGLE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setSparkAngle(PrimitiveConverter.getFloat(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == BubbleBorder.DISPLAY_SPARK_HASH) {
            this.setDisplaySpark(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != BubbleBorder.SPARK_ANGLE_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setSparkAngle(PrimitiveConverter.getFloat(value));
        }
        return true;
    }
    
    static {
        BubbleBorder.m_logger = Logger.getLogger((Class)BubbleBorder.class);
        DISPLAY_SPARK_HASH = "displaySpark".hashCode();
        SPARK_ANGLE_HASH = "sparkAngle".hashCode();
    }
}
