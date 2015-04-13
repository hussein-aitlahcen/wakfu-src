package com.ankamagames.wakfu.client.ui.protocol.message.pet;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;

public class UIPetMessage extends UIMessage
{
    private final PetDetailDialogView m_pet;
    
    public UIPetMessage(final PetDetailDialogView pet, final int id) {
        super();
        this.m_pet = pet;
        this.setId(id);
    }
    
    public PetDetailDialogView getPet() {
        return this.m_pet;
    }
}
