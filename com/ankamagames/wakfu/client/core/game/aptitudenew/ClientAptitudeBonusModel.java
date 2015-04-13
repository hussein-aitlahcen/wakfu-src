package com.ankamagames.wakfu.client.core.game.aptitudenew;

import com.ankamagames.wakfu.common.game.aptitudeNewVersion.*;

public class ClientAptitudeBonusModel extends AptitudeBonusModel
{
    private final int m_gfxId;
    
    public ClientAptitudeBonusModel(final int id, final int effectId, final int max, final int gfxId) {
        super(id, effectId, max);
        this.m_gfxId = gfxId;
    }
    
    public ClientAptitudeBonusModel(final int id, final int effectId, final int gfxId) {
        super(id, effectId);
        this.m_gfxId = gfxId;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    @Override
    public String toString() {
        return "ClientAptitudeBonusModel{m_gfxId=" + this.m_gfxId + '}';
    }
}
