package com.ankamagames.wakfu.client.core.game.craft;

import com.ankamagames.wakfu.common.game.craft.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.craft.util.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class ClientCraftListener implements CraftListener
{
    private static final Logger m_logger;
    public static final ClientCraftListener INSTANCE;
    
    @Override
    public void onCraftLearned(final ReferenceCraft refCraft) {
        final ClientBagContainer bags = WakfuGameEntity.getInstance().getLocalPlayer().getBags();
        final Collection<Item> items = bags.getAllWithReferenceId(refCraft.getLearningBookId());
        for (final Item item : items) {
            if (bags.removeItemFromBags(item) != null) {
                item.release();
            }
        }
        final String message = WakfuTranslator.getInstance().getString("craft.jobLearnt", WakfuTranslator.getInstance().getString(43, refCraft.getId(), new Object[0]));
        final ChatMessage chatMessage = new ChatMessage(message);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
        CraftDisplayer.INSTANCE.addCraft(refCraft.getId());
        PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), "craft");
        ClientGameEventManager.INSTANCE.fireEvent(new ClientEventCraftLearned(refCraft.getId()));
    }
    
    @Override
    public void onCraftXpGained(final int craftId, final long xpAdded) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final long xp = localPlayer.getCraftHandler().getXp(craftId);
        final int levelGained = CraftXPUtil.getCurrentLevel(xp) - CraftXPUtil.getCurrentLevel(xp - xpAdded);
        final String craftName = WakfuTranslator.getInstance().getString(43, craftId, new Object[0]);
        final String message = WakfuTranslator.getInstance().getString("infoPop.xpGain", craftName, xpAdded, localPlayer.getCraftHandler().getNextInXp(craftId), ChatConstants.CHAT_GAME_ERROR_COLOR, levelGained);
        final ChatMessage chatMessage = new ChatMessage(message);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
        if (levelGained == 0) {
            return;
        }
        final String title = WakfuTranslator.getInstance().getString("notification.skillLevelUpTitle", craftName);
        final String text = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString("notification.skillLevelUpText", craftName, localPlayer.getCraftHandler().getLevel(craftId)), NotificationMessageType.CRAFT, craftId + "");
        final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.CRAFT);
        Worker.getInstance().pushMessage(uiNotificationMessage);
        localPlayer.displayCraftLevelGainedParticle();
        CraftDisplayer.INSTANCE.onCraftXpGained(craftId, xpAdded);
    }
    
    @Override
    public void onRecipeLearned(final int craftId, final int recipeId) {
        final String message = WakfuTranslator.getInstance().getString("craft.recipeDiscovered", WakfuTranslator.getInstance().getString(46, recipeId, new Object[0]));
        final ChatMessage chatMessage = new ChatMessage(message);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
        CraftDisplayer.INSTANCE.onRecipeLearnt(craftId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ClientCraftListener.class);
        INSTANCE = new ClientCraftListener();
    }
}
