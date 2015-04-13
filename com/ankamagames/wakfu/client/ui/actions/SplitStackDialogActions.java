package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.Merchant.*;
import com.ankamagames.wakfu.client.ui.protocol.message.market.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.ui.protocol.message.exchange.*;

@XulorActionsTag
public class SplitStackDialogActions
{
    public static final String PACKAGE = "wakfu.split";
    public static byte m_sourcePosition;
    public static byte m_destinationPosition;
    public static long m_sourceUniqueId;
    public static long m_destinationUniqueId;
    private static int m_maxQuantity;
    private static Item m_item;
    private static MerchantInventoryItem m_merchantItem;
    private static short m_messageType;
    private static Long m_exchangeId;
    private static int m_currentQuantity;
    private static CharacterView m_sourceCharacter;
    private static CharacterView m_destinationCharacter;
    
    public static void decreaseSplitCount(final Event event, final TextEditor te) {
        if (te.getText().isEmpty()) {
            return;
        }
        int quantity = PrimitiveConverter.getInteger(te.getText());
        --quantity;
        setQuantity(te, quantity, true);
    }
    
    public static void keyType(final Event event, final TextEditor te) {
        if (event instanceof KeyEvent && ((KeyEvent)event).getKeyChar() == '\n') {
            validSplit(event);
            return;
        }
        if (te.getText().isEmpty()) {
            return;
        }
        final int quantity = PrimitiveConverter.getInteger(te.getText());
        setQuantity(te, quantity, false);
    }
    
    private static void setQuantity(final TextEditor te, int quantity, boolean applyText) {
        if (quantity < 0) {
            quantity = 0;
            applyText = true;
        }
        else if (quantity > SplitStackDialogActions.m_maxQuantity) {
            quantity = SplitStackDialogActions.m_maxQuantity;
            applyText = true;
        }
        SplitStackDialogActions.m_currentQuantity = quantity;
        if (applyText) {
            te.setText(String.valueOf(quantity));
        }
    }
    
    public static void increaseSplitCount(final Event event, final TextEditor te) {
        if (te.getText().isEmpty()) {
            return;
        }
        int quantity = PrimitiveConverter.getInteger(te.getText());
        ++quantity;
        setQuantity(te, quantity, true);
    }
    
    public static void validSplit(final Event event) {
        int money = 0;
        short quantity = 0;
        if (isMoneyTransfer()) {
            money = SplitStackDialogActions.m_currentQuantity;
        }
        else {
            quantity = (short)SplitStackDialogActions.m_currentQuantity;
        }
        Xulor.getInstance().unload("splitStackDialog");
        if (quantity > 0 || money > 0) {
            switch (SplitStackDialogActions.m_messageType) {
                case 16825: {
                    final UIItemMessage message = new UIItemMessage();
                    message.setId(SplitStackDialogActions.m_messageType);
                    message.setItem(SplitStackDialogActions.m_item);
                    message.setQuantity(quantity);
                    message.setSourceUniqueId(SplitStackDialogActions.m_sourceUniqueId);
                    message.setDestinationUniqueId(SplitStackDialogActions.m_destinationUniqueId);
                    message.setDestinationPosition(SplitStackDialogActions.m_destinationPosition);
                    Worker.getInstance().pushMessage(message);
                    return;
                }
                case 19324:
                case 19325: {
                    final UIMessage message2 = new UIMessage();
                    message2.setIntValue(money);
                    message2.setId(SplitStackDialogActions.m_messageType);
                    Worker.getInstance().pushMessage(message2);
                    return;
                }
                case 19321:
                case 19327: {
                    final UIItemMessage message = new UIItemMessage();
                    message.setId(SplitStackDialogActions.m_messageType);
                    message.setItem(SplitStackDialogActions.m_item);
                    message.setQuantity(quantity);
                    message.setDestinationUniqueId(SplitStackDialogActions.m_destinationUniqueId);
                    message.setDestinationPosition(SplitStackDialogActions.m_destinationPosition);
                    Worker.getInstance().pushMessage(message);
                    return;
                }
                case 19140:
                case 19141:
                case 19320:
                case 19326:
                case 19333: {
                    final UIItemMessage message = new UIItemMessage();
                    message.setId(SplitStackDialogActions.m_messageType);
                    message.setLongValue(SplitStackDialogActions.m_item.getUniqueId());
                    message.setShortValue(quantity);
                    message.setDestinationPosition(SplitStackDialogActions.m_destinationPosition);
                    Worker.getInstance().pushMessage(message);
                    return;
                }
                case 17304: {
                    final UIMerchantMessage message3 = new UIMerchantMessage();
                    message3.setDestinationPosition(SplitStackDialogActions.m_destinationPosition);
                    message3.setItem(SplitStackDialogActions.m_item);
                    message3.setQuantity(quantity);
                    message3.setId(SplitStackDialogActions.m_messageType);
                    Worker.getInstance().pushMessage(message3);
                    return;
                }
                case 16844: {
                    final UIItemMessage message = new UIItemMessage();
                    message.setItem(SplitStackDialogActions.m_item);
                    message.setQuantity(quantity);
                    message.setId(SplitStackDialogActions.m_messageType);
                    Worker.getInstance().pushMessage(message);
                    return;
                }
                case 19258: {
                    final UIMarketAddItem uiMarketItemMessage = new UIMarketAddItem();
                    uiMarketItemMessage.setItem(SplitStackDialogActions.m_item);
                    uiMarketItemMessage.setShortValue(quantity);
                    Worker.getInstance().pushMessage(uiMarketItemMessage);
                    return;
                }
                case 17305: {
                    final UIMerchantMessage uiMsg = new UIMerchantMessage();
                    uiMsg.setMerchantItem(SplitStackDialogActions.m_merchantItem);
                    uiMsg.setDestinationPosition(SplitStackDialogActions.m_destinationPosition);
                    uiMsg.setContainerId(SplitStackDialogActions.m_destinationUniqueId);
                    uiMsg.setQuantity(quantity);
                    uiMsg.setId(17305);
                    Worker.getInstance().pushMessage(uiMsg);
                    final UIMarketAddItem uiMarketItemMessage2 = new UIMarketAddItem();
                    uiMarketItemMessage2.setItem(SplitStackDialogActions.m_item);
                    uiMarketItemMessage2.setShortValue(quantity);
                    Worker.getInstance().pushMessage(uiMarketItemMessage2);
                    return;
                }
            }
            if (ClientTradeHelper.INSTANCE.isTradeRunning()) {
                Xulor.getInstance().unload("splitStackDialog");
                if (quantity > 0) {
                    final UIExchangeMoveItemMessage message4 = new UIExchangeMoveItemMessage();
                    message4.setExchangeId(SplitStackDialogActions.m_exchangeId);
                    message4.setItem(SplitStackDialogActions.m_item);
                    message4.setItemQuantity(quantity);
                    message4.setId(SplitStackDialogActions.m_messageType);
                    Worker.getInstance().pushMessage(message4);
                }
            }
            else {
                final UIItemMessage message = new UIItemMessage();
                message.setDestinationPosition(SplitStackDialogActions.m_destinationPosition);
                message.setSourcePosition(SplitStackDialogActions.m_sourcePosition);
                message.setSourceUniqueId(SplitStackDialogActions.m_sourceUniqueId);
                message.setDestinationUniqueId(SplitStackDialogActions.m_destinationUniqueId);
                message.setSourceCharacter(SplitStackDialogActions.m_sourceCharacter);
                message.setItem(SplitStackDialogActions.m_item);
                message.setQuantity(quantity);
                message.setId(SplitStackDialogActions.m_messageType);
                Worker.getInstance().pushMessage(message);
            }
        }
    }
    
    private static boolean isMoneyTransfer() {
        return SplitStackDialogActions.m_messageType == 16823 || SplitStackDialogActions.m_messageType == 19324 || SplitStackDialogActions.m_messageType == 19325;
    }
    
    public static void setMaxQuantity(final int quantity) {
        SplitStackDialogActions.m_maxQuantity = quantity;
    }
    
    public static void setMessageType(final short messageType) {
        SplitStackDialogActions.m_messageType = messageType;
    }
    
    public static void setExchangeId(final long id) {
        SplitStackDialogActions.m_exchangeId = id;
    }
    
    public static void setMerchantItem(final MerchantInventoryItem merchantItem) {
        SplitStackDialogActions.m_merchantItem = merchantItem;
    }
    
    public static void setItem(final Item item) {
        SplitStackDialogActions.m_item = item;
        SplitStackDialogActions.m_currentQuantity = item.getQuantity();
    }
    
    public static void setSourceUniqueId(final long uid) {
        SplitStackDialogActions.m_sourceUniqueId = uid;
    }
    
    public static void setSourcePosition(final byte position) {
        SplitStackDialogActions.m_sourcePosition = position;
    }
    
    public static void setDestinationPosition(final Byte position) {
        SplitStackDialogActions.m_destinationPosition = position;
    }
    
    public static void setDestinationUniqueId(final long uid) {
        SplitStackDialogActions.m_destinationUniqueId = uid;
    }
    
    public static byte getSourcePosition() {
        return SplitStackDialogActions.m_sourcePosition;
    }
    
    public static byte getDestinationPosition() {
        return SplitStackDialogActions.m_destinationPosition;
    }
    
    public static long getSourceUniqueId() {
        return SplitStackDialogActions.m_sourceUniqueId;
    }
    
    public static long getDestinationUniqueId() {
        return SplitStackDialogActions.m_destinationUniqueId;
    }
    
    public static int getMaxQuantity() {
        return SplitStackDialogActions.m_maxQuantity;
    }
    
    public static Item getItem() {
        return SplitStackDialogActions.m_item;
    }
    
    public static MerchantInventoryItem getMerchantItem() {
        return SplitStackDialogActions.m_merchantItem;
    }
    
    public static short getMessageType() {
        return SplitStackDialogActions.m_messageType;
    }
    
    public static Long getExchangeId() {
        return SplitStackDialogActions.m_exchangeId;
    }
    
    public static CharacterView getDestinationCharacter() {
        return SplitStackDialogActions.m_destinationCharacter;
    }
    
    public static void setDestinationCharacter(final CharacterView destinationCharacter) {
        SplitStackDialogActions.m_destinationCharacter = destinationCharacter;
    }
    
    public static CharacterView getSourceCharacter() {
        return SplitStackDialogActions.m_sourceCharacter;
    }
    
    public static void setSourceCharacter(final CharacterView sourceCharacter) {
        SplitStackDialogActions.m_sourceCharacter = sourceCharacter;
    }
}
