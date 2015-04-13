package com.ankamagames.wakfu.client.core.game.wakfu;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;

public class WakfuEcosystemFamilyInfo implements FieldProvider
{
    private static final String FAMILY_NAME_FIELD = "name";
    private static final String FAMILY_ICON_URL_FIELD = "familyIconUrl";
    private static final String FAMILY_ICON_STYLE_FIELD = "familyIconStyle";
    private static final String GAUGE_COLOR_FIELD = "gaugeColor";
    private static final String CURRENT_VALUE_FIELD = "currentValue";
    private static final String CURRENT_VALUE_PERC_FIELD = "currentValuePerc";
    private static final String CURRENT_VALUE_TEXT_FIELD = "currentValueText";
    private static final String IS_ON_OVERFLOW_FIELD = "isOnOverflow";
    private static final String PROTECTOR_INTERVAL_FIELD = "protectorInterval";
    private static final String IS_PROTECTOR_FIELD = "isProtector";
    private static final String MAX_VALUE_TEXT_FIELD = "maxValueText";
    private static final String WAKFU_GAUGE_STYLE_FIELD = "wakfuGaugeStyle";
    private static final String NUMBER_IN_ZONE_TEXT_FIELD = "numberInZoneText";
    private static final String INTERVAL_TEXT_FIELD = "intervalText";
    private static final String[] FIELDS;
    protected static final Logger m_logger;
    private EcosystemFamilyType m_familyType;
    private int m_familyId;
    private int m_currentValue;
    private int m_maxValue;
    private IntervalInformationFieldProvider m_protectorInterval;
    private boolean m_reintroducing;
    
    public WakfuEcosystemFamilyInfo(final EcosystemFamilyType familyType, final int familyId, Interval protectorInterval, final int value, final int maxValue) {
        super();
        this.m_reintroducing = false;
        this.m_familyType = familyType;
        this.m_familyId = familyId;
        this.m_currentValue = value;
        this.m_maxValue = maxValue;
        if (protectorInterval == null && StaticProtectorView.INSTANCE.getProtectorId() != -1) {
            protectorInterval = StaticProtectorView.INSTANCE.getProtectorInterval(familyId);
        }
        this.m_protectorInterval = ((protectorInterval == null) ? null : new IntervalInformationFieldProvider(protectorInterval, this.m_maxValue));
    }
    
    @Override
    public String[] getFields() {
        return WakfuEcosystemFamilyInfo.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("protectorInterval")) {
            return this.m_protectorInterval;
        }
        if (fieldName.equals("currentValue")) {
            return this.getCurrentRatio();
        }
        if (fieldName.equals("currentValuePerc")) {
            return new Percentage(this.getCurrentRatio() * 100.0f);
        }
        if (fieldName.equals("currentValueText")) {
            return (this.m_currentValue > 100) ? UIEcosystemUtils.getRoundedEcosystemValue(this.m_currentValue) : this.m_currentValue;
        }
        if (fieldName.equals("maxValueText")) {
            return this.m_maxValue;
        }
        if (fieldName.equals("isOnOverflow")) {
            return this.m_currentValue > this.m_maxValue;
        }
        if (!fieldName.equals("familyIconStyle")) {
            if (fieldName.equals("familyIconUrl")) {
                if (!this.m_familyType.isMonster()) {
                    return null;
                }
                try {
                    return String.format(WakfuConfiguration.getInstance().getString("monstersFamily"), this.m_familyId);
                }
                catch (PropertyException e) {
                    WakfuEcosystemFamilyInfo.m_logger.error((Object)"Exception", (Throwable)e);
                    return null;
                }
            }
            if (fieldName.equals("isProtector")) {
                return this.isInProtectorInterval();
            }
            if (fieldName.equals("wakfuGaugeStyle")) {
                final float ratio = this.getCurrentRatio();
                if (ratio > 0.65) {
                    return "CurrentWakfuBig";
                }
                if (ratio > 0.35) {
                    return "CurrentStasisBig";
                }
                return "CurrentStasisBig";
            }
            else {
                if (fieldName.equals("numberInZoneText")) {
                    return WakfuTranslator.getInstance().getString("wakfu.currentResourceValueInZone", this.getName());
                }
                if (fieldName.equals("intervalText")) {
                    if (this.m_protectorInterval == null) {
                        return null;
                    }
                    return WakfuTranslator.getInstance().getString("wakfu.protectorInterval", this.getName(), this.m_protectorInterval.getIntervalMinValue(), this.m_protectorInterval.getIntervalMaxValue());
                }
            }
            return null;
        }
        if (this.m_familyType.isMonster()) {
            return null;
        }
        switch (this.m_familyId) {
            case 10: {
                return "PlantsIcon";
            }
            case 1: {
                return "TreesIcon";
            }
            case 2: {
                return "FarmingIcon";
            }
            default: {
                return "";
            }
        }
    }
    
    public float getCurrentRatio() {
        return Math.min(this.m_currentValue, this.m_maxValue) / this.m_maxValue;
    }
    
    public String getName() {
        String familyName;
        if (this.m_familyType.isMonster()) {
            familyName = WakfuTranslator.getInstance().getString(38, this.m_familyId, new Object[0]);
        }
        else {
            familyName = WakfuTranslator.getInstance().getString(37, this.m_familyId, new Object[0]);
        }
        if (familyName != null && !familyName.isEmpty()) {
            return familyName;
        }
        return "Famille inconnue";
    }
    
    public boolean hasProtectorInterval() {
        return this.m_protectorInterval != null;
    }
    
    public void setProtectorInterval(final Interval protectorInterval) {
        this.m_protectorInterval = new IntervalInformationFieldProvider(protectorInterval, this.m_maxValue);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, WakfuEcosystemFamilyInfo.FIELDS);
    }
    
    public boolean isInProtectorInterval() {
        return this.m_protectorInterval != null && this.m_protectorInterval.isInInterval(this.m_currentValue);
    }
    
    public int getProtectorIntervalDiffFromMax() {
        return (this.m_protectorInterval == null) ? -1 : (this.m_currentValue - this.m_protectorInterval.getIntervalMaxValue());
    }
    
    public EcosystemFamilyType getFamilyType() {
        return this.m_familyType;
    }
    
    public int getFamilyId() {
        return this.m_familyId;
    }
    
    public void setReintroducing(final boolean reintroducing) {
        this.m_reintroducing = reintroducing;
    }
    
    public boolean isReintroducing() {
        return this.m_reintroducing;
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
    
    public int getCurrentValue() {
        return this.m_currentValue;
    }
    
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("(");
        buffer.append("familyId=").append(this.m_familyId).append(", ");
        buffer.append("currentValue=").append(this.m_currentValue);
        buffer.append(")");
        return buffer.toString();
    }
    
    static {
        FIELDS = new String[] { "name", "gaugeColor", "currentValue", "familyIconUrl", "currentValueText", "protectorInterval", "maxValueText", "isProtector", "wakfuGaugeStyle", "numberInZoneText", "intervalText" };
        m_logger = Logger.getLogger((Class)WakfuEcosystemFamilyInfo.class);
    }
}
