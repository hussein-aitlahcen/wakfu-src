package com.ankamagames.wakfu.client.core.game.pet;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.pet.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class PetEquipmentInventoryListener implements InventoryObserver
{
    private final LocalPlayerCharacter m_localPlayer;
    private final Collection<PetListener> m_petListener;
    
    public PetEquipmentInventoryListener(final LocalPlayerCharacter localPlayer) {
        super();
        this.m_petListener = new ArrayList<PetListener>();
        this.m_localPlayer = localPlayer;
    }
    
    @Override
    public void onInventoryEvent(final InventoryEvent event) {
        final InventoryEvent.Action action = event.getAction();
        if (action == InventoryEvent.Action.ITEM_ADDED || action == InventoryEvent.Action.ITEM_ADDED_AT) {
            final PetHolder item = (PetHolder)((InventoryItemModifiedEvent)event).getConcernedItem();
            if (!item.hasPet()) {
                return;
            }
            final short pos = this.m_localPlayer.getEquipmentInventory().getPosition(item.getUniqueId());
            final EquipmentPosition position = EquipmentPosition.fromId(MathHelper.ensureByte(pos));
            final Pet pet = item.getPet();
            final PetListener petListener = new PetListener(this.m_localPlayer, position);
            this.m_petListener.add(petListener);
            pet.addListener(petListener);
        }
        else if (action == InventoryEvent.Action.ITEM_REMOVED || action == InventoryEvent.Action.ITEM_REMOVED_AT) {
            final PetHolder item = (PetHolder)((InventoryItemModifiedEvent)event).getConcernedItem();
            if (!item.hasPet()) {
                return;
            }
            final Pet pet2 = item.getPet();
            final Iterator<PetListener> it = this.m_petListener.iterator();
            while (it.hasNext()) {
                if (pet2.removeListener(it.next())) {
                    it.remove();
                }
            }
        }
    }
    
    private static class PetListener implements PetModelListener
    {
        private final LocalPlayerCharacter m_player;
        private final EquipmentPosition m_position;
        private int m_level;
        
        PetListener(final LocalPlayerCharacter player, final EquipmentPosition position) {
            super();
            this.m_level = -1;
            this.m_player = player;
            this.m_position = position;
        }
        
        @Override
        public void nameChanged(final String name) {
        }
        
        @Override
        public void colorItemChanged(final int colorItemRefId) {
        }
        
        @Override
        public void equippedItemChanged(final int equippedItem) {
        }
        
        @Override
        public void healthChanged(final int health) {
        }
        
        @Override
        public void xpChanged(final int xp) {
            final Item petHolder = ((ArrayInventoryWithoutCheck<Item, R>)this.m_player.getEquipmentInventory()).getFromPosition(this.m_position.m_id);
            if (petHolder == null || !petHolder.hasPet()) {
                return;
            }
            final Pet pet = petHolder.getPet();
            final short newLevel = pet.getLevel();
            if (this.m_level == newLevel) {
                return;
            }
            this.m_player.reloadItemEffects();
            this.m_level = newLevel;
        }
        
        @Override
        public void lastMealDateChanged(final GameDateConst mealDate) {
        }
        
        @Override
        public void lastHungryDateChanged(final GameDateConst hungryDate) {
        }
        
        @Override
        public void sleepDateChanged(final GameDateConst sleepDate) {
        }
        
        @Override
        public void sleepItemChanged(final int sleepRefItemId) {
        }
    }
}
