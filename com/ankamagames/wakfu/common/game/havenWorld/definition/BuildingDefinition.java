package com.ankamagames.wakfu.common.game.havenWorld.definition;

import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.wakfu.common.game.effect.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class BuildingDefinition extends AbstractBuildingDefinition
{
    private final ArrayList<BuildingIEDefinition> m_interactiveElements;
    private final ArrayList<WakfuEffect> m_effects;
    private final ArrayList<WakfuEffect> m_globalEffects;
    private final TIntArrayList m_worldEffectIds;
    private IntIntLightWeightMap m_skinMap;
    
    public BuildingDefinition(final short id, final short catalogEntryId, final int kamasCost, final byte neededWorkers, final int grantedWorkers, final int editorGroupId, final int resourcesCost, final boolean canBeDestroyed) {
        super(id, catalogEntryId, kamasCost, neededWorkers, grantedWorkers, editorGroupId, resourcesCost, canBeDestroyed);
        this.m_interactiveElements = new ArrayList<BuildingIEDefinition>();
        this.m_effects = new ArrayList<WakfuEffect>();
        this.m_globalEffects = new ArrayList<WakfuEffect>();
        this.m_worldEffectIds = new TIntArrayList();
    }
    
    public void addIE(final BuildingIEDefinition ieDefinition) {
        this.m_interactiveElements.add(ieDefinition);
    }
    
    public void addEffect(final WakfuEffect effect) {
        if (effect.hasProperty(RunningEffectPropertyType.HAVEN_WORLD_GUILD_MEMBER_WORLD_BUFF)) {
            this.m_globalEffects.add(effect);
        }
        else {
            this.m_effects.add(effect);
        }
    }
    
    @Override
    public boolean isDecoOnly() {
        return false;
    }
    
    @Override
    public boolean hasEffect() {
        return !this.m_effects.isEmpty();
    }
    
    @Override
    public boolean forEachElement(final TObjectProcedure<BuildingIEDefinition> procedure) {
        for (int i = 0; i < this.m_interactiveElements.size(); ++i) {
            if (!procedure.execute(this.m_interactiveElements.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public boolean forEachEffect(final TObjectProcedure<WakfuEffect> procedure) {
        for (int i = 0; i < this.m_effects.size(); ++i) {
            if (!procedure.execute(this.m_effects.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public boolean forEachGlobalEffect(final TObjectProcedure<WakfuEffect> procedure) {
        for (int i = 0; i < this.m_globalEffects.size(); ++i) {
            if (!procedure.execute(this.m_globalEffects.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public boolean forEachWorldEffect(final TIntProcedure procedure) {
        for (int i = 0; i < this.m_worldEffectIds.size(); ++i) {
            if (!procedure.execute(this.m_worldEffectIds.getQuick(i))) {
                return false;
            }
        }
        return true;
    }
    
    public void addBuff(final int buffId) {
        this.m_worldEffectIds.add(buffId);
    }
    
    public void setSkinMap(final IntIntLightWeightMap skinMap) {
        if (skinMap.size() == 0) {
            this.m_skinMap = null;
        }
        else {
            this.m_skinMap = skinMap;
        }
    }
    
    @Override
    public boolean acceptItem(final int itemId) {
        return this.m_skinMap != null && this.m_skinMap.get(itemId) != 0;
    }
    
    @Override
    public int[] getEquipableItems() {
        return (this.m_skinMap == null) ? PrimitiveArrays.EMPTY_INT_ARRAY : this.m_skinMap.keys();
    }
    
    @Override
    public int getEditorGroupId(final int itemId) {
        final int result = (this.m_skinMap == null) ? 0 : this.m_skinMap.get(itemId);
        if (result == 0) {
            return this.getEditorGroupId();
        }
        return result;
    }
    
    public boolean hasGlobalEffects() {
        return !this.m_globalEffects.isEmpty();
    }
}
