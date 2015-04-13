package com.ankamagames.wakfu.client.ui.protocol.message.pet;

import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import com.ankamagames.wakfu.common.game.item.*;

public class UIFeedPetMessage extends UIItemMessage
{
    private final PetDetailDialogView m_pet;
    private final Item m_item;
    
    public UIFeedPetMessage(final PetDetailDialogView pet, final Item item) {
        super();
        this.m_pet = pet;
        this.m_item = item;
    }
    
    @Override
    public Item getItem() {
        return this.m_item;
    }
    
    public PetDetailDialogView getPet() {
        return this.m_pet;
    }
    
    @Override
    public int getId() {
        return 19154;
    }
}
