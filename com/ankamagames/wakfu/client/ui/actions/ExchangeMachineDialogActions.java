package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.exchangeMachine.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.exchangeMachine.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.*;

@XulorActionsTag
public class ExchangeMachineDialogActions
{
    public static final String PACKAGE = "wakfu.exchangeMachine";
    
    public static void selectExchange(final MouseEvent itemEvent, final Window window, final ExchangeEntryView exchangeEntryView) {
        if (!"exchangeBackground".equals(itemEvent.getTarget().getId())) {
            return;
        }
        if (itemEvent.getButton() == 3) {
            openItemDescription(itemEvent, exchangeEntryView, window);
        }
    }
    
    private static void openItemDescription(final MouseEvent e, final ExchangeEntryView exchangeEntryView, final Window window) {
        if (e.getButton() != 3) {
            return;
        }
        final IngredientView resulting = exchangeEntryView.getResulting();
        if (resulting == null) {
            return;
        }
        final ReferenceItem referenceItem = resulting.getReferenceItem();
        final Item item = Item.newInstance(referenceItem);
        item.initializeWithReferenceItem(referenceItem);
        AbstractUIDetailMessage msg;
        if (item.hasPet()) {
            msg = new UIPetDetailMessage(new PetDetailDialogView(item));
            msg.setId(16430);
        }
        else {
            msg = new UIItemDetailMessage();
            msg.setItem(item);
            msg.setId(16415);
        }
        msg.setX(MouseManager.getInstance().getX());
        final Widget w = e.getTarget();
        msg.setY(e.getScreenY() + w.getHeight());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void purchase(final Event event, final ExchangeEntryView exchangeEntryView) {
        final UIExchangeMachineMessage exchangeMachineMessage = new UIExchangeMachineMessage();
        exchangeMachineMessage.setExchangeView(exchangeEntryView);
        Worker.getInstance().pushMessage(exchangeMachineMessage);
    }
    
    public static void outExchange(final Event event, final Image image) {
        outExchange(event, image, null);
    }
    
    public static void overExchange(final Event event, final Image image) {
        overExchange(event, image, null);
    }
    
    public static void outExchange(final Event event, final Image image, final Widget w) {
        image.setDisplaySize(new Dimension(46, 46));
        if (w != null) {
            w.setVisible(false);
        }
    }
    
    public static void overExchange(final Event event, final Image image, final Widget w) {
        image.setDisplaySize(new Dimension(50, 50));
        if (w != null) {
            w.setVisible(true);
        }
    }
}
