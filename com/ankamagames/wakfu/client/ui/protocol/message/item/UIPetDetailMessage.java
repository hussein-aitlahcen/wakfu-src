package com.ankamagames.wakfu.client.ui.protocol.message.item;

import com.ankamagames.wakfu.client.core.game.pet.newPet.*;

public class UIPetDetailMessage<T extends AbstractPetDetailDialogView> extends AbstractUIDetailMessage<T>
{
    public UIPetDetailMessage(final T view) {
        super();
        this.setItem(view);
    }
}
