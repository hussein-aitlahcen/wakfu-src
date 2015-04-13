package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.account.*;

public class VideoCommand implements Command
{
    protected static Logger m_logger;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final LocalAccountInformations localAccount = WakfuGameEntity.getInstance().getLocalAccount();
        if (localAccount != null && !AdminRightHelper.checkRights(localAccount.getAdminRights(), AdminRightHelper.NO_RIGHT)) {
            if (Xulor.getInstance().isLoaded("videoDialog")) {
                Xulor.getInstance().unload("videoDialog");
                Xulor.getInstance().removeActionClass("wakfu.video");
            }
            else {
                Xulor.getInstance().load("videoDialog", Dialogs.getDialogPath("videoDialog"), 32769L, (short)30000);
                Xulor.getInstance().putActionClass("wakfu.video", VideoActions.class);
                VideoCommand.m_logger.info((Object)"Chargement de l'ui video");
            }
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        VideoCommand.m_logger = Logger.getLogger((Class)VideoCommand.class);
    }
}
