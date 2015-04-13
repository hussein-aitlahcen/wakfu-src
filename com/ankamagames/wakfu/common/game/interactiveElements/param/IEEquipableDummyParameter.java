package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.chaos.*;

public final class IEEquipableDummyParameter extends IEParameter
{
    private final String m_animName;
    private final byte m_sex;
    
    public IEEquipableDummyParameter(final int paramId, final int visualId, final ChaosInteractiveCategory chaosCategory, final int chaosCollectorId, final String animName, final byte sex) {
        super(paramId, visualId, chaosCategory, chaosCollectorId);
        this.m_animName = animName;
        this.m_sex = sex;
    }
    
    public String getAnimName() {
        return this.m_animName;
    }
    
    public byte getSex() {
        return this.m_sex;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IEEquipableDummyParameter");
        sb.append("{m_animName='").append(this.m_animName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
