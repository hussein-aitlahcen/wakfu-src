package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class CharacterEffectManagerForReconnectionMessageHandler extends UsingFightMessageHandler<CharacterEffectManagerForReconnectionMessage, Fight>
{
    private static final Logger m_logger;
    
    @Override
    public boolean onMessage(final CharacterEffectManagerForReconnectionMessage msg) {
        final long characterId = msg.getCharacterId();
        final byte[] serializedEffects = msg.getSerializedEffects();
        final CharacterInfo fighterFromId = ((Fight)this.m_concernedFight).getFighterFromId(characterId);
        if (fighterFromId == null) {
            return false;
        }
        disableChrageListener(fighterFromId);
        try {
            fighterFromId.recoverEffects(serializedEffects);
            final List<RunningEffect> spellGain = fighterFromId.getRunningEffectManager().getEffectsWithActionId(RunningEffectConstants.SPELL_BOOST_LEVEL.getId());
            spellGain.addAll(fighterFromId.getRunningEffectManager().getEffectsWithActionId(RunningEffectConstants.ACTIVE_SUPPORT_SPELL_GAIN.getId()));
            spellGain.addAll(fighterFromId.getRunningEffectManager().getEffectsWithActionId(RunningEffectConstants.PASSIVE_SPELL_GAIN.getId()));
            spellGain.addAll(fighterFromId.getRunningEffectManager().getEffectsWithActionId(RunningEffectConstants.ELEMENT_SPELL_GAIN.getId()));
            spellGain.addAll(fighterFromId.getRunningEffectManager().getEffectsWithActionId(RunningEffectConstants.ALL_ELEMENTAL_SPELL_GAIN.getId()));
            for (final RunningEffect runningEffect : spellGain) {
                runningEffect.computeValue(null);
                runningEffect.execute(null, false);
            }
        }
        catch (Exception e) {
            CharacterEffectManagerForReconnectionMessageHandler.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        enableChrageListener(fighterFromId);
        return false;
    }
    
    private static void disableChrageListener(final CharacterInfo fighterFromId) {
        if (!(fighterFromId instanceof PlayerCharacter)) {
            return;
        }
        final SacrieurArmsApparitionListener chrageListener = ((PlayerCharacter)fighterFromId).getChrageListener();
        chrageListener.disable();
    }
    
    private static void enableChrageListener(final CharacterInfo fighterFromId) {
        if (!(fighterFromId instanceof PlayerCharacter)) {
            return;
        }
        final SacrieurArmsApparitionListener chrageListener = ((PlayerCharacter)fighterFromId).getChrageListener();
        chrageListener.enable();
    }
    
    static {
        m_logger = Logger.getLogger((Class)CharacterEffectManagerForReconnectionMessageHandler.class);
    }
}
