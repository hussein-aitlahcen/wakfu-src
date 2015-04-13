package com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement;

import org.jetbrains.annotations.*;

public interface InteractiveElementActionRunner
{
    boolean fireAction(InteractiveElementAction p0, InteractiveElementUser p1);
    
    void sendActionMessage(InteractiveElementAction p0);
    
    boolean onAction(InteractiveElementAction p0, InteractiveElementUser p1);
    
    @Nullable
    InteractiveElementAction getDefaultAction();
    
    InteractiveElementAction[] getUsableActions();
}
