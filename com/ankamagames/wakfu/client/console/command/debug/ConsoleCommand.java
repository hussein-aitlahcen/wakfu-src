package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.account.*;

public class ConsoleCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final LocalAccountInformations localAccount = WakfuGameEntity.getInstance().getLocalAccount();
        if (localAccount != null && !AdminRightHelper.checkRights(localAccount.getAdminRights(), AdminRightHelper.NO_RIGHT)) {
            if (Xulor.getInstance().isLoaded("consoleDialog")) {
                Xulor.getInstance().unload("consoleDialog");
            }
            else {
                Xulor.getInstance().load("consoleDialog", Dialogs.getDialogPath("consoleDialog"), 1025L, (short)30000);
            }
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
