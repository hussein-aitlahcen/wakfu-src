package com.ankamagames.wakfu.client.core.game.item.xp;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.common.game.item.xp.*;

public class ItemXpControllerUINotifier extends ItemXpController
{
    private static final Logger m_logger;
    
    public ItemXpControllerUINotifier(final ItemXpHolder holder) {
        super(holder);
    }
    
    @Override
    public void setXp(final long xp) throws ItemXpException {
        final ItemXpHolder xpHolder = this.getXpHolder();
        if (xpHolder == null) {
            ItemXpControllerUINotifier.m_logger.error((Object)"Propri\u00e9taire d'un item \u00e0 xp inconnu !");
            return;
        }
        final ItemXp itemXp = xpHolder.getXp();
        final long previousXp = itemXp.getXp();
        final short previousLevel = itemXp.getLevel();
        super.setXp(xp);
        final long nextLevelIn = itemXp.getXpTable().getXpByLevel(itemXp.getLevel() + 1) - xp;
        final String itemName = ((Item)this.getXpHolder()).getReferenceItem().getName();
        final int levelDifference = itemXp.getLevel() - previousLevel;
        final long xpDifference = xp - previousXp;
        final String message = WakfuTranslator.getInstance().getString("infoPop.xpGain", itemName, xpDifference, nextLevelIn, ChatConstants.CHAT_FIGHT_EFFECT_COLOR, levelDifference);
        final ChatMessage chatMessage = new ChatMessage(message);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemXpControllerUINotifier.class);
    }
}
