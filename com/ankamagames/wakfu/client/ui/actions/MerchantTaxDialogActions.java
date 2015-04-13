package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.Merchant.*;
import com.ankamagames.xulor2.event.*;

@XulorActionsTag
public class MerchantTaxDialogActions
{
    protected static Logger m_logger;
    public static final String PACKAGE = "wakfu.merchant.tax";
    
    public static void priceChanged(final Event event) {
        final TextEditor textEditor = event.getTarget();
        String s = textEditor.getText();
        if (s.length() == 0) {
            return;
        }
        final long l = Long.parseLong(s);
        if (l > 2147483647L) {
            final String maxString = String.valueOf(Integer.MAX_VALUE);
            textEditor.setText(maxString);
            s = maxString;
        }
        final int price = Integer.parseInt(s);
        final UIMessage msg = new UIMessage();
        msg.setId(17300);
        msg.setIntValue(price);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void packChanged(final ListSelectionChangedEvent event) {
        if (event.getSelected()) {
            final PackTypeFieldProvider packType = (PackTypeFieldProvider)event.getValue();
            final UIMerchantChangePackTypeMessage msg = new UIMerchantChangePackTypeMessage();
            msg.setPackType(packType.getPackType());
            msg.setId(17302);
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void durationChanged(final ListSelectionChangedEvent event) {
        if (event.getSelected()) {
            final MerchantInventoryItem.DurationFieldProvider duration = (MerchantInventoryItem.DurationFieldProvider)event.getValue();
            final UIMerchantChangeDurationMessage msg = new UIMerchantChangeDurationMessage();
            msg.setDuration(duration.getDuration());
            msg.setId(17311);
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void valid(final Event event) {
        if (event instanceof KeyEvent && ((KeyEvent)event).getKeyCode() != 10) {
            return;
        }
        UIMessage.send((short)17308);
    }
    
    public static void cancel(final Event event) {
        UIMessage.send((short)17309);
    }
    
    static {
        MerchantTaxDialogActions.m_logger = Logger.getLogger((Class)MerchantTaxDialogActions.class);
    }
}
