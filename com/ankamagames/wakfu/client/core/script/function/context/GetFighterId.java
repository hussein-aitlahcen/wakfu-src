package com.ankamagames.wakfu.client.core.script.function.context;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import java.util.*;
import org.keplerproject.luajava.*;

public class GetFighterId extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public GetFighterId(final LuaState state) {
        super(state);
    }
    
    @Override
    public String getName() {
        return "getFighterId";
    }
    
    @Override
    public String getDescription() {
        return "Renvoi l'id du mobile positionn? en X, Y ou nil si aucun mobile trouv?";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("worldX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("worldY", null, LuaScriptParameterType.INTEGER, false) };
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false) };
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final int x = this.getParamInt(0);
        final int y = this.getParamInt(1);
        final int fightId = this.getScriptObject().getFightId();
        final FightInfo fight = FightManager.getInstance().getFightById(fightId);
        if (fight == null) {
            GetFighterId.m_logger.error((Object)("fight inconnu " + fightId));
            this.addReturnNilValue();
            return;
        }
        final Collection<CharacterInfo> mobiles = fight.getFighters();
        for (final CharacterInfo fighter : mobiles) {
            if (fighter.getWorldCellX() == x && fighter.getWorldCellY() == y) {
                this.addReturnValue(fighter.getId());
                return;
            }
        }
        this.addReturnNilValue();
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetFighterId.class);
    }
}
