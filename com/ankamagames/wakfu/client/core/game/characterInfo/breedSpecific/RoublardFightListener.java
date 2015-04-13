package com.ankamagames.wakfu.client.core.game.characterInfo.breedSpecific;

import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.common.game.fighter.specialEvent.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public class RoublardFightListener implements BreedSpecific
{
    @Override
    public void clear() {
    }
    
    @Override
    public void onSpecialFighterEvent(final SpecialEvent event) {
        if (event.getId() == 1000) {
            final WakfuRunningEffect effect = ((EffectAppliedEvent)event).getEffect();
            if (effect.getEffectId() == 63708) {
                updateShortcutBars();
            }
        }
        else if (event.getId() == 1001) {
            final WakfuRunningEffect effect = ((EffectUnappliedEvent)event).getState();
            if (effect.getEffectId() == 63708) {
                updateShortcutBars();
            }
        }
    }
    
    private static void updateShortcutBars() {
        WakfuGameEntity.getInstance().getLocalPlayer().getShortcutBarManager().updateShortcutBars();
    }
    
    @Override
    public void onBarrelCarried(final BasicEffectArea area) {
    }
    
    @Override
    public void onBarrelUncarried(final BasicEffectArea area) {
    }
}
