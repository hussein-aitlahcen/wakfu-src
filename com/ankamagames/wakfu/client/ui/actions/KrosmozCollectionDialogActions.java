package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.krosmoz.collection.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.krozmoz.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;

@XulorActionsTag
public class KrosmozCollectionDialogActions
{
    public static final String PACKAGE = "wakfu.krosmozCollection";
    
    public static void previousSeason(final Event e) {
        UIMessage.send((short)17352);
    }
    
    public static void nextSeason(final Event e) {
        UIMessage.send((short)17353);
    }
    
    public static void deleteFigure(final ItemEvent e) {
        final KrosmozFigureView view = (KrosmozFigureView)e.getItemValue();
        final KrosmozFigureData figure = view.getFigure();
        final AbstractUIMessage msg = new UIMessage();
        msg.setId(17351);
        msg.setIntValue(figure.getFigureId());
        Worker.getInstance().pushMessage(msg);
    }
}
