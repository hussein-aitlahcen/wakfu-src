package com.ankamagames.wakfu.client.core.script.fightLibrary.effectArea;

import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetAreaPosition extends JavaFunctionEx
{
    private static final LuaScriptParameterDescriptor[] RESULTS;
    private static final String NAME = "getAreaPosition";
    private static final String DESC = "Permet de r?cup?rer les coordonn?es de la zone associ?e de l'action";
    private final EffectAreaTriggeredAction m_effectAreaAction;
    
    GetAreaPosition(final LuaState luaState, final EffectAreaTriggeredAction effectAreaAction) {
        super(luaState);
        this.m_effectAreaAction = effectAreaAction;
    }
    
    @Override
    public String getName() {
        return "getAreaPosition";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetAreaPosition.RESULTS;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final BasicEffectAreaManager manager = this.m_effectAreaAction.getEffectAreaManager();
        if (manager == null) {
            return;
        }
        final BasicEffectArea area = manager.getActiveEffectAreaWithId(this.m_effectAreaAction.getInstigatorId());
        if (area == null) {
            return;
        }
        this.addReturnValue(area.getWorldCellX());
        this.addReturnValue(area.getWorldCellY());
        this.addReturnValue(area.getWorldCellAltitude());
    }
    
    @Override
    public String getDescription() {
        return "Permet de r?cup?rer les coordonn?es de la zone associ?e de l'action";
    }
    
    static {
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("posX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posY", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posZ", null, LuaScriptParameterType.INTEGER, false) };
    }
}
