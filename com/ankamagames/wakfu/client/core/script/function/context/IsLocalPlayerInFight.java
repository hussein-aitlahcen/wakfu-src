package com.ankamagames.wakfu.client.core.script.function.context;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import org.keplerproject.luajava.*;

public final class IsLocalPlayerInFight extends JavaFunctionEx
{
    public IsLocalPlayerInFight(final LuaState state) {
        super(state);
    }
    
    @Override
    public String getName() {
        return "isInFight";
    }
    
    @Override
    public String getDescription() {
        return "Test si le localPlayer est en combat.";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("currentFight", "True si on veut tester que le joueur local appartient au combat courant", LuaScriptParameterType.BOOLEAN, true) };
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("inFight", "True si le joueur local est en combat, false sinon", LuaScriptParameterType.BOOLEAN, false) };
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final Fight currentFight = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOrObservedFight();
        if (currentFight == null) {
            this.addReturnValue(false);
            return;
        }
        if (paramCount == 1 && this.getParamBool(0)) {
            this.addReturnValue(this.getScriptObject().getFightId() == currentFight.getId());
            return;
        }
        this.addReturnValue(true);
    }
}
