package com.ankamagames.wakfu.common.game.item.gems;

import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;

public class ImmutableGems extends Gems
{
    public ImmutableGems(final AbstractReferenceItem holderDefinition) {
        super(holderDefinition);
    }
    
    @Override
    public boolean isEditable() {
        return false;
    }
    
    @Override
    public boolean canGem(final AbstractReferenceItem item, final byte index) {
        return false;
    }
    
    @Override
    public void equipGem(final AbstractReferenceItem item, final byte index) throws GemsException {
        throw new GemsException("Impossible d'equiper une gemme sur un ImmutableGems");
    }
    
    @Override
    public int removeGem(final byte index) throws GemsException {
        throw new GemsException("Impossible de retirer une gemme sur un ImmutableGems");
    }
}
