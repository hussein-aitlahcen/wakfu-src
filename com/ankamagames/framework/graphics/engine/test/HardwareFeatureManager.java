package com.ankamagames.framework.graphics.engine.test;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import java.util.*;

public class HardwareFeatureManager implements FieldProvider
{
    protected static final Logger m_logger;
    public static final HardwareFeatureManager INSTANCE;
    private final EnumMap<HardwareFeature, Boolean> m_features;
    private static final String[] FIELDS;
    
    private HardwareFeatureManager() {
        super();
        this.m_features = new EnumMap<HardwareFeature, Boolean>(HardwareFeature.class);
    }
    
    public void setFeatureSupported(final HardwareFeature feature, final boolean isSupported) {
        this.m_features.put(feature, Boolean.valueOf(isSupported));
    }
    
    public boolean isFeatureSupported(final HardwareFeature feature) {
        final Boolean isSupported = this.m_features.get(feature);
        return isSupported != null && isSupported;
    }
    
    @Override
    public String[] getFields() {
        return HardwareFeatureManager.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        return this.isFeatureSupported(HardwareFeature.getFeatureByPropertyName(fieldName));
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
    
    public boolean isShaderSupported() {
        return this.isFeatureSupported(HardwareFeature.GL_FRAGMENT_SHADERS) && this.isFeatureSupported(HardwareFeature.GL_VERTEX_SHADERS);
    }
    
    static {
        m_logger = Logger.getLogger((Class)HardwareFeatureManager.class);
        INSTANCE = new HardwareFeatureManager();
        final HardwareFeature[] features = HardwareFeature.values();
        FIELDS = new String[features.length];
        for (int i = 0; i < features.length; ++i) {
            HardwareFeatureManager.FIELDS[i] = features[i].getPropertyName();
        }
    }
}
