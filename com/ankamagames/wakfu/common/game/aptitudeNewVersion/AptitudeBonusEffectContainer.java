package com.ankamagames.wakfu.common.game.aptitudeNewVersion;

import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;

public final class AptitudeBonusEffectContainer implements WakfuEffectContainer
{
    private final AptitudeBonusModel m_bonusModel;
    private final short m_level;
    
    public AptitudeBonusEffectContainer(@NotNull final AptitudeBonusModel bonusModel, final short level) {
        super();
        this.m_bonusModel = bonusModel;
        this.m_level = level;
    }
    
    public AptitudeBonusModel getBonusModel() {
        return this.m_bonusModel;
    }
    
    @Override
    public long getEffectContainerId() {
        return WakfuEffectContainerUtils.getDefaultUid(this.m_bonusModel.getEffectId(), this.m_level);
    }
    
    @Override
    public short getLevel() {
        return this.m_level;
    }
    
    @Override
    public int getContainerType() {
        return 36;
    }
    
    @Override
    public Iterator<WakfuEffect> iterator() {
        final WakfuEffect effect = AptitudeBonusEffectManager.INSTANCE.getEffect(this.m_bonusModel.getEffectId());
        if (effect == null) {
            return Collections.emptyList().iterator();
        }
        return Collections.singleton(effect).iterator();
    }
    
    @Override
    public short getAggroWeight() {
        return 0;
    }
    
    @Override
    public short getAllyEfficacity() {
        return 0;
    }
    
    @Override
    public short getFoeEfficacity() {
        return 0;
    }
    
    @Override
    public String toString() {
        return "AptitudeBonusEffectContainer{m_bonusModel=" + this.m_bonusModel + ", m_level=" + this.m_level + '}';
    }
}
