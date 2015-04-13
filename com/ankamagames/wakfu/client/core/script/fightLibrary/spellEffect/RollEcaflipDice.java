package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.breedSpecific.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class RollEcaflipDice extends SpellEffectActionFunction
{
    private static final String NAME = "rollEcaflipDice";
    private static final String DESC = "Joue les visuels associ?s ? un jet de d? Ecaflip";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    RollEcaflipDice(final LuaState luaState, final SpellEffectActionInterface spellEffectAction) {
        super(luaState, spellEffectAction);
    }
    
    @Override
    public String getName() {
        return "rollEcaflipDice";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return RollEcaflipDice.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final int diceRoll = this.getParamInt(0);
        final long targetId = this.m_spellEffectAction.getTargetId();
        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(targetId);
        final EcaflipFightListener listener = EcaflipFightListenerManager.INSTANCE.getListener(character.getId());
        listener.rollDice((byte)diceRoll);
    }
    
    @Override
    public String getDescription() {
        return "Joue les visuels associ?s ? un jet de d? Ecaflip";
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("diceValue", null, LuaScriptParameterType.NUMBER, false) };
    }
}
