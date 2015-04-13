package com.ankamagames.wakfu.client.core.game.protector;

import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.rawData.*;
import gnu.trove.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.protector.*;

public class ProtectorManager extends ProtectorManagerBase<Protector>
{
    private final TIntIntHashMap m_protectorByTerritory;
    private final BinaryLoader<ProtectorBinaryData> m_creatorProtectorStatic;
    private final BinaryLoader<ProtectorBinaryData> m_creatorProtector;
    public static final ProtectorManager INSTANCE;
    
    private ProtectorManager() {
        super();
        this.m_protectorByTerritory = new TIntIntHashMap();
        this.m_creatorProtector = new BinaryLoaderFromFile<ProtectorBinaryData>(new ProtectorBinaryData());
        this.m_creatorProtectorStatic = new BinaryLoaderFromFile<ProtectorBinaryData>(new ProtectorBinaryData());
        this.loadProtectorsTerritories();
    }
    
    @Override
    public boolean registerProtector(final Protector protector) {
        final ProtectorBinaryData data = this.m_creatorProtectorStatic.createFromId(protector.getId());
        if (data == null) {
            return false;
        }
        final ProtectorStaticData psd = loadStaticFromBinaryForm(data);
        final Point3 pos = psd.getPositionConst();
        protector.setPosition(pos.getX(), pos.getY(), pos.getZ());
        for (final ProtectorSecret secret : psd.getSecrets()) {
            protector.addSecret(secret);
        }
        protector.setCraft(psd.getCraft());
        return super.registerProtector(protector);
    }
    
    @Nullable
    public Protector getStaticProtector(final int id) {
        final Protector protector = super.getProtector(id);
        if (protector != null) {
            return protector;
        }
        final ProtectorBinaryData data = this.m_creatorProtector.createFromId(id);
        if (data == null) {
            return null;
        }
        final Protector loadedProtector = loadFromBinaryForm(data);
        loadedProtector.setPosition(data.getPositionX(), data.getPositionY(), data.getPositionZ());
        return loadedProtector;
    }
    
    @Nullable
    public Protector getStaticProtectorByTerritoryId(final int id) {
        final int protectorId = this.m_protectorByTerritory.get(id);
        if (protectorId != 0) {
            return this.getStaticProtector(protectorId);
        }
        return null;
    }
    
    public final void loadProtectorsTerritories() {
        this.m_protectorByTerritory.clear();
        try {
            BinaryDocumentManager.getInstance().foreach(new ProtectorBinaryData(), new LoadProcedure<ProtectorBinaryData>() {
                @Override
                public void load(final ProtectorBinaryData data) {
                    ProtectorManager.this.m_protectorByTerritory.put(data.getTerritory(), data.getProtectorId());
                }
            });
        }
        catch (Exception e) {
            ProtectorManager.m_logger.error((Object)"", (Throwable)e);
        }
    }
    
    private static ProtectorStaticData loadStaticFromBinaryForm(final ProtectorBinaryData bs) {
        final ProtectorStaticData psd = new ProtectorStaticData(bs.getProtectorId());
        psd.setPosition(bs.getPositionX(), bs.getPositionY(), bs.getPositionZ());
        for (final ProtectorBinaryData.ProtectorSecret secret : bs.getSecrets()) {
            psd.addSecret(new ProtectorSecret(secret.getId(), secret.getAchievementGoalId(), secret.getSecretGfxId(), secret.getDiscoveredGfxId()));
        }
        psd.setCraft(bs.getCraftLearnt());
        return psd;
    }
    
    private static Protector loadFromBinaryForm(final ProtectorBinaryData bs) {
        final int id = bs.getProtectorId();
        final int nativeNationId = bs.getNationId();
        final int territoryId = bs.getTerritory();
        final int monsterCrewId = bs.getMonsterId();
        final TIntObjectHashMap<Interval> monsterObjectives = new TIntObjectHashMap<Interval>();
        for (final ProtectorBinaryData.ProtectorFaunaWill will : bs.getFaunaWill()) {
            monsterObjectives.put(will.getTypeId(), new Interval(will.getMin(), will.getMax()));
        }
        final TIntObjectHashMap<Interval> resourceObjectives = new TIntObjectHashMap<Interval>();
        for (final ProtectorBinaryData.ProtectorFloraWill will2 : bs.getFloraWill()) {
            resourceObjectives.put(will2.getTypeId(), new Interval(will2.getMin(), will2.getMax()));
        }
        final RawProtector rawProtector = new RawProtector();
        rawProtector.protectorId = id;
        rawProtector.nationality = new RawProtector.Nationality();
        rawProtector.nationality.nativeNationId = nativeNationId;
        rawProtector.nationality.currentNationId = nativeNationId;
        rawProtector.nationality.territoryId = territoryId;
        rawProtector.appearance = new RawProtector.Appearance();
        rawProtector.appearance.monsterCrewId = monsterCrewId;
        rawProtector.appearance.monterId = -1L;
        try {
            BinaryDocumentManager.getInstance().forId(bs.getBuffListId(), new ProtectorBuffListBinaryData(), new LoadProcedure<ProtectorBuffListBinaryData>() {
                @Override
                public void load(final ProtectorBuffListBinaryData data) {
                    for (final int buffId : data.getBuffLists()) {
                        final RawProtectorReferenceInventory.Content content = new RawProtectorReferenceInventory.Content();
                        content.referenceId = buffId;
                        if (rawProtector.referenceMerchantInventories == null) {
                            rawProtector.referenceMerchantInventories = new RawProtector.ReferenceMerchantInventories();
                        }
                        rawProtector.referenceMerchantInventories.buffsReferenceInventory.contents.add(content);
                    }
                }
            });
        }
        catch (Exception e) {
            ProtectorManager.m_logger.error((Object)("probl\u00e8me avec protector " + id), (Throwable)e);
        }
        rawProtector.monsterTargets = new RawProtector.MonsterTargets();
        monsterObjectives.forEachEntry(new TIntObjectProcedure<Interval>() {
            @Override
            public boolean execute(final int familyId, final Interval target) {
                final RawProtector.MonsterTargets.MonsterTarget monsterTarget = new RawProtector.MonsterTargets.MonsterTarget();
                monsterTarget.target.referenceId = familyId;
                monsterTarget.target.min = target.getMin();
                monsterTarget.target.max = target.getMax();
                rawProtector.monsterTargets.targets.add(monsterTarget);
                return true;
            }
        });
        rawProtector.resourceTargets = new RawProtector.ResourceTargets();
        resourceObjectives.forEachEntry(new TIntObjectProcedure<Interval>() {
            @Override
            public boolean execute(final int familyId, final Interval target) {
                final RawProtector.ResourceTargets.ResourceTarget resourceTarget = new RawProtector.ResourceTargets.ResourceTarget();
                resourceTarget.target.referenceId = familyId;
                resourceTarget.target.min = target.getMin();
                resourceTarget.target.max = target.getMax();
                rawProtector.resourceTargets.targets.add(resourceTarget);
                return true;
            }
        });
        final ByteBuffer bb = ByteBuffer.wrap(ProtectorSerializer.INSTANCE.serializeRawProtector(rawProtector));
        final Protector protector = ProtectorStaticSerializer.INSTANCE.fromBuild(bb);
        for (final ProtectorBinaryData.ProtectorSecret secret : bs.getSecrets()) {
            protector.addSecret(new ProtectorSecret(secret.getId(), secret.getAchievementGoalId(), secret.getSecretGfxId(), secret.getDiscoveredGfxId()));
        }
        return protector;
    }
    
    static {
        INSTANCE = new ProtectorManager();
    }
}
