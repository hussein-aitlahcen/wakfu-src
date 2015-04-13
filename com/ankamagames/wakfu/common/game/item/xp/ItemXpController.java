package com.ankamagames.wakfu.common.game.item.xp;

public class ItemXpController
{
    private final ItemXpHolder m_xpHolder;
    
    public ItemXpController(final ItemXpHolder holder) {
        super();
        this.m_xpHolder = holder;
    }
    
    public void setXp(final long xp) throws ItemXpException {
        if (!this.m_xpHolder.hasXp()) {
            throw new ItemXpException("pas d'xp sur l'item");
        }
        final ItemXpModel xpModel = (ItemXpModel)this.m_xpHolder.getXp();
        xpModel.setXp(xp);
    }
    
    public ItemXpHolder getXpHolder() {
        return this.m_xpHolder;
    }
    
    @Override
    public String toString() {
        return "ItemXpController{m_xpHolder=" + this.m_xpHolder + '}';
    }
}
