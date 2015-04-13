package com.ankamagames.wakfu.client.console.command.xulor;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;

public class LoadCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (!args.isEmpty()) {
            Xulor.getInstance().load(args.get(2), Dialogs.getDialogPath(args.get(2)), 16L, (short)0);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
