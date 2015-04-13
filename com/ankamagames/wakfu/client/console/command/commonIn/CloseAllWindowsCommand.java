package com.ankamagames.wakfu.client.console.command.commonIn;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;

public class CloseAllWindowsCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final boolean visible = !MasterRootContainer.getInstance().getVisible();
        if (visible || ModalManager.getInstance().isEmpty()) {
            MasterRootContainer.getInstance().setVisible(visible);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
