package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.protocol.message.guild.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;

@XulorActionsTag
public class GuildCreatorDialogActions
{
    public static final String PACKAGE = "wakfu.guildBannerCreator";
    
    public static void selectColor(final ItemEvent e) {
        final UIGuildBannerSetColorMessage msg = new UIGuildBannerSetColorMessage();
        msg.setId(18200);
        msg.setColor((Color)e.getItemValue());
        Worker.getInstance().pushMessage(msg);
    }
    
    private static void sendColorTypeChangeMessage(final GuildBannerGenerator.GuildBannerColorType type, final Container c1, final Container c2) {
        c1.setVisible(false);
        c2.setVisible(true);
        final UIGuildBannerSetColorMessage msg = new UIGuildBannerSetColorMessage();
        msg.setId(18203);
        msg.setType(type);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void setFirstPartBackgroundColorType(final Event e, final Container c1, final Container c2) {
        sendColorTypeChangeMessage(GuildBannerGenerator.GuildBannerColorType.BACKGROUND, c1, c2);
    }
    
    public static void setFirstPartForegroundColorType(final Event e, final Container c1, final Container c2) {
        sendColorTypeChangeMessage(GuildBannerGenerator.GuildBannerColorType.FOREGROUND, c1, c2);
    }
    
    public static void closeColorPicker(final Event e, final Container c1, final Container c2) {
        c1.setVisible(true);
        c2.setVisible(false);
    }
    
    public static void nextBackground(final Event e) {
        final UIMessage msg = new UIMessage();
        msg.setId(18201);
        msg.setIntValue(1);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void previousBackground(final Event e) {
        final UIMessage msg = new UIMessage();
        msg.setId(18201);
        msg.setIntValue(-1);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void nextForeground(final Event e) {
        final UIMessage msg = new UIMessage();
        msg.setId(18202);
        msg.setIntValue(1);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void previousForeground(final Event e) {
        final UIMessage msg = new UIMessage();
        msg.setId(18202);
        msg.setIntValue(-1);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void createGuild(final Event e, final TextEditor te, final String withKamas) {
        final boolean useKama = Boolean.parseBoolean(withKamas);
        final String guildName = te.getText();
        final UIGuildCreateMessage msg = new UIGuildCreateMessage();
        final String capitalizedGuildName = StringUtils.capitalizeWords(guildName).replaceAll("[?]", "");
        if (!capitalizedGuildName.equals(guildName)) {
            te.setTextImmediate(capitalizedGuildName);
        }
        msg.setName(capitalizedGuildName);
        msg.setBooleanValue(useKama);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void closeDialog(final Event e) {
        WakfuGameEntity.getInstance().removeFrame(UIGuildCreatorFrame.getInstance());
    }
}
