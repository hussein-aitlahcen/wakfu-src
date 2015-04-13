package com.ankamagames.wakfu.client.console.command.xulor;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.xulor2.*;

public class TextureDiagResetCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        Xulor.getInstance().getDocumentParser().resetPixmapRegistration();
        manager.trace("Remise \u00e0 zero");
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
