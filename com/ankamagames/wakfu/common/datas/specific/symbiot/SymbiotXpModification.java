package com.ankamagames.wakfu.common.datas.specific.symbiot;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;

public final class SymbiotXpModification
{
    private TByteObjectHashMap<XpModification> m_xpByCreatureId;
    
    public SymbiotXpModification() {
        super();
        this.m_xpByCreatureId = new TByteObjectHashMap<XpModification>();
    }
    
    public void setCreatureXp(final byte index, final long xp, final short levelDifference) {
        this.m_xpByCreatureId.put(index, new XpModification(xp, levelDifference));
    }
    
    public void setCreatureXp(final byte index, final long xp) {
        this.m_xpByCreatureId.put(index, new XpModification(xp, (short)0));
    }
    
    public boolean isEmpty() {
        return this.m_xpByCreatureId.isEmpty();
    }
    
    public TByteObjectHashMap<XpModification> getXpByCreatureId() {
        return this.m_xpByCreatureId;
    }
}
