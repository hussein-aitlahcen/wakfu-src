package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.item.ui.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import org.keplerproject.luajava.*;

final class DisplayFlyingItem extends SpellEffectActionFunction
{
    private static final String NAME = "displayFlyingItem";
    private static final String DESC = "Affiche le gfx d'un item au dessus de la cible de l'effet (le refId de l'item correspond ? la valeur de l'effet)";
    
    DisplayFlyingItem(final LuaState luaState, final SpellEffectActionInterface spellEffectAction) {
        super(luaState, spellEffectAction);
    }
    
    @Override
    public String getName() {
        return "displayFlyingItem";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final int itemRefId = this.m_spellEffectAction.getEffectValue();
        final long targetId = this.m_spellEffectAction.getTargetId();
        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(targetId);
        final LocalPlayerCharacter lp = WakfuGameEntity.getInstance().getLocalPlayer();
        Point3 position;
        if (character != null) {
            position = character.getPosition();
        }
        else {
            position = this.m_spellEffectAction.getPosition();
        }
        final Fight lpFight = lp.getCurrentOrObservedFight();
        if (lpFight != null && this.m_spellEffectAction.getFightId() != lpFight.getId()) {
            return;
        }
        if (position == null) {
            return;
        }
        ItemFeedbackHelper.displayFlyingItemGain(itemRefId, new Point3Positionable(position), character.getSex());
    }
    
    @Override
    public String getDescription() {
        return "Affiche le gfx d'un item au dessus de la cible de l'effet (le refId de l'item correspond ? la valeur de l'effet)";
    }
}
