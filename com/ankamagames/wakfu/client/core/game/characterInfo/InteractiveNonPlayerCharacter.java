package com.ankamagames.wakfu.client.core.game.characterInfo;

import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;

public class InteractiveNonPlayerCharacter extends NonPlayerCharacter
{
    @Override
    public InteractiveElementAction getDefaultAction() {
        return null;
    }
    
    @Override
    public InteractiveElementAction[] getUsableActions() {
        return InteractiveElementAction.EMPTY_ACTIONS;
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        return false;
    }
    
    @Override
    public byte getActorTypeId() {
        return 4;
    }
    
    @Override
    public void release() {
    }
    
    @Override
    protected byte defaultCharacterType() {
        return 3;
    }
}
