package com.ankamagames.wakfu.client.core.game.item.ui;

import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.text.flying.*;

public class ItemFeedbackHelper
{
    public static void sendChatItemUndroppableMessage(final Item item) {
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("item.unDroppable", item.getQuantity(), UIChatFrame.getItemFormatedForChatLinkString(item)));
        chatMessage.setPipeDestination(3);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    public static void sendChatItemUndeletableMessage(final Item item) {
        final ChatMessage errorMsg = new ChatMessage(WakfuTranslator.getInstance().getString("item.unDeletable"));
        errorMsg.setPipeDestination(3);
        ChatManager.getInstance().pushMessage(errorMsg);
    }
    
    public static void sendChatItemsAddedMessage(final ArrayList<Item> items) {
        sendChatItemsAddedMessage(items, 4);
    }
    
    public static void sendChatItemsAddedMessage(final ArrayList<Item> items, final int pipeDestination) {
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("item.selfLoot", getItemList(items)));
        chatMessage.setPipeDestination(pipeDestination);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    private static String getItemList(final ArrayList<Item> items) {
        String itemList = "";
        boolean first = true;
        for (final Item item : items) {
            if (!first) {
                itemList += ", ";
            }
            itemList += WakfuTranslator.getInstance().getString("item.quantity", item.getQuantity(), UIChatFrame.getItemFormatedForChatLinkString(item));
            first = false;
        }
        return itemList;
    }
    
    public static void sendChatItemAddedMessage(final Item item) {
        sendChatItemAddedMessage(item, 4);
    }
    
    public static void sendChatItemAddedMessage(final Item item, final int pipeDestination) {
        sendChatItemAddedMessage(item, item.getQuantity(), pipeDestination);
    }
    
    public static void sendChatItemAddedMessage(final Item item, final short quantity) {
        sendChatItemAddedMessage(item, quantity, 4);
    }
    
    public static void sendChatItemAddedMessage(final Item item, final short quantity, final int pipeDestination) {
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("item.selfLoot", WakfuTranslator.getInstance().getString("item.quantity", quantity, UIChatFrame.getItemFormatedForChatLinkString(item))));
        chatMessage.setPipeDestination(pipeDestination);
        ChatManager.getInstance().pushMessage(chatMessage);
        displayFlyingItemGain(item);
    }
    
    public static void sendChatItemsRemovedMessage(final ArrayList<Item> items) {
        sendChatItemsRemovedMessage(items, 4);
    }
    
    public static void sendChatItemsRemovedMessage(final ArrayList<Item> items, final int pipeDestination) {
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("item.selfDrop", getItemList(items)));
        chatMessage.setPipeDestination(pipeDestination);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    public static void sendChatItemRemovedMessage(final Item item, final int pipeDestination) {
        sendChatItemRemovedMessage(item, item.getQuantity(), pipeDestination);
    }
    
    public static void sendChatItemRemovedMessage(final int refId, final short quantity) {
        final Item item = ReferenceItemManager.getInstance().getDefaultItem(refId);
        sendChatItemRemovedMessage(item, quantity, 4);
    }
    
    public static void sendChatItemRemovedMessage(final Item item, final short quantity) {
        sendChatItemRemovedMessage(item, quantity, 4);
    }
    
    public static void sendChatItemRemovedMessage(final Item item, final short quantity, final int pipeDestination) {
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("item.selfDrop", WakfuTranslator.getInstance().getString("item.quantity", quantity, UIChatFrame.getItemFormatedForChatLinkString(item))));
        chatMessage.setPipeDestination(pipeDestination);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    public static void displayFlyingItemGain(final Item item) {
        if (item != null) {
            displayFlyingItemGain(item.getReferenceItem());
        }
    }
    
    public static void displayFlyingItemGain(final int refId, final WorldPositionable target, final byte sex) {
        final AbstractReferenceItem item = ReferenceItemManager.getInstance().getReferenceItem(refId);
        if (item != null && target != null) {
            displayFlyingItem(item, target, sex);
        }
    }
    
    public static void displayFlyingItemGain(final AbstractReferenceItem item) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterActor actor = localPlayer.getActor();
        if (!actor.isVisible()) {
            return;
        }
        displayFlyingItem(item, actor, localPlayer.getSex());
    }
    
    private static void displayFlyingItem(final AbstractReferenceItem item, final WorldPositionable actor, final byte sex) {
        final FlyingImage.DefaultFlyingImageDeformer deformer = new FlyingImage.DefaultFlyingImageDeformer();
        final int gfxId = (sex == 0) ? item.getGfxId() : item.getFemaleGfxId();
        final AbstractDelayedAdviser flyingImage = new FlyingImage(WakfuConfiguration.getInstance().getItemSmallIconUrl(gfxId), 32, 32, deformer, 3000);
        flyingImage.setTarget(actor);
        final int adviserCount = AdviserManager.getInstance().countAdviserOfType(actor, 4);
        flyingImage.setWaitingTime(adviserCount * 500);
        AdviserManager.getInstance().addAdviser(flyingImage);
    }
}
