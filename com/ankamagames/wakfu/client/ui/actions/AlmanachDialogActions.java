package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.almanach.*;

@XulorActionsTag
public class AlmanachDialogActions
{
    public static final String PACKAGE = "wakfu.almanach";
    
    public static void previousMonth(final Event e) {
        AlmanachView.INSTANCE.previousMonth();
    }
    
    public static void nextMonth(final Event e) {
        AlmanachView.INSTANCE.nextMonth();
    }
    
    public static void displayRewardMachine(final Event e) {
        final IEExchangeParameter param = (IEExchangeParameter)IEParametersManager.INSTANCE.getParam(IETypes.EXCHANGE_MACHINE, 53);
        final String name = WakfuTranslator.getInstance().getString(107, 53, new Object[0]);
        UIExchangeMachineFrame.getInstance().setExchangeMachineParameters(param, name);
    }
    
    public static void selectDate(final ItemEvent e) {
        final AlmanachDateView view = (AlmanachDateView)e.getItemValue();
        if (view != null) {
            AlmanachView.INSTANCE.setDisplayedDate(view.getDate());
        }
    }
}
