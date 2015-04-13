package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@XulorActionsTag
public class GuildImprovementDialogActions
{
    public static final String PACKAGE = "wakfu.guildImprovement";
    
    public static void incrementLevel(final Event e) {
        UIMessage.send((short)18222);
    }
    
    public static void activateBonus(final Event e, final GuildBonusView bonus) {
        final UIMessage msg = new UIMessage();
        msg.setIntValue(bonus.getId());
        msg.setId(18224);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void buyBonus(final Event e, final GuildBonusView bonus) {
        final UIMessage msg = new UIMessage();
        msg.setIntValue(bonus.getId());
        msg.setId(18223);
        Worker.getInstance().pushMessage(msg);
    }
}
