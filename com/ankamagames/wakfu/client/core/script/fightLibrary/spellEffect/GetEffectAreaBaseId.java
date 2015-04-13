package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetEffectAreaBaseId extends JavaFunctionEx
{
    public static final String NAME = "getEffectAreaBaseId";
    private static final String DESC = "Retourne le reference id de la zone dont l'uid est pass? en param?tre";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    GetEffectAreaBaseId(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getEffectAreaBaseId";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GetEffectAreaBaseId.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetEffectAreaBaseId.RESULTS;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long areaId = this.getParamLong(0);
        final FightInfo fight = StaticEffectAreaDisplayer.getInstance().getFight();
        if (fight == null) {
            return;
        }
        final BasicEffectAreaManager eam = fight.getEffectAreaManager();
        if (eam == null) {
            return;
        }
        final BasicEffectArea area = eam.getEffectAreaWithId(areaId);
        if (area == null || !(area instanceof GraphicalAreaProvider)) {
            return;
        }
        this.addReturnValue(area.getBaseId());
    }
    
    @Override
    public String getDescription() {
        return "Retourne le reference id de la zone dont l'uid est pass? en param?tre";
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("areaId", null, LuaScriptParameterType.LONG, false) };
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("areaBaseId", null, LuaScriptParameterType.LONG, false) };
    }
}
