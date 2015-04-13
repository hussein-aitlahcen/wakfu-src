package com.ankamagames.wakfu.client.core.game.wakfu;

import com.ankamagames.wakfu.common.game.wakfu.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.world.*;
import java.util.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.xulor2.property.*;

public class WakfuGlobalZoneManager extends BaseWakfuZoneManager implements FieldProvider
{
    private static final WakfuGlobalZoneManager m_instance;
    public static final String PROTECTOR_SATISACTION_COLOR_FIELD = "protectorSatisfactionColor";
    public static final String PROTECTOR_SATISFACTION_FIELD = "protectorSatisfaction";
    private static final String[] FIELDS;
    private WorldInfoManager.WorldInfo m_worldInfo;
    private int m_ambianceZoneId;
    
    public WakfuGlobalZoneManager() {
        super();
        this.m_ambianceZoneId = -1;
    }
    
    @Override
    public String[] getFields() {
        return WakfuGlobalZoneManager.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("protectorSatisfactionColor")) {
            return this.getProtectorSatisfactionColor();
        }
        if (fieldName.equals("protectorSatisfaction")) {
            return this.getSatisfactionSortedList();
        }
        return null;
    }
    
    public int getWillCount() {
        int count = 0;
        for (final WakfuEcosystemFamilyInfo wakfuEcosystemFamilyInfo : WakfuMonsterZoneManager.getInstance().getMonsterFamilyInfos()) {
            if (wakfuEcosystemFamilyInfo.hasProtectorInterval()) {
                ++count;
            }
        }
        for (final WakfuEcosystemFamilyInfo wakfuEcosystemFamilyInfo : WakfuResourceZoneManager.getInstance().getResourceFamilyInfos()) {
            if (wakfuEcosystemFamilyInfo.hasProtectorInterval()) {
                ++count;
            }
        }
        return count;
    }
    
    private ArrayList<Boolean> getSatisfactionSortedList() {
        final ArrayList<Boolean> satisfactions = new ArrayList<Boolean>();
        for (final WakfuEcosystemFamilyInfo wakfuEcosystemFamilyInfo : WakfuMonsterZoneManager.getInstance().getMonsterFamilyInfos()) {
            if (wakfuEcosystemFamilyInfo.hasProtectorInterval()) {
                satisfactions.add(wakfuEcosystemFamilyInfo.isInProtectorInterval());
            }
        }
        for (final WakfuEcosystemFamilyInfo wakfuEcosystemFamilyInfo : WakfuResourceZoneManager.getInstance().getResourceFamilyInfos()) {
            if (wakfuEcosystemFamilyInfo.hasProtectorInterval()) {
                satisfactions.add(wakfuEcosystemFamilyInfo.isInProtectorInterval());
            }
        }
        Collections.sort(satisfactions, new Comparator<Boolean>() {
            @Override
            public int compare(final Boolean o1, final Boolean o2) {
                return o2.compareTo(o1);
            }
        });
        return satisfactions;
    }
    
    private Color getProtectorSatisfactionColor() {
        final Protector protector = ProtectorView.getInstance().getProtector();
        if (protector == null) {
            return null;
        }
        final ProtectorSatisfactionLevel protectorSatisfactionLevel = protector.getSatisfactionLevel();
        if (protectorSatisfactionLevel == null) {
            return null;
        }
        switch (protectorSatisfactionLevel) {
            case SATISFIED: {
                return Color.GREEN;
            }
            case HALF_SATISFIED: {
                return Color.WHITE;
            }
            case UNSATISFIED: {
                return Color.RED;
            }
            default: {
                return null;
            }
        }
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
    
    public static WakfuGlobalZoneManager getInstance() {
        return WakfuGlobalZoneManager.m_instance;
    }
    
    public void fireProtectorSatisfactionLevel() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "protectorSatisfactionColor");
    }
    
    public void updateZoneEquilibrium() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "protectorSatisfactionColor", "protectorSatisfaction");
    }
    
    public void setAmbianceZoneId(final int ambianceZoneId) {
        this.m_ambianceZoneId = ambianceZoneId;
    }
    
    public void setWorldInfo(final WorldInfoManager.WorldInfo worldInfo) {
        this.m_worldInfo = worldInfo;
    }
    
    public WorldInfoManager.WorldInfo.AmbienceZone getCurrentAmbienceZone() {
        return this.m_worldInfo.getAmbienceZone(this.m_ambianceZoneId);
    }
    
    public float getZoneEquilibrium() {
        return (WakfuMonsterZoneManager.getInstance().getCurrentRatio() + WakfuResourceZoneManager.getInstance().getCurrentRatio()) / 2.0f;
    }
    
    static {
        m_instance = new WakfuGlobalZoneManager();
        FIELDS = new String[] { "protectorSatisfactionColor", "protectorSatisfactionColor" };
    }
}
