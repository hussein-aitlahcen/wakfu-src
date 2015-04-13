package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.ui.protocol.message.dimensionalBag.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.core.event.*;

@XulorActionsTag
public class DimensionalBagAppearanceManagerDialogActions
{
    public static final String PACKAGE = "wakfu.appearanceManager";
    
    public static void selectAppearance(final ListSelectionChangedEvent e) {
        if (e.getSelected()) {
            final UIDimensionalBagSelectAppearanceMessage msg = new UIDimensionalBagSelectAppearanceMessage((UIDimensionalBagAppearanceManagerFrame.DimensionalBagCustomView)e.getValue());
            msg.setId(17012);
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void next(final Event e) {
        UIDimensionalBagAppearanceManagerFrame.getInstance().next();
    }
    
    public static void previous(final Event e) {
        UIDimensionalBagAppearanceManagerFrame.getInstance().previous();
    }
}
