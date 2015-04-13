package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class SetEffectAreaPlayDeathAnim extends JavaFunctionEx
{
    private static final String NAME = "setEffectAreaPlayDeathAnim";
    private static final String DESC = "Active ou d?sactive l'anim de mort sur la zone dont l'uid est pass? en param?tre";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    SetEffectAreaPlayDeathAnim(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setEffectAreaPlayDeathAnim";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetEffectAreaPlayDeathAnim.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long areaId = this.getParamLong(0);
        final boolean playAnim = this.getParamBool(1);
        final FightInfo fight = StaticEffectAreaDisplayer.getInstance().getFight();
        if (fight == null) {
            return;
        }
        final BasicEffectAreaManager eam = fight.getEffectAreaManager();
        if (eam == null) {
            return;
        }
        final BasicEffectArea area = eam.getEffectAreaWithId(areaId);
        if (!(area instanceof GraphicalAreaProvider)) {
            return;
        }
        ((GraphicalAreaProvider)area).getGraphicalArea().setShouldPlayDeathAnimation(playAnim);
    }
    
    @Override
    public String getDescription() {
        return "Active ou d?sactive l'anim de mort sur la zone dont l'uid est pass? en param?tre";
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("areaId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("playAnim", null, LuaScriptParameterType.BOOLEAN, false) };
    }
}
