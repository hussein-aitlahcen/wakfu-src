package com.ankamagames.wakfu.common.game.aptitudeNewVersion;

public final class InactiveAptitudeBonusInventory extends AbstractAptitudeBonusInventory
{
    private final AbstractAptitudeBonusInventory m_referenceInventory;
    
    InactiveAptitudeBonusInventory(final AbstractAptitudeBonusInventory inventory) {
        super();
        this.m_referenceInventory = inventory;
        this.reset();
    }
    
    public void reset() {
        this.m_referenceInventory.copyTo(this);
    }
    
    public short getMinLevelForBonus(final int bonusId) {
        return this.m_referenceInventory.getLevel(bonusId);
    }
    
    @Override
    public String toString() {
        return "InactiveAptitudeBonusInventory{m_referenceInventory=" + this.m_referenceInventory + '}';
    }
}
