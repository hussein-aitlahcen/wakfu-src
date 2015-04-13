package com.ankamagames.wakfu.client.core.game.pet.newPet;

import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.wakfu.common.game.pet.definition.*;
import gnu.trove.*;
import com.ankamagames.framework.graphics.image.*;

public class PetActor extends Actor
{
    private final Pet m_pet;
    private final CharacterActor m_playerActor;
    
    public PetActor(final Pet pet) {
        super(GUIDGenerator.getGUID());
        this.m_pet = pet;
        this.m_playerActor = null;
    }
    
    public PetActor(final Pet pet, final CharacterActor playerActor) {
        super(GUIDGenerator.getGUID());
        this.m_pet = pet;
        this.m_playerActor = playerActor;
    }
    
    @Override
    public void onInventoryEvent(final InventoryEvent event) {
    }
    
    @Override
    public String getFormatedOverheadText() {
        return this.m_pet.getName();
    }
    
    @Override
    public boolean isVisible() {
        if (this.m_pet.getDefinition().hasMount()) {
            return false;
        }
        if (this.m_playerActor == null) {
            return true;
        }
        final BasicOccupation occ = this.m_playerActor.getCharacterInfo().getCurrentOccupation();
        if (occ != null && occ.getOccupationTypeId() == 14) {
            return false;
        }
        final boolean playerCheck = this.m_playerActor.isVisible() && this.m_playerActor.getCurrentFightId() <= 0;
        return super.isVisible() && playerCheck;
    }
    
    @Override
    protected PathFindResult checkDimensionalBagPath(final DimensionalBagView dimensionalBagView, final PathFindResult path) {
        return dimensionalBagView.checkPath(this.m_playerActor.getId(), path);
    }
    
    @Override
    public boolean isHighlightable() {
        return false;
    }
    
    @Override
    public String toString() {
        return "PetActor{m_pet=" + this.m_pet + ", m_playerActor=" + ((this.m_playerActor == null) ? null : this.m_playerActor.getCharacterInfo()) + '}';
    }
    
    public void setCustomColor(final PetDefinitionColor colorDef) {
        if (colorDef != null) {
            colorDef.foreach(new SetColorProc());
            this.forceReloadAnimation();
        }
    }
    
    private class SetColorProc implements TIntObjectProcedure<Color>
    {
        @Override
        public boolean execute(final int index, final Color color) {
            PetActor.this.setCustomColor(index, color.getFloatRGBA());
            return true;
        }
    }
}
