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

public class WakfuMonsterZoneManager extends BaseWakfuZoneManager implements FieldProvider
{
    protected static final Logger m_logger;
    private static final String MONSTER_FAMILY_RATIOS = "monsterFamilyRatios";
    private static final String[] FIELDS;
    private static WakfuMonsterZoneManager m_instance;
    private ArrayList<WakfuEcosystemFamilyInfo> m_monsterFamilyInfos;
    
    private WakfuMonsterZoneManager() {
        super();
        this.m_monsterFamilyInfos = new ArrayList<WakfuEcosystemFamilyInfo>();
    }
    
    public void unserialize(final byte[] serializedMonsterInformation) {
        this.m_monsterFamilyInfos.clear();
        final ByteBuffer buffer = ByteBuffer.wrap(serializedMonsterInformation);
        final TIntIntHashMap currentMonsterCount = new TIntIntHashMap();
        final int monsterFamilyCount = buffer.getInt();
        final WorldInfoManager.WorldInfo.AmbienceZone currentAmbienceZone = WakfuGlobalZoneManager.getInstance().getCurrentAmbienceZone();
        if (currentAmbienceZone == null) {
            WakfuMonsterZoneManager.m_logger.warn((Object)"R\u00e9ception d'information de monstres dans une zone inconnue du client");
            return;
        }
        final MonsterBalancing monsterBalancing = currentAmbienceZone.getMonsters();
        final Protector protector = ProtectorView.getInstance().getProtector();
        final ProtectorSatisfactionManager satisfactionManager = (protector != null) ? protector.getSatisfactionManager() : null;
        for (int i = 0; i < monsterFamilyCount; ++i) {
            final int monsterFamilyId = buffer.getInt();
            final int monsterCount = buffer.getInt();
            Interval protectorInterval = null;
            if (satisfactionManager != null) {
                protectorInterval = satisfactionManager.getMonsterInterval(monsterFamilyId);
            }
            final int maxValue = monsterBalancing.m_stasisThreshold;
            currentMonsterCount.put(monsterFamilyId, monsterCount);
            this.m_monsterFamilyInfos.add(new WakfuEcosystemFamilyInfo(EcosystemFamilyType.getMonsterTypeFromIndex(i), monsterFamilyId, protectorInterval, monsterCount, maxValue));
        }
        if (satisfactionManager != null) {
            satisfactionManager.setMonsterSatisfaction(-1);
            satisfactionManager.updateMonsterSatisfaction(currentMonsterCount);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, WakfuMonsterZoneManager.FIELDS);
    }
    
    public WakfuEcosystemFamilyInfo getMonsterFamilyInfo(final int familyId) {
        for (int i = 0, size = this.m_monsterFamilyInfos.size(); i < size; ++i) {
            final WakfuEcosystemFamilyInfo familyInfo = this.m_monsterFamilyInfos.get(i);
            if (familyInfo.getFamilyId() == familyId) {
                return familyInfo;
            }
        }
        return null;
    }
    
    public ArrayList<WakfuEcosystemFamilyInfo> getMonsterFamilyInfos() {
        return this.m_monsterFamilyInfos;
    }
    
    public float getCurrentRatio() {
        float computedRatios = 0.0f;
        for (final WakfuEcosystemFamilyInfo wakfuEcosystemFamilyInfo : this.m_monsterFamilyInfos) {
            computedRatios += wakfuEcosystemFamilyInfo.getCurrentRatio();
        }
        return computedRatios / this.m_monsterFamilyInfos.size();
    }
    
    public static WakfuMonsterZoneManager getInstance() {
        return WakfuMonsterZoneManager.m_instance;
    }
    
    @Override
    public String[] getFields() {
        return WakfuMonsterZoneManager.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("monsterFamilyRatios")) {
            return (this.m_monsterFamilyInfos.size() > 0) ? this.m_monsterFamilyInfos : null;
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
        this.m_monsterFamilyInfos.clear();
    }
    
    public void debugPrint() {
        WakfuMonsterZoneManager.m_logger.info((Object)"#################################################");
        for (int i = 0, size = this.m_monsterFamilyInfos.size(); i < size; ++i) {
            final WakfuEcosystemFamilyInfo familyInfo = this.m_monsterFamilyInfos.get(i);
            WakfuMonsterZoneManager.m_logger.info((Object)familyInfo);
        }
        WakfuMonsterZoneManager.m_logger.info((Object)"#################################################");
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuMonsterZoneManager.class);
        FIELDS = new String[] { "monsterFamilyRatios" };
        WakfuMonsterZoneManager.m_instance = new WakfuMonsterZoneManager();
    }
}
