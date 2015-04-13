package com.ankamagames.wakfu.common.game.characteristics.skill;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.characteristics.craft.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;

public final class SkillCharacteristicsForPlayer implements SkillCharacteristics
{
    private final TShortObjectHashMap<EcosystemSkillCharacteristic> m_ecosystemSkillCharacteristic;
    private final TLongObjectHashMap<CraftSkillCharacteristic> m_craftSkillCharacteristic;
    
    public SkillCharacteristicsForPlayer() {
        super();
        this.m_ecosystemSkillCharacteristic = new TShortObjectHashMap<EcosystemSkillCharacteristic>();
        this.m_craftSkillCharacteristic = new TLongObjectHashMap<CraftSkillCharacteristic>();
    }
    
    @Override
    public int getEcosystemCharacteristicEfficiency(final EcosystemActionType actionType, final ResourceType resourceType) {
        if (actionType == null || resourceType == null) {
            return 0;
        }
        final short index = MathHelper.getShortFromTwoBytes(actionType.getId(), resourceType.getId());
        final EcosystemSkillCharacteristic characteristic = this.m_ecosystemSkillCharacteristic.get(index);
        if (characteristic != null) {
            return characteristic.getEfficiencyPercentage();
        }
        return 0;
    }
    
    @Override
    public void modifyEcosystemCharacteristicEfficiency(final EcosystemActionType actionType, final ResourceType resourceType, final int efficiencyModification) {
        if (actionType == null || resourceType == null || efficiencyModification == 0) {
            return;
        }
        final EcosystemSkillCharacteristic characteristic = this.getOrCreateCharac(actionType, resourceType);
        characteristic.setEfficiencyPercentage(characteristic.getEfficiencyPercentage() + efficiencyModification);
    }
    
    @Override
    public int getCraftCharacteristicEfficiency(final CraftSkillType craftSkillType, final int craftId) {
        if (craftSkillType == null) {
            return 0;
        }
        final long index = MathHelper.getLongFromTwoInt(craftSkillType.getId(), craftId);
        final CraftSkillCharacteristic characteristic = this.m_craftSkillCharacteristic.get(index);
        if (characteristic != null) {
            return characteristic.getEfficiencyPercentage();
        }
        return 0;
    }
    
    @Override
    public void modifyCraftCharacteristicEfficiency(final CraftSkillType craftSkillType, final int craftId, final int value) {
        if (craftSkillType == null || value == 0) {
            return;
        }
        final CraftSkillCharacteristic characteristic = this.getOrCreateCharac(craftSkillType, craftId);
        characteristic.setEfficiencyPercentage(characteristic.getEfficiencyPercentage() + value);
    }
    
    private EcosystemSkillCharacteristic getOrCreateCharac(final EcosystemActionType actionType, final ResourceType resourceType) {
        final short index = MathHelper.getShortFromTwoBytes(actionType.getId(), resourceType.getId());
        EcosystemSkillCharacteristic characteristic = this.m_ecosystemSkillCharacteristic.get(index);
        if (characteristic == null) {
            characteristic = this.addEcosystemCharacteristic(actionType, resourceType);
        }
        return characteristic;
    }
    
    private CraftSkillCharacteristic getOrCreateCharac(final CraftSkillType craftSkillType, final int craftId) {
        final long index = MathHelper.getLongFromTwoInt(craftSkillType.getId(), craftId);
        CraftSkillCharacteristic characteristic = this.m_craftSkillCharacteristic.get(index);
        if (characteristic == null) {
            characteristic = this.addCraftCharacteristic(craftId, craftSkillType);
        }
        return characteristic;
    }
    
    @NotNull
    private EcosystemSkillCharacteristic addEcosystemCharacteristic(final EcosystemActionType actionType, final ResourceType resourceType) {
        final short index = MathHelper.getShortFromTwoBytes(actionType.getId(), resourceType.getId());
        EcosystemSkillCharacteristic charac = this.m_ecosystemSkillCharacteristic.get(index);
        if (charac != null) {
            return charac;
        }
        charac = new EcosystemSkillCharacteristic(actionType, resourceType);
        this.m_ecosystemSkillCharacteristic.put(index, charac);
        return charac;
    }
    
    @NotNull
    private CraftSkillCharacteristic addCraftCharacteristic(final int craftId, final CraftSkillType skillType) {
        final long index = MathHelper.getLongFromTwoInt(skillType.getId(), craftId);
        CraftSkillCharacteristic charac = this.m_craftSkillCharacteristic.get(index);
        if (charac != null) {
            return charac;
        }
        charac = new CraftSkillCharacteristic();
        this.m_craftSkillCharacteristic.put(index, charac);
        return charac;
    }
    
    @Override
    public void reset() {
        this.m_ecosystemSkillCharacteristic.clear();
        this.m_craftSkillCharacteristic.clear();
    }
}
