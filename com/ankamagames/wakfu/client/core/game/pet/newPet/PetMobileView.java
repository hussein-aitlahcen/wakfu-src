package com.ankamagames.wakfu.client.core.game.pet.newPet;

import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.pet.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.gameAction.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.pet.exception.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class PetMobileView implements VisibleChangedListener, PetHolder
{
    private final PlayerCharacter m_player;
    private final Pet m_pet;
    private PetActor m_petMobile;
    private PetFollowProcedure m_petFollowProcedure;
    private final PetModelListener m_petListener;
    
    public PetMobileView(final PlayerCharacter player, final Pet pet) {
        super();
        this.m_petListener = new PetListener();
        this.m_player = player;
        this.m_pet = pet;
        this.initialize();
    }
    
    private void initialize() {
        final CharacterActor playerActor = this.m_player.getActor();
        this.m_petMobile = new PetActor(this.m_pet, playerActor);
        final int equippedRefItemId = this.m_pet.getEquippedRefItemId();
        this.m_petMobile.setGfx(PetHelper.getPetGfx(this.m_player, this.m_pet));
        this.m_petMobile.setDirection(playerActor.getDirection());
        this.m_petMobile.setAnimation("AnimStatique");
        this.m_petMobile.setMovementSelector(SimpleMovementSelector.getInstance());
        this.m_petMobile.setDeltaZ(LayerOrder.MOBILE.getDeltaZ());
        this.m_petMobile.setAvailableDirections((byte)8);
        this.m_petMobile.setVisible(true);
        MobileManager.getInstance().addMobile(this.m_petMobile);
        this.m_petMobile.setCustomColor(this.m_pet.getColorDefinition());
        final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(equippedRefItemId);
        if (refItem != null) {
            this.m_petMobile.applyPartsEquipment(refItem.getGfxId());
        }
        this.m_petMobile.forceReloadAnimation();
        playerActor.addStartPathListener(this.m_petFollowProcedure = new PetFollowProcedure(this.m_petMobile));
        playerActor.addPositionListener(this.m_petFollowProcedure);
        final Point3 petCell = PetFollowProcedure.getNextPetCell(playerActor.getCurrentWorldX(), playerActor.getCurrentWorldY(), playerActor.getCurrentAltitude());
        this.m_petMobile.setWorldPosition(petCell.getX(), petCell.getY(), petCell.getZ());
        this.m_pet.addListener(this.m_petListener);
    }
    
    public void detach() {
        final Mobile parentMobile = this.m_petMobile.getLinkedParentMobile();
        if (parentMobile != null) {
            parentMobile.unlinkChildMobile(this.m_petMobile);
        }
        MobileManager.getInstance().removeMobile(this.m_petMobile.getId());
        this.m_petMobile.dispose();
        this.m_petMobile = null;
        if (this.m_player.hasActor()) {
            final CharacterActor actor = this.m_player.getActor();
            actor.removeStartListener(this.m_petFollowProcedure);
            actor.removePositionListener(this.m_petFollowProcedure);
        }
        this.m_petFollowProcedure = null;
        this.m_pet.removeListener(this.m_petListener);
    }
    
    @Override
    public void onVisibleChanged(final boolean visible, final VisibleChangedCause cause) {
        if (this.m_petMobile == null || cause != VisibleChangedCause.VISIBLE) {
            return;
        }
        final boolean healthCheck = this.m_pet.getHealth() > 0;
        final boolean sleepCheck = this.m_pet.getSleepRefItemId() <= 0 && this.m_pet.getSleepDate().isNull();
        this.m_petMobile.setVisible(visible && healthCheck && sleepCheck);
    }
    
    @Override
    public boolean hasPet() {
        return true;
    }
    
    @Override
    public Pet getPet() throws PetException {
        return this.m_pet;
    }
    
    @Override
    public long getUniqueId() {
        return this.m_petMobile.getId();
    }
    
    public Actor getMobile() {
        return this.m_petMobile;
    }
    
    @Override
    public String toString() {
        return "ServerPetHandlerListener{m_player=" + this.m_player + '}';
    }
    
    private class PetListener implements PetModelListener
    {
        @Override
        public void nameChanged(final String name) {
        }
        
        @Override
        public void colorItemChanged(final int colorItemRefId) {
            PetMobileView.this.m_petMobile.setCustomColor(PetMobileView.this.m_pet.getColorDefinition());
        }
        
        @Override
        public void equippedItemChanged(final int equippedItem) {
            PetHelper.applyEquipment(PetMobileView.this.m_player, PetMobileView.this.m_pet, PetMobileView.this.m_petMobile, equippedItem);
        }
        
        @Override
        public void healthChanged(final int health) {
            PetMobileView.this.onVisibleChanged(health > 0, VisibleChangedCause.VISIBLE);
        }
        
        @Override
        public void xpChanged(final int xp) {
        }
        
        @Override
        public void lastMealDateChanged(final GameDateConst mealDate) {
        }
        
        @Override
        public void lastHungryDateChanged(final GameDateConst hungryDate) {
        }
        
        @Override
        public void sleepDateChanged(final GameDateConst sleepDate) {
            PetMobileView.this.onVisibleChanged(sleepDate.isNull(), VisibleChangedCause.VISIBLE);
        }
        
        @Override
        public void sleepItemChanged(final int sleepRefItemId) {
            PetMobileView.this.onVisibleChanged(sleepRefItemId <= 0, VisibleChangedCause.VISIBLE);
        }
    }
}
