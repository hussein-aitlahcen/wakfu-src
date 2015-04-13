package com.ankamagames.wakfu.client.core.game.wakfu;

import com.ankamagames.wakfu.common.game.wakfu.*;
import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import java.nio.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.ecosystem.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public class WakfuResourceZoneManager extends BaseWakfuZoneManager implements FieldProvider
{
    protected static final Logger m_logger;
    private static final String RESOURCE_FAMILY_RATIOS = "resourceFamilyRatios";
    private static final String[] FIELDS;
    private static WakfuResourceZoneManager m_instance;
    private ArrayList<WakfuEcosystemFamilyInfo> m_resourceFamilyInfos;
    
    private WakfuResourceZoneManager() {
        super();
        this.m_resourceFamilyInfos = new ArrayList<WakfuEcosystemFamilyInfo>();
    }
    
    public void unserialize(final byte[] serializedResourceInformation) {
        this.m_resourceFamilyInfos.clear();
        final ByteBuffer buffer = ByteBuffer.wrap(serializedResourceInformation);
        final TIntIntHashMap currentResourceCount = new TIntIntHashMap();
        final int resourceFamilyCount = buffer.getInt();
        final WorldInfoManager.WorldInfo.AmbienceZone currentAmbienceZone = WakfuGlobalZoneManager.getInstance().getCurrentAmbienceZone();
        if (currentAmbienceZone == null) {
            WakfuResourceZoneManager.m_logger.warn((Object)"R\u00e9ception d'information de ressources dans une zone inconnue du client");
            return;
        }
        final ResourceBalancing resourceBalancing = currentAmbienceZone.getResources();
        final Protector protector = ProtectorView.getInstance().getProtector();
        final ProtectorSatisfactionManager satisfactionManager = (protector != null) ? protector.getSatisfactionManager() : null;
        for (int i = 0; i < resourceFamilyCount; ++i) {
            final int resourceFamilyId = buffer.getInt();
            final int value = buffer.getInt();
            Interval protectorInterval = null;
            if (satisfactionManager != null) {
                protectorInterval = satisfactionManager.getResourceInterval(resourceFamilyId);
            }
            final int maxValue = resourceBalancing.m_stasisThreshold;
            currentResourceCount.put(resourceFamilyId, value);
            if (currentAmbienceZone.isResourceAuthorized(resourceFamilyId)) {
                this.m_resourceFamilyInfos.add(new WakfuEcosystemFamilyInfo(EcosystemFamilyType.getResourceTypeFromIndex(i), resourceFamilyId, protectorInterval, value, maxValue));
            }
        }
        if (satisfactionManager != null) {
            satisfactionManager.setResourceSatisfaction(-1);
            satisfactionManager.updateResourceSatisfaction(currentResourceCount);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, WakfuResourceZoneManager.FIELDS);
    }
    
    public WakfuEcosystemFamilyInfo getResourceFamilyInfo(final int familyId) {
        for (int i = 0, size = this.m_resourceFamilyInfos.size(); i < size; ++i) {
            final WakfuEcosystemFamilyInfo familyInfo = this.m_resourceFamilyInfos.get(i);
            if (familyInfo.getFamilyId() == familyId) {
                return familyInfo;
            }
        }
        return null;
    }
    
    public ArrayList<WakfuEcosystemFamilyInfo> getResourceFamilyInfos() {
        return this.m_resourceFamilyInfos;
    }
    
    public float getCurrentRatio() {
        float computedRatios = 0.0f;
        for (final WakfuEcosystemFamilyInfo wakfuEcosystemFamilyInfo : this.m_resourceFamilyInfos) {
            computedRatios += wakfuEcosystemFamilyInfo.getCurrentRatio();
        }
        return computedRatios / this.m_resourceFamilyInfos.size();
    }
    
    public static WakfuResourceZoneManager getInstance() {
        return WakfuResourceZoneManager.m_instance;
    }
    
    @Override
    public String[] getFields() {
        return WakfuResourceZoneManager.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("resourceFamilyRatios")) {
            return (this.m_resourceFamilyInfos.size() > 0) ? this.m_resourceFamilyInfos : null;
        }
        return null;
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
    
    public void clear() {
        this.m_resourceFamilyInfos.clear();
    }
    
    public void debugPrint() {
        WakfuResourceZoneManager.m_logger.info((Object)"#################################################");
        for (int i = 0, size = this.m_resourceFamilyInfos.size(); i < size; ++i) {
            final WakfuEcosystemFamilyInfo familyInfo = this.m_resourceFamilyInfos.get(i);
            WakfuResourceZoneManager.m_logger.info((Object)familyInfo);
        }
        WakfuResourceZoneManager.m_logger.info((Object)"#################################################");
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuResourceZoneManager.class);
        FIELDS = new String[] { "resourceFamilyRatios" };
        WakfuResourceZoneManager.m_instance = new WakfuResourceZoneManager();
    }
}
