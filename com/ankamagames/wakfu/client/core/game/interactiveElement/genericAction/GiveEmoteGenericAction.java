package com.ankamagames.wakfu.client.core.game.interactiveElement.genericAction;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class GiveEmoteGenericAction extends AbstractClientGenericAction
{
    public GiveEmoteGenericAction(final int id) {
        super(id);
    }
    
    @Override
    protected void runAction() {
    }
    
    @Override
    public boolean isRunnable(final CharacterInfo concernedPlayer) {
        return true;
    }
    
    @Override
    public boolean isEnabled(final CharacterInfo concernedPlayer) {
        return true;
    }
}
