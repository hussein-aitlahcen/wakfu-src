package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.title.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.secret.*;

@XulorActionsTag
public class PassportDialogActions
{
    public static final String PACKAGE = "wakfu.passport";
    
    public static void selectTitle(final ListSelectionChangedEvent event) {
        if (event.getSelected()) {
            final PlayerTitle title = (PlayerTitle)event.getValue();
            final UIMessage msg = new UIMessage();
            msg.setId(17684);
            msg.setShortValue(title.getId());
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void setViewMode(final Event e, final String mode) {
        final int modeValue = PrimitiveConverter.getInteger(mode, 0);
        PropertiesProvider.getInstance().setPropertyValue("passportViewMode", modeValue);
    }
    
    public static void setCategory(final Event e, final String mode) {
        final int category = PrimitiveConverter.getInteger(mode, 0);
        PropertiesProvider.getInstance().setPropertyValue("passportCategory", category);
    }
    
    public static void previousSecretPage(final Event e, final SecretsView secrets) {
        secrets.previousPage();
    }
    
    public static void nextSecretPage(final Event e, final SecretsView secrets) {
        secrets.nextPage();
    }
}
