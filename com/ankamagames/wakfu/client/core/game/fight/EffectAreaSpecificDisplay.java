package com.ankamagames.wakfu.client.core.game.fight;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import java.util.*;

public final class EffectAreaSpecificDisplay
{
    public static final EffectAreaSpecificDisplay INSTANCE;
    private final Set<BasicEffectArea> m_toRemoveAtEndOfTurn;
    
    private EffectAreaSpecificDisplay() {
        super();
        this.m_toRemoveAtEndOfTurn = new HashSet<BasicEffectArea>();
    }
    
    public void onStartTurn(final CharacterInfo character) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        if (character == player || !character.isControlledByLocalPlayer()) {
            return;
        }
        if (character.getBreed() == AvatarBreed.ENUTROF) {
            this.addEnutrofDeposit(character);
        }
    }
    
    private void addEnutrofDeposit(final CharacterInfo character) {
        final BasicEffectAreaManager effectAreaManager = character.getCurrentFight().getEffectAreaManager();
        final Collection<BasicEffectArea> activeEffectAreas = effectAreaManager.getActiveEffectAreas();
        for (final BasicEffectArea area : activeEffectAreas) {
            if (area.getOwner() == character && area.getType() == EffectAreaType.ENUTROF_DEPOSIT.getTypeId()) {
                StaticEffectAreaDisplayer.getInstance().addStaticEffectArea(area);
                this.m_toRemoveAtEndOfTurn.add(area);
            }
        }
    }
    
    public void onEndTurn(final CharacterInfo character) {
        for (final BasicEffectArea area : this.m_toRemoveAtEndOfTurn) {
            StaticEffectAreaDisplayer.getInstance().removeStaticEffectArea(area);
        }
    }
    
    static {
        INSTANCE = new EffectAreaSpecificDisplay();
    }
}
