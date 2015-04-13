package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.game.fight.history.*;

@XulorActionsTag
public class FightResultDialogActions
{
    public static final String PACKAGE = "wakfu.fightResult";
    
    public static void closeFightResultDialog(final Event event) {
        UIMessage.send((short)17730);
    }
    
    public static void openSpellsDetailsDialog(final Event event) {
        if (Xulor.getInstance().isLoaded("fightResultSpellsDetailsDialog")) {
            Xulor.getInstance().unload("fightResultSpellsDetailsDialog");
        }
        else {
            UIFightEndFrame.getInstance().loadSpellsDetailsDialog();
        }
    }
    
    public static void openSummonsDetailsDialog(final Event event) {
        if (Xulor.getInstance().isLoaded("fightResultSummonsDetailsDialog")) {
            Xulor.getInstance().unload("fightResultSummonsDetailsDialog");
        }
        else {
            UIFightEndFrame.getInstance().loadSummonsDetailsDialog();
        }
    }
    
    public static void openItemDetails(final ItemEvent event) {
        if (event.getType() == Events.ITEM_CLICK && event.getButton() == 3) {
            Actions.sendOpenCloseItemDetailMessage(null, ((ReferenceItemFieldProvider)event.getItemValue()).getReferenceItem());
        }
    }
}
