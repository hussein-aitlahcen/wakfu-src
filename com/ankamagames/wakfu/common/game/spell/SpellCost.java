package com.ankamagames.wakfu.common.game.spell;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.fighter.*;

public final class SpellCost
{
    private final TByteByteHashMap m_costs;
    private boolean m_forMovement;
    private boolean m_forTackle;
    
    public SpellCost() {
        super();
        this.m_costs = new TByteByteHashMap();
        this.m_forMovement = false;
        this.m_forTackle = false;
    }
    
    public SpellCost(final byte ap, final byte mp, final byte wp) {
        super();
        this.m_costs = new TByteByteHashMap();
        this.m_forMovement = false;
        this.m_forTackle = false;
        this.addCost(FighterCharacteristicType.AP.getId(), ap);
        this.addCost(FighterCharacteristicType.MP.getId(), mp);
        this.addCost(FighterCharacteristicType.WP.getId(), wp);
    }
    
    public void addCost(final byte characId, final byte value) {
        if (value <= 0) {
            return;
        }
        this.m_costs.put(characId, value);
    }
    
    public byte getCharacCost(final FighterCharacteristicType charac) {
        if (charac == null) {
            return 0;
        }
        if (!this.m_costs.contains(charac.getId())) {
            return 0;
        }
        return this.m_costs.get(charac.getId());
    }
    
    public boolean isForMovement() {
        return this.m_forMovement;
    }
    
    public void setForMovement(final boolean forMovement) {
        this.m_forMovement = forMovement;
    }
    
    public boolean isForTackle() {
        return this.m_forTackle;
    }
    
    public void setForTackle(final boolean forTackle) {
        this.m_forTackle = forTackle;
    }
}
