package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;

public class PlayerXpModificationAction extends TimedAction
{
    private final Iterable<PlayerXpModification> m_playerXpModifications;
    
    public PlayerXpModificationAction(final int uniqueId, final Iterable<PlayerXpModification> playerXpModifications) {
        super(uniqueId, FightActionType.XP_GAIN.getId(), 0);
        this.m_playerXpModifications = playerXpModifications;
    }
    
    public long onRun() {
        boolean localLevelUp = false;
        for (final PlayerXpModification playerXpModification : this.m_playerXpModifications) {
            final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(playerXpModification.getPlayerId());
            if (!(character instanceof PlayerCharacter)) {
                continue;
            }
            final PlayerCharacter player = (PlayerCharacter)character;
            if (!playerXpModification.getPlayerXpModification().doesLevelUp() || player.isDead() || player.isOnFight()) {
                continue;
            }
            this.playLevelUpAnimation(player);
            if (player != WakfuGameEntity.getInstance().getLocalPlayer()) {
                continue;
            }
            localLevelUp = true;
        }
        int animationDuration = 0;
        if (localLevelUp) {
            try {
                animationDuration = WakfuGameEntity.getInstance().getLocalPlayer().getActor().getAnimationDuration("AnimLevelUp");
                if (animationDuration == Integer.MAX_VALUE) {
                    animationDuration = 1500;
                }
            }
            catch (Exception e) {
                animationDuration = 0;
                PlayerXpModificationAction.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
        return animationDuration;
    }
    
    private boolean applyXpModification(final PlayerCharacter player, final XpModification xpModification) {
        final long xpGain = xpModification.getXpGain();
        if (xpGain <= 0L) {
            return false;
        }
        final short levelDifference = xpModification.getLevelDifference();
        player.addCharacterXp(xpGain, levelDifference);
        if (levelDifference <= 0) {
            return false;
        }
        if (player == WakfuGameEntity.getInstance().getLocalPlayer()) {
            try {
                ClientGameEventManager.INSTANCE.fireEvent(new ClientEventLevelGain(player.getLevel()));
            }
            catch (Exception e) {
                PlayerXpModificationAction.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
        return true;
    }
    
    private void playLevelUpAnimation(final PlayerCharacter player) {
        player.applyLevelUpParticleSystem();
        player.setDirection(Direction8.SOUTH);
        player.getActor().setAnimation("AnimLevelUp");
        player.getActor().setStaticAnimationKey("AnimStatique");
    }
    
    @Override
    protected void onActionFinished() {
        for (final PlayerXpModification playerXpModification : this.m_playerXpModifications) {
            final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(playerXpModification.getPlayerId());
            if (!(character instanceof PlayerCharacter)) {
                continue;
            }
            final PlayerCharacter player = (PlayerCharacter)character;
            this.applyXpModification(player, playerXpModification.getPlayerXpModification());
        }
    }
    
    public static PlayerXpModificationAction buildFromMessage(final PlayerXpModificationMessage xpModMessage) {
        return new PlayerXpModificationAction(TimedAction.getNextUid(), xpModMessage.getXpModifications());
    }
}
