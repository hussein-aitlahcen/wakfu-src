package com.ankamagames.wakfu.client.console.command.xulor;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.*;

public class DialogInfosCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (args.size() == 3) {
            final Widget w = (Widget)Xulor.getInstance().getLoadedElement(args.get(2));
            if (w != null) {
                manager.trace(w.toString());
            }
            else {
                manager.err("La dialog " + args.get(2) + " n'existe pas.");
            }
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
