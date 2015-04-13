package com.ankamagames.wakfu.common.game.craftNew;

public class CraftContractRecipeComponent extends AbstractCraftComponent
{
    private short m_requesterCraftUserQuantity;
    private short m_requestedCraftUserQuantity;
    
    public CraftContractRecipeComponent(final int referenceItemId) {
        super(referenceItemId);
    }
    
    public CraftContractRecipeComponent(final int referenceItemId, final byte position) {
        super(referenceItemId, position);
    }
    
    public void setRequesterCraftUserQuantity(final short requesterCraftUserQuantity) {
        this.m_requesterCraftUserQuantity = requesterCraftUserQuantity;
    }
    
    public void setRequestedCraftUserQuantity(final short requestedCraftUserQuantity) {
        this.m_requestedCraftUserQuantity = requestedCraftUserQuantity;
    }
    
    @Override
    public short getQuantity() {
        return (short)(this.getRequestedCraftUserQuantity() + this.getRequesterCraftUserQuantity());
    }
    
    @Override
    public short getRequesterCraftUserQuantity() {
        return this.m_requesterCraftUserQuantity;
    }
    
    @Override
    public short getRequestedCraftUserQuantity() {
        return this.m_requestedCraftUserQuantity;
    }
}
