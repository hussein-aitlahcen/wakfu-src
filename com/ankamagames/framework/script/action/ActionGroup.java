package com.ankamagames.framework.script.action;

import java.util.*;

public interface ActionGroup extends ActionEventListener
{
    List<Action> getActions();
    
    void addAction(Action p0);
    
    Action getActionByUniqueId(int p0);
    
    Action getActionByTypeAndId(int p0, int p1);
    
    void addListener(ActionGroupEventListener p0);
    
    void removeListener(ActionGroupEventListener p0);
    
    void runNextAction();
    
    void runAction(Action p0, boolean p1);
    
    void onActionFinished(Action p0);
    
    void kill();
    
    void executeAllAction();
    
    Action getActionsBySpecialId(int p0);
    
    Action getActionByType(int p0);
}
