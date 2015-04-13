package com.ankamagames.wakfu.client.core.companion;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.account.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class CompanionUpdateXpMessageRunner implements MessageRunner<CompanionUpdateXpMessage>
{
    private static final Logger m_logger;
    
    @Override
    public boolean run(final CompanionUpdateXpMessage msg) {
        final LocalAccountInformations localPlayer = WakfuGameEntity.getInstance().getLocalAccount();
        final CompanionModel companion = CompanionManager.INSTANCE.getCompanion(localPlayer.getAccountId(), msg.getCompanionId());
        if (companion == null) {
            CompanionUpdateXpMessageRunner.m_logger.error((Object)("Impossible de modifier le nom du compagnon " + msg.getCompanionId()));
            return false;
        }
        final CompanionController companionController = new CompanionController(companion);
        try {
            final long newXp = msg.getXp();
            final long currentXp = companion.getXp();
            final short newLevel = CharacterXpTable.getInstance().getLevelByXp(newXp);
            final long nextIn = CharacterXpTable.getInstance().getXpByLevel(newLevel + 1) - newXp;
            final short levelDifference = (short)(newLevel - CharacterXpTable.getInstance().getLevelByXp(currentXp));
            final String name = (companion.getName().length() > 0) ? companion.getName() : MonsterBreedManager.getInstance().getBreedFromId(companion.getBreedId()).getName();
            final String message = WakfuTranslator.getInstance().getString("infoPop.xpGain", name, newXp - currentXp, nextIn, ChatConstants.CHAT_FIGHT_EFFECT_COLOR, levelDifference);
            final ChatMessage chatMessage = new ChatMessage(message.toString());
            chatMessage.setPipeDestination(4);
            ChatManager.getInstance().pushMessage(chatMessage);
            companionController.setXp(newXp);
        }
        catch (CompanionException e) {
            CompanionUpdateXpMessageRunner.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 5559;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CompanionUpdateXpMessageRunner.class);
    }
}
