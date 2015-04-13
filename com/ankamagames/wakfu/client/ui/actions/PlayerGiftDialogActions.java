package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.gift.*;
import com.ankamagames.wakfu.client.ui.protocol.message.playerGift.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;

@XulorActionsTag
public class PlayerGiftDialogActions
{
    public static final String PACKAGE = "wakfu.playerGift";
    
    public static void packageListOffset(final Event e, final List l, final String offsetS) {
        final float offset = PrimitiveConverter.getFloat(offsetS);
        l.setListOffset(l.getOffset() + offset);
    }
    
    public static void consumeOneGift(final Event event, final GiftItem gift) {
        final UIPlayerGiftMessage msg = new UIPlayerGiftMessage(gift);
        msg.setBooleanValue(false);
        msg.setId(16622);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void consumeAllGifts(final Event event, final GiftItem gift) {
        final UIPlayerGiftMessage msg = new UIPlayerGiftMessage(gift);
        msg.setBooleanValue(true);
        msg.setId(16622);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void selectGiftPackage(final ItemEvent e) {
        final UIPlayerGiftPackageMessage msg = new UIPlayerGiftPackageMessage((GiftPackage)e.getItemValue());
        msg.setId(16623);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void showItemDetailPopup(final ItemEvent itemEvent) {
        final GiftItem item = (GiftItem)itemEvent.getItemValue();
        PopupInfosActions.showPopup(item.getReferenceItem(), 0);
    }
    
    public static void hideItemDetailPopup(final ItemEvent itemEvent) {
        final GiftItem item = (GiftItem)itemEvent.getItemValue();
        PopupInfosActions.hidePopup(itemEvent, item.getReferenceItem());
    }
    
    public static void openCloseDialog(final Event event) {
        UIMessage.send((short)16620);
    }
    
    public static void openItemDetails(final Event e, final GiftItem gift, final Window window) {
        if (((MouseEvent)e).getButton() != 1) {
            final ReferenceItem referenceItem = gift.getReferenceItem();
            final Item item = new Item(referenceItem.getId());
            item.initializeWithReferenceItem(referenceItem);
            item.setQuantity((short)1);
            AbstractUIDetailMessage msg;
            if (item.hasPet()) {
                msg = new UIPetDetailMessage(new PetDetailDialogView(item));
                msg.setId(16430);
            }
            else {
                msg = new UIItemDetailMessage();
                msg.setId(16415);
                msg.setItem(item);
            }
            msg.setParentWindowId((window == null) ? null : window.getElementMap().getId());
            Worker.getInstance().pushMessage(msg);
        }
    }
}
