package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;

public class NationRankEquipmentHelper
{
    private static final Logger m_logger;
    private static final TByteObjectHashMap<EquipmentsByNation> EQUIPMENTS;
    
    private static EquipmentsByNation createGovernorEquipements() {
        final EquipmentsByNation equipments = new EquipmentsByNation();
        final Equipment commonHead = new Equipment(EquipmentPosition.HEAD, 1349907);
        equipments.add(30, commonHead, new Equipment(EquipmentPosition.BACK, 1329924));
        equipments.add(31, commonHead, new Equipment(EquipmentPosition.BACK, 1329925));
        equipments.add(32, commonHead, new Equipment(EquipmentPosition.BACK, 1329926));
        equipments.add(33, commonHead, new Equipment(EquipmentPosition.BACK, 13210733));
        return equipments;
    }
    
    private static EquipmentsByNation createGeneralEquipements() {
        final EquipmentsByNation equipments = new EquipmentsByNation();
        equipments.add(30, new Equipment(EquipmentPosition.BACK, 1349913));
        equipments.add(31, new Equipment(EquipmentPosition.BACK, 1349912));
        equipments.add(32, new Equipment(EquipmentPosition.BACK, 1349911));
        equipments.add(33, new Equipment(EquipmentPosition.BACK, 13410435));
        return equipments;
    }
    
    private static EquipmentsByNation createMarshalEquipements() {
        final EquipmentsByNation equipments = new EquipmentsByNation();
        equipments.add(30, new Equipment(EquipmentPosition.BACK, 1349909));
        equipments.add(31, new Equipment(EquipmentPosition.BACK, 1349908));
        equipments.add(32, new Equipment(EquipmentPosition.BACK, 1349910));
        equipments.add(33, new Equipment(EquipmentPosition.BACK, 13410434));
        return equipments;
    }
    
    private static EquipmentsByNation createForAllNation(final Equipment... equipements) {
        final EquipmentsByNation map = new EquipmentsByNation();
        map.add(30, equipements);
        map.add(31, equipements);
        map.add(32, equipements);
        map.add(33, equipements);
        return map;
    }
    
    public static void foreachEquipement(final NationRank rank, final int nationId, final TObjectIntProcedure<EquipmentPosition> procedure) {
        final EquipmentsByNation equipmentsByNation = NationRankEquipmentHelper.EQUIPMENTS.get(rank.getBaseId());
        if (equipmentsByNation == null) {
            NationRankEquipmentHelper.m_logger.error((Object)("Pas d'equiepement d\u00e9fini pour le rank " + rank));
            return;
        }
        final Equipment[] equipements = equipmentsByNation.getEquipements(nationId);
        if (equipements == null) {
            NationRankEquipmentHelper.m_logger.error((Object)("Pas d'equiepement d\u00e9fini pour le rank " + rank + " pour la nation " + nationId));
            return;
        }
        for (int i = 0, size = equipements.length; i < size; ++i) {
            if (!procedure.execute(equipements[i].position, equipements[i].gfxId)) {
                return;
            }
        }
    }
    
    public static int getParticleForRank(final NationRank rank) {
        if (rank == null) {
            return -1;
        }
        switch (rank) {
            case GOVERNOR: {
                return 800205;
            }
            case DEPUTY: {
                return 800216;
            }
            case GENERAL: {
                return 800215;
            }
            case MARSHAL: {
                return 800214;
            }
            case TREASURER: {
                return 800217;
            }
            case CHALLENGER: {
                return 800218;
            }
            case METEOROLOGIST: {
                return 800219;
            }
            case ZOOLOGIST: {
                return 800221;
            }
            default: {
                return -1;
            }
        }
    }
    
    public static int[] getAllGfxIds() {
        final TIntHashSet gfxIds = new TIntHashSet();
        NationRankEquipmentHelper.EQUIPMENTS.forEachValue(new TObjectProcedure<EquipmentsByNation>() {
            @Override
            public boolean execute(final EquipmentsByNation object) {
                for (int i = 0, size = object.m_equipments.size(); i < size; ++i) {
                    for (final Equipment e : object.m_equipments.getQuickValue(i)) {
                        gfxIds.add(e.gfxId);
                    }
                }
                return true;
            }
        });
        return gfxIds.toArray();
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationRankEquipmentHelper.class);
        (EQUIPMENTS = new TByteObjectHashMap<EquipmentsByNation>()).put(NationRank.GOVERNOR.getBaseId(), createGovernorEquipements());
        NationRankEquipmentHelper.EQUIPMENTS.put(NationRank.DEPUTY.getBaseId(), createForAllNation(new Equipment(EquipmentPosition.HEAD, 1349921)));
        NationRankEquipmentHelper.EQUIPMENTS.put(NationRank.GENERAL.getBaseId(), createGeneralEquipements());
        NationRankEquipmentHelper.EQUIPMENTS.put(NationRank.MARSHAL.getBaseId(), createMarshalEquipements());
        NationRankEquipmentHelper.EQUIPMENTS.put(NationRank.METEOROLOGIST.getBaseId(), createForAllNation(new Equipment(EquipmentPosition.HEAD, 1349917)));
        NationRankEquipmentHelper.EQUIPMENTS.put(NationRank.ZOOLOGIST.getBaseId(), createForAllNation(new Equipment(EquipmentPosition.HEAD, 1349918)));
        NationRankEquipmentHelper.EQUIPMENTS.put(NationRank.TREASURER.getBaseId(), createForAllNation(new Equipment(EquipmentPosition.HEAD, 1349920)));
        NationRankEquipmentHelper.EQUIPMENTS.put(NationRank.CHALLENGER.getBaseId(), createForAllNation(new Equipment(EquipmentPosition.HEAD, 1349922)));
    }
    
    private static class Equipment
    {
        final EquipmentPosition position;
        final int gfxId;
        
        Equipment(final EquipmentPosition position, final int gfxId) {
            super();
            this.position = position;
            this.gfxId = gfxId;
        }
    }
    
    private static class EquipmentsByNation
    {
        final IntObjectLightWeightMap<Equipment[]> m_equipments;
        
        EquipmentsByNation() {
            super();
            this.m_equipments = new IntObjectLightWeightMap<Equipment[]>(4);
        }
        
        void add(final int nationId, final Equipment... equipements) {
            this.m_equipments.put(nationId, equipements);
        }
        
        Equipment[] getEquipements(final int nationId) {
            return this.m_equipments.get(nationId);
        }
    }
}
